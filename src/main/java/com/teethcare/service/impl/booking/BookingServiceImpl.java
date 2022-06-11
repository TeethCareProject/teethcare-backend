package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.mapper.BookingMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.model.request.BookingRequest;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.PatientService;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceRepository serviceRepository;
    private final PatientService patientService;


    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.getById(id);
    }

    @Override
    public void save(Booking theEntity) {
        bookingRepository.save(theEntity);
    }

    @Override
    public void delete(int theId) {
        Booking booking = findById(theId);
        booking.setStatus(Status.Booking.UNAVAILABLE.name());
        save(booking);
    }

    @Override
    public void update(Booking theEntity) {

    }

    @Override
    public Booking saveBooking(BookingRequest bookingRequest, Account account) {
        Booking bookingTmp = bookingMapper.mapBookingRequestToBooking(bookingRequest);
        //get millisecond
        long millisecond = bookingRequest.getDesiredCheckingTime();

        Timestamp desiredCheckingTime = ConvertUtils.getTimestamp(millisecond);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (desiredCheckingTime.compareTo(now) < 0) {
            throw new BadRequestException("Desired checking time invalid");
        }
        bookingTmp.setDesiredCheckingTime(desiredCheckingTime);

        //set service to booking
        int serviceID = bookingRequest.getServiceId();
        ServiceOfClinic service = serviceOfClinicService.findById(serviceID);
        List<ServiceOfClinic> serviceOfClinicList = new ArrayList<>();
        serviceOfClinicList.add(service);
        bookingTmp.setServices(serviceOfClinicList);

        //set clinic to booking
        Clinic clinic = service.getClinic();
        bookingTmp.setClinic(clinic);

        //set patient to booking
        Patient patient = patientService.findById(account.getId());
        bookingTmp.setPatient(patient);
        bookingTmp.setStatus(Status.Booking.PENDING.name());

        if (patient != null && !serviceOfClinicList.isEmpty() && clinic != null) {
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

        Page<Booking> bookingPage = null;

        switch (Role.valueOf(role)) {
            case CUSTOMER_SERVICE:
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByStatusNotLike(Status.Booking.UNAVAILABLE.name());

                bookingListForCustomerService = bookingListForCustomerService.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());
                for (Booking booking : bookingListForCustomerService) {
                    System.out.println(booking.getId());
                }

                return PaginationAndSortFactory.convertToPage(bookingListForCustomerService, pageable);
            case PATIENT:
                List<Booking> bookingListForPatient = bookingRepository.findBookingByPatientId(accountId);

                bookingListForPatient = bookingListForPatient.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());

                return PaginationAndSortFactory.convertToPage(bookingListForPatient, pageable);
            case DENTIST:
                bookingPage = bookingRepository.findBookingByDentistId(accountId, pageable);
                break;
        }

        return bookingPage;
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
    public List<Booking> findAllByCustomerService(CustomerService customerService) {
        return bookingRepository.findAllByCustomerService(customerService);
    }

    @Override
    public Booking findBookingById(int id) {
        return bookingRepository.findBookingById(id);
    }

//  ### APPOINTMENT SECTION ###
    @Override
    public Booking createAppointment(AppointmentRequest appointmentRequest, Account customerService) {
        Booking bookingTmp = new Booking();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        bookingTmp.setCreateBookingDate(now);
        if (appointmentRequest.getAppointmentDate() - now.getTime() < 0) {
            throw new BadRequestException("Appointment Date invalid");
        }
        bookingTmp.setAppointmentDate(ConvertUtils.getTimestamp(appointmentRequest.getAppointmentDate()));
        if (appointmentRequest.getExpirationAppointmentDate().compareTo(appointmentRequest.getAppointmentDate()) < 0) {
            throw new BadRequestException("Expiration Appointment Date invalid");
        }
        bookingTmp.setExpireAppointmentDate(ConvertUtils.getTimestamp(appointmentRequest.getExpirationAppointmentDate()));

        Booking preBooking = findBookingById(appointmentRequest.getPreBookingId());
        bookingTmp.setPreBooking(preBooking);

        ServiceOfClinic service = serviceRepository.findByIdAndStatus(appointmentRequest.getServiceId(), Status.Service.ACTIVE.name());
        List<ServiceOfClinic> services = new ArrayList<>();
        services.add(service);
        bookingTmp.setServices(services);

        bookingTmp.setCustomerService((CustomerService) customerService);

        bookingTmp.setDescription(appointmentRequest.getDescription());

        bookingTmp.setPatient(preBooking.getPatient());

        //set total price as first service price (appointment has only 1 service)
        bookingTmp.setTotalPrice(bookingTmp.getServices().get(0).getPrice());

        bookingTmp.setClinic(preBooking.getClinic());

        bookingTmp.setStatus(Status.Booking.APPOINTMENT.name());

        bookingRepository.save(bookingTmp);
        return bookingTmp;
    }

    public List<Booking> getAppointmentChainByBookingId(int bookingId) {
        List<Booking> bookings = new ArrayList<>();
        Booking booking = bookingRepository.findBookingById(bookingId);
        bookings.add(booking);
        for (Booking b = booking.getMappedPreBooking(); b != null; b = b.getMappedPreBooking()) {
            bookings.add(b);
        }
        return bookings;
    }
}
