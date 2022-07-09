package com.teethcare.service.impl.booking;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.teethcare.common.Message;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.*;
import com.teethcare.repository.AppointmentRepository;
import com.teethcare.repository.BookingRepository;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import com.teethcare.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final PatientService patientService;
    private final DentistService dentistService;
    private final ClinicService clinicService;
    private final AppointmentRepository appointmentRepository;
    private final VoucherService voucherService;

    @Override
    public List<Booking> findAll() {
        //TODO
        return null;
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.findBookingById(id);
    }

    @Override
    public void save(Booking entity) {
        bookingRepository.save(entity);
    }

    @Override
    public void delete(int id) {
        Booking booking = findById(id);
        booking.setStatus(Status.Booking.UNAVAILABLE.name());
        save(booking);
    }

    @Override
    public void update(Booking entity) {
        //TODO
    }

    @Override
    public Booking saveBooking(BookingRequest bookingRequest, Account account) {
        Booking bookingTmp = bookingMapper.mapBookingRequestToBooking(bookingRequest);
        //set service to booking
        int serviceID = bookingRequest.getServiceId();
        ServiceOfClinic service = serviceOfClinicService.findById(serviceID);
        List<ServiceOfClinic> serviceOfClinicList = new ArrayList<>();
        serviceOfClinicList.add(service);
        bookingTmp.setServices(serviceOfClinicList);

        //set clinic to booking
        Clinic clinic = service.getClinic();
        bookingTmp.setClinic(clinic);

        Timestamp desiredCheckingTime = new Timestamp(bookingRequest.getDesiredCheckingTime());
        Timestamp now = new Timestamp(System.currentTimeMillis());

        if (desiredCheckingTime.compareTo(now) < 0) {
            throw new BadRequestException("Desired checking time invalid");
        }

        LocalTime checkedTime = desiredCheckingTime.toLocalDateTime().toLocalTime();
        LocalTime startTimeShift1 = clinic.getStartTimeShift1().toLocalTime();
        LocalTime startTimeShift2 = clinic.getStartTimeShift2().toLocalTime();
        LocalTime endTimeShift1 = clinic.getEndTimeShift1().toLocalTime();
        LocalTime endTimeShift2 = clinic.getEndTimeShift2().toLocalTime();
        boolean isValidWorkTime = checkedTime.isAfter(endTimeShift2) || checkedTime.isBefore(startTimeShift1)
                || checkedTime.isAfter(endTimeShift1) && checkedTime.isBefore(startTimeShift2);

        if (isValidWorkTime) {
            throw new BadRequestException(Message.OUT_OF_WORKING_TIME.name());
        }
        bookingTmp.setDesiredCheckingTime(desiredCheckingTime);
        bookingTmp.setCreateBookingTime(now);

        //set patient to booking
        Patient patient = patientService.findById(account.getId());
        bookingTmp.setPatient(patient);
        bookingTmp.setStatus(Status.Booking.PENDING.name());

        //voucher section
        if (bookingRequest.getVoucherCode() != null) {
            Voucher voucher = voucherService.findActiveByVoucherCode(bookingRequest.getVoucherCode());
            bookingTmp.setVoucher(voucher);
            voucherService.useVoucher(bookingRequest.getVoucherCode(), clinic);
        }

        if (patient != null && !serviceOfClinicList.isEmpty()) {
            return bookingRepository.save(bookingTmp);
        }
        return null;
    }

    @Override
    public Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable) {
        return bookingRepository.findAll(bookingSpecification, pageable);
    }

    @Override
    public Page<Booking> findAll(String role, int accountId,
                                 BookingFilterRequest filterRequest,
                                 Pageable pageable) {

        Sort sort = pageable.getSort();

        switch (Role.valueOf(role)) {
            case CUSTOMER_SERVICE:
                Clinic clinic = clinicService.findClinicByCustomerServiceId(accountId);
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByClinicAndStatusIsNotNull(clinic, sort);

                bookingListForCustomerService = bookingListForCustomerService.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());
                return PaginationAndSortFactory.convertToPage(bookingListForCustomerService, pageable);
            case PATIENT:
                List<Booking> bookingListForPatient = bookingRepository.findBookingByPatientIdAndStatusIsNotNull(accountId, sort);

                bookingListForPatient = bookingListForPatient.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

                return PaginationAndSortFactory.convertToPage(bookingListForPatient, pageable);
            case DENTIST:
                List<String> statuses = List.of(Status.Booking.TREATMENT.name(), Status.Booking.DONE.name());
                List<Booking> bookingListForDentist = bookingRepository.findBookingByDentistIdAndStatusIn(accountId, statuses, sort);

                bookingListForDentist = bookingListForDentist.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

                return PaginationAndSortFactory.convertToPage(bookingListForDentist, pageable);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean confirmBookingRequest(int bookingId, CustomerService customerService, ObjectNode objectNode) {
        Booking booking = findBookingById(bookingId);

        boolean isAccepted = objectNode.get("isAccepted").asBoolean();

        if (isAccepted) {
            booking.setStatus(Status.Booking.REQUEST.name());
            booking.setCustomerService(customerService);

            save(booking);
            return true;
        } else {
            String rejectedNote;
            log.info("rejectedNote: " + objectNode.get("rejectedNote"));
            if (objectNode.get("rejectedNote") == null) {
                rejectedNote = null;
            } else {
                rejectedNote = objectNode.get("rejectedNote").asText();
            }
            booking.setStatus(Status.Booking.REJECTED.name());
            booking.setRejectedNote(rejectedNote);
            booking.setCustomerService(customerService);

            save(booking);
            return false;
        }
    }

    @Override
    public void rejectBookingRequest(int bookingId) {
        Booking booking = findBookingById(bookingId);

        long currentTime = System.currentTimeMillis();
        boolean notOver120s = (currentTime - booking.getCreateBookingTime().getTime()) <= 120 * 1000;
        log.info("It is not over 120s: " + notOver120s);
        if (notOver120s) {
            booking.setStatus(Status.Booking.REJECTED.name());
        } else {
            throw new BadRequestException(Message.UPDATE_FAIL.name() + ": Over 120s");
        }
        save(booking);
    }

    @Override
    public boolean confirmFinalBooking(BookingUpdateRequest bookingUpdateRequest) {
        int bookingId = bookingUpdateRequest.getBookingId();

        if (bookingUpdateRequest.getVersion() == null) {
            throw new BadRequestException(Message.PARAMS_MISSING.name());
        }

        Booking booking = findBookingById(bookingId);

        if (booking.getVersion() != bookingUpdateRequest.getVersion() || booking.isConfirmed()) {
            return false;
        }

        booking.setConfirmed(true);
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public List<Booking> findBookingByClinic(Clinic clinic) {
        return bookingRepository.findBookingByClinic(clinic);
    }

    @Transactional
    public boolean updateStatus(int bookingId, boolean isCheckin) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking is not existed!");
        }
        String status = booking.getStatus();
        switch (Status.Booking.valueOf(status)) {
            case REQUEST:
                if (!isCheckin) {
                    throw new BadRequestException("Your booking status is " + booking.getStatus() + " not valid for checkin");
                }
                if (booking.getExaminationTime() == null || booking.getDentist() == null
                        || booking.getCustomerService() == null || booking.getServices() == null) {
                    return false;
                }
                if (System.currentTimeMillis() - booking.getExaminationTime().getTime() >= 10 * 60 * 1000
                        || System.currentTimeMillis() - booking.getExaminationTime().getTime() <= -10 * 60 * 1000) {
                    throw new BadRequestException("Your checkin time is " + booking.getExaminationTime()
                            + ". You are soon/late at least for 10 minutes");
                }
                booking.setStatus(Status.Booking.TREATMENT.name());
                break;
            case TREATMENT:
                if (isCheckin) {
                    throw new BadRequestException("Your booking status is " + booking.getStatus() + " not valid for checkout");
                }
                if (booking.getExaminationTime() == null || booking.getDentist() == null
                        || booking.getCustomerService() == null || booking.getServices() == null || booking.getTotalPrice() == null
                        || !booking.isConfirmed()) {
                    return false;
                }
                booking.setStatus(Status.Booking.DONE.name());
                break;
            default:
                throw new BadRequestException("Your booking status is " + booking.getStatus() + " not valid for checkin/checkout");
        }
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public boolean checkAvailableTime(CheckAvailableTimeRequest checkAvailableTimeRequest) {
        boolean check;
        Clinic clinic = clinicService.findById(checkAvailableTimeRequest.getClinicId());
        if (clinic == null) {
            throw new BadRequestException("Clinic ID " + checkAvailableTimeRequest.getClinicId() + " not found!");
        }
        Timestamp lowerBound = new Timestamp(checkAvailableTimeRequest.getDesiredCheckingTime() - clinic.getBookingGap() * 60 * 1000);
        Timestamp upperBound = new Timestamp(checkAvailableTimeRequest.getDesiredCheckingTime() + clinic.getBookingGap() * 60 * 1000);
        List<Booking> queryBookingList =
                bookingRepository.findAllBookingByClinicIdAndDesiredCheckingTimeBetweenOrExaminationTimeBetween(checkAvailableTimeRequest.getClinicId(),
                        lowerBound, upperBound, lowerBound, upperBound);
        long now = System.currentTimeMillis();
        LocalTime checkedTime = new Timestamp(checkAvailableTimeRequest.getDesiredCheckingTime()).toLocalDateTime().toLocalTime();
        boolean isInvalidWorkTime = checkedTime.isAfter(clinic.getEndTimeShift2().toLocalTime()) || checkedTime.isBefore(clinic.getStartTimeShift1().toLocalTime())
                || checkedTime.isAfter(clinic.getStartTimeShift2().toLocalTime()) && checkedTime.isBefore(clinic.getStartTimeShift2().toLocalTime());
        check = !isInvalidWorkTime
                && (clinic.getDentists().size() - queryBookingList.size() > 0)
                && checkAvailableTimeRequest.getDesiredCheckingTime() >= now;
        return check;
    }

    @Override
    public List<Integer> getAvailableTime(GetAvailableTimeRequest getAvailableTimeRequest) {
        Clinic neededClinic = clinicService.findById(getAvailableTimeRequest.getClinicId());

        if (neededClinic == null) {
            throw new NotFoundException("Invalid clinic Id");
        }

        List<Integer> defaultTimes = IntStream.range(LocalTime.MIN.getHour(), LocalTime.MAX.getHour()).mapToObj(i -> i).collect(Collectors.toList());

        List<Integer> shiftRange1 = IntStream.range(TimeUtils.ceilHour(neededClinic.getStartTimeShift1()), TimeUtils.floorHour(neededClinic.getEndTimeShift1())).mapToObj(i -> i).collect(Collectors.toList());
        List<Integer> shiftRange2 = IntStream.range(TimeUtils.ceilHour(neededClinic.getStartTimeShift2()), TimeUtils.floorHour(neededClinic.getEndTimeShift2())).mapToObj(i -> i).collect(Collectors.toList());

        List<Integer> clinicWorkingTimes = defaultTimes.stream().filter(hour -> (shiftRange1.contains(hour) || shiftRange2.contains(hour))).collect(Collectors.toList());

        List<Integer> availableTimes = clinicWorkingTimes.stream().filter(hour -> {
                    CheckAvailableTimeRequest check = new CheckAvailableTimeRequest(
                            getAvailableTimeRequest.getClinicId(),
                            ConvertUtils.getDate(getAvailableTimeRequest.getDate()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (hour * 60 * 60 * 1000)
                    );
                    return checkAvailableTime(check);
                }
        ).collect(Collectors.toList());

        return availableTimes;
    }

    @Override
    public Booking findBookingById(int id) {
        return bookingRepository.findBookingById(id);
    }

    @Override
    public void firstlyUpdated(BookingUpdateRequest bookingUpdateRequest, boolean isAllDeleted) {
        int bookingId = bookingUpdateRequest.getBookingId();
        List<Integer> servicesIds = bookingUpdateRequest.getServiceIds();
        Integer dentistId = bookingUpdateRequest.getDentistId();
        Long examinationTimeRequest = bookingUpdateRequest.getExaminationTime();

        Booking booking = bookingRepository.findBookingById(bookingId);

        if (dentistId == null || examinationTimeRequest == null) {
            throw new BadRequestException(Message.PARAMS_MISSING.name());
        }

        List<ServiceOfClinic> services = new ArrayList<>();
        if (servicesIds != null) {
            services = servicesIds.stream()
                    .map(serviceOfClinicService::findById)
                    .collect(Collectors.toList());
        } else {
            if (!isAllDeleted) {
                services = booking.getServices();
            }
        }

        Timestamp examinationTime = new Timestamp(examinationTimeRequest);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        if (examinationTime.compareTo(currentTime) < 0) {
            throw new BadRequestException(Message.DATE_ERROR.name());
        }

        Dentist dentist = dentistService.findActive(dentistId);
        List<Booking> checkedBookings = new ArrayList<>();
        checkedBookings.addAll(bookingRepository.findBookingByStatusAndExaminationTimeAndDentistId(Status.Booking.TREATMENT.name(), examinationTime, dentistId));
        checkedBookings.addAll(bookingRepository.findBookingByStatusAndExaminationTimeAndDentistId(Status.Booking.REQUEST.name(), examinationTime, dentistId));
        if (!checkedBookings.isEmpty()) {
            throw new BadRequestException(Message.DENTIST_NO_AVAILABLE.name());
        }

        booking.setServices(services);
        booking.setExaminationTime(examinationTime);
        booking.setDentist(dentist);

        save(booking);
    }

    @Override
    public boolean secondlyUpdated(BookingUpdateRequest bookingUpdateRequest, boolean isAllDeleted) {
        int bookingId = bookingUpdateRequest.getBookingId();
        List<Integer> servicesIds = bookingUpdateRequest.getServiceIds();
        String note = bookingUpdateRequest.getNote();
        Booking booking = bookingRepository.findBookingById(bookingId);

        if (booking.isConfirmed()) {
            return false;
        }

        List<ServiceOfClinic> services = new ArrayList<>();
        if (servicesIds != null) {
            services = servicesIds.stream()
                    .map(serviceOfClinicService::findById)
                    .collect(Collectors.toList());
        } else {
            if (!isAllDeleted) {
                services = booking.getServices();
            }
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        if (services.size() != 0) {
            for (ServiceOfClinic service : services) {
                totalPrice = totalPrice.add(service.getPrice());
            }
        }

        int bookingVersion = booking.getVersion() + 1;

        booking.setStatus(Status.Booking.TREATMENT.name());
        booking.setNote(note);
        booking.setServices(services);
        booking.setTotalPrice(totalPrice);
        booking.setVersion(bookingVersion);
        save(booking);
        return true;
    }

    @Override
    public Booking saveBookingFromAppointment(BookingFromAppointmentRequest bookingFromAppointmentRequest, Account account) {
        if (bookingRepository.findBookingByPreBookingId(bookingFromAppointmentRequest.getAppointmentId()) == null) {
            Booking bookingTmp = new Booking();
            //get millisecond
            long millisecond = bookingFromAppointmentRequest.getDesiredCheckingTime();

            //set service to booking
            if (bookingFromAppointmentRequest.getServiceId() != null) {
                int serviceID = bookingFromAppointmentRequest.getServiceId();
                ServiceOfClinic service = serviceOfClinicService.findById(serviceID);
                List<ServiceOfClinic> serviceOfClinicList = List.of(service);
                bookingTmp.setServices(serviceOfClinicList);
            }
            Appointment appointment = appointmentRepository.findAppointmentByStatusInAndId(Status.Appointment.getNames(), bookingFromAppointmentRequest.getAppointmentId());
            if (appointment == null) {
                throw new NotFoundException("Appointment ID " + bookingFromAppointmentRequest.getAppointmentId() + " not found!");
            }
            bookingTmp.setPreBooking(bookingMapper.mapAppointmentToBooking(appointment));
            //set clinic to booking
            Clinic clinic = appointment.getClinic();
            bookingTmp.setClinic(clinic);

            Timestamp desiredCheckingTime = new Timestamp(millisecond);
            Timestamp now = new Timestamp(System.currentTimeMillis());

            if (desiredCheckingTime.compareTo(now) < 0) {
                throw new BadRequestException("Desired checking time invalid");
            }
            bookingTmp.setDesiredCheckingTime(desiredCheckingTime);
            bookingTmp.setCreateBookingTime(now);
            //set patient to booking
            Patient patient = patientService.findById(account.getId());
            bookingTmp.setPatient(patient);
            bookingTmp.setStatus(Status.Booking.PENDING.name());

            if (patient != null && clinic != null) {
                return bookingRepository.save(bookingTmp);
            }
        }
        return null;
    }
}
