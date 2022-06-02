package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.*;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.PatientRepository;
import com.teethcare.service.BookingService;
import com.teethcare.service.ClinicService;
import com.teethcare.specification.builder.BookingBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
                                 String clinicName,
                                 int bookingId,
                                 Specification<Booking> bookingSpecification,
                                 Pageable pageable) {

        Page<Booking> bookingPage = null;

        switch (role) {
            case "CUSTOMER_SERVICE":
                bookingPage = bookingRepository.findAll(bookingSpecification, pageable);
                break;
            case "PATIENT":
                if ((clinicName == null || clinicName.isBlank()) && (bookingId == -1)) {
                    bookingPage = bookingRepository.findBookingByPatientId(accountId, pageable);
                } else {
                    if (bookingId == -1) {
                        bookingPage =
                                bookingRepository.findBookingByPatientIdAndClinicNameLike(
                                        accountId, "%" + clinicName + "%", pageable);
                    } else {
                        if (clinicName == null || clinicName.isBlank()) {
                            bookingPage = bookingRepository.findBookingByIdAndPatientId(bookingId, accountId, pageable);
                        } else {
                            bookingPage = bookingRepository.findBookingByIdAndPatientIdAndDentistClinicNameLike(
                                    bookingId, accountId, "%" + clinicName + "%", pageable);
                        }
                    }
                }
                break;
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
