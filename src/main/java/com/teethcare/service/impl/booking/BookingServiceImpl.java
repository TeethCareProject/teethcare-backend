package com.teethcare.service.impl.booking;

import com.teethcare.common.Message;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingFromAppointmentRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.model.request.BookingUpdateRequest;
import com.teethcare.repository.AppointmentRepository;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceRepository serviceRepository;
    private final PatientService patientService;
    private final DentistService dentistService;
    private final ClinicService clinicService;
    private final AppointmentRepository appointmentRepository;

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

        long millisecond = bookingRequest.getDesiredCheckingTime();
        Timestamp desiredCheckingTime = ConvertUtils.getTimestamp(millisecond);
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
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByClinic(clinic, sort);

                bookingListForCustomerService = bookingListForCustomerService.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());
                return PaginationAndSortFactory.convertToPage(bookingListForCustomerService, pageable);
            case PATIENT:
                List<Booking> bookingListForPatient = bookingRepository.findBookingByPatientId(accountId, sort);

                bookingListForPatient = bookingListForPatient.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

              return PaginationAndSortFactory.convertToPage(bookingListForPatient, pageable);
            case DENTIST:
                List<Booking> bookingListForDentist = bookingRepository.findBookingByDentistId(accountId, sort);

                bookingListForDentist = bookingListForDentist.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

                return PaginationAndSortFactory.convertToPage(bookingListForDentist, pageable);
        }
        return null;
    }

    @Override
    @Transactional
    public void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService) {
        Booking booking = findBookingById(bookingId);

        if (isAccepted) {
            booking.setStatus(Status.Booking.REQUEST.name());
        } else {
            booking.setStatus(Status.Booking.REJECTED.name());
        }
        booking.setCustomerService(customerService);

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
    public boolean updateStatus(int bookingId) {
        Booking booking = bookingRepository.findBookingById(bookingId);
        String status = booking.getStatus();
        switch (Status.Booking.valueOf(status)) {
            case REQUEST:
                if (booking.getExaminationTime() == null || booking.getDentist() == null
                        || booking.getCustomerService() == null || booking.getServices() == null) {
                    return false;
                }
                booking.setStatus(Status.Booking.TREATMENT.name());
                break;
            case TREATMENT:
                if (booking.getExaminationTime() == null || booking.getDentist() == null
                        || booking.getCustomerService() == null || booking.getServices() == null || booking.getTotalPrice() == null
                        || !booking.isConfirmed()) {
                    return false;
                }
                booking.setStatus(Status.Booking.DONE.name());
                break;
            default:
                return false;
        }
        bookingRepository.save(booking);
        return true;
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
            for (Integer servicesId : servicesIds) {
                services.add(serviceOfClinicService.findById(servicesId));
            }
        } else {
            if (!isAllDeleted) {
                services = booking.getServices();
            }
        }

        Timestamp examinationTime = ConvertUtils.getTimestamp(examinationTimeRequest);
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
            for (Integer servicesId : servicesIds) {
                services.add(serviceOfClinicService.findById(servicesId));
            }
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
                List<ServiceOfClinic> serviceOfClinicList = new ArrayList<>();
                serviceOfClinicList.add(service);
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

            Timestamp desiredCheckingTime = ConvertUtils.getTimestamp(millisecond);
            Timestamp now = new Timestamp(System.currentTimeMillis());
//        Time startTimeShift1 = clinic.getS
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
    }}
