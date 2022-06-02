package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.PatientRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final PatientRepository patientRepository;
    private final BookingRepository bookingRepository;
    private final ClinicRepository clinicRepository;

    private final ClinicService clinicService;

    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking findById(int id) {
        Booking booking = bookingRepository.getById(id);
        return booking;
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
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
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

        switch (role) {
            case "CUSTOMER_SERVICE":
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByStatusNotLike(Status.Booking.UNAVAILABLE.name(), pageable);

                if (filterRequest.getBookingId() != null) {
                    Predicate<Booking> byBookingId = booking -> Integer.toString(booking.getId()).contains(Integer.toString(filterRequest.getBookingId()));
                    bookingListForCustomerService = bookingListForCustomerService.stream()
                            .filter(byBookingId)
                            .collect(Collectors.toList());
                }

                if (filterRequest.getCustomerServiceId() != null) {
                    Predicate<Booking> rejectNullCustomerService = booking -> booking.getCustomerService() != null;
                    Predicate<Booking> byCSId = booking -> booking.getCustomerService().getId() == filterRequest.getCustomerServiceId();
                    bookingListForCustomerService = bookingListForCustomerService.stream()
                            .filter(rejectNullCustomerService)
                            .filter(byCSId)
                            .collect(Collectors.toList());
                }

                if (filterRequest.getDentistId() != null) {
                    Predicate<Booking> rejectNullDentist = booking -> booking.getDentist() != null;
                    Predicate<Booking> byDentistId = booking -> booking.getDentist().getId() == filterRequest.getDentistId();
                    bookingListForCustomerService = bookingListForCustomerService.stream()
                            .filter(rejectNullDentist)
                            .filter(byDentistId)
                            .collect(Collectors.toList());
                }

                if (filterRequest.getPatientName() != null) {
                    Predicate<Booking> byPatientName = booking -> ((booking.getPatient().getFirstName() + booking.getPatient().getLastName()).contains(filterRequest.getPatientName()));
                    bookingListForCustomerService = bookingListForCustomerService.stream()
                            .filter(byPatientName)
                            .collect(Collectors.toList());
                }

                if (filterRequest.getPatientPhone() != null) {
                    Predicate<Booking> byPatientPhone = booking -> ((booking.getPatient().getPhone()).contains(filterRequest.getPatientPhone()));
                    bookingListForCustomerService = bookingListForCustomerService.stream()
                            .filter(byPatientPhone)
                            .collect(Collectors.toList());
                }

                return new PageImpl<>(bookingListForCustomerService);
            case "PATIENT":
                List<Booking> bookingListForPatient = bookingRepository.findBookingByPatientId(accountId, pageable);

                if (filterRequest.getClinicName() != null) {
                    Predicate<Booking> byClinicName = booking -> (booking.getClinic().getName().contains(filterRequest.getClinicName()));
                    bookingListForPatient = bookingListForPatient.stream()
                            .filter(byClinicName)
                            .collect(Collectors.toList());
                }

                if (filterRequest.getBookingId() != null) {
                    Predicate<Booking> byBookingId = booking -> Integer.toString(booking.getId()).contains(Integer.toString(filterRequest.getBookingId()));
                    bookingListForPatient = bookingListForPatient.stream()
                            .filter(byBookingId)
                            .collect(Collectors.toList());
                }
              return new PageImpl<>(bookingListForPatient);
            case "DENTIST":
//                bookingPage = bookingRepository.findBookingByDentistId(id, pageable);
                break;
        }

        return bookingPage;
    }

    @Override
    @Transactional
    public void confirmBookingRequest(int bookingId, boolean isAccepted, CustomerService customerService) {
        Booking booking = findBookingById(bookingId);

        booking.setId(bookingId);
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
        Booking booking = null;

        booking = bookingRepository.findBookingById(id);

        if (booking == null) {
            throw new NotFoundException();
        } else {
            return bookingRepository.findBookingById(id);
        }
    }
}
