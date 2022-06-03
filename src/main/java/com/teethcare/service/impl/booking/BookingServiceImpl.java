package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.BookingFilterRequest;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.PatientRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import com.teethcare.utils.PaginationAndSortFactory;
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

        switch (Role.valueOf(role)) {
            case CUSTOMER_SERVICE:
                List<Booking> bookingListForCustomerService = bookingRepository.findBookingByStatusNotLike(Status.Booking.UNAVAILABLE.name());

                bookingListForCustomerService = bookingListForCustomerService.stream()
                        .filter(filterRequest.getPredicate())
                        .collect(Collectors.toList());
                for(Booking booking: bookingListForCustomerService) {
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
}
