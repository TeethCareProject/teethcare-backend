package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.repository.BookingRepository;
import com.teethcare.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

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
        booking.setStatus(Status.INACTIVE.name());
        save(booking);
    }

    @Override
    public List<Booking> findBookingByPatientId(int theId) {
        return bookingRepository.findBookingByPatientId(theId);
    }

    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    @Override
    public List<Booking> findBookingByPatientIdAndStatus(int theId, String status) {
        return bookingRepository.findBookingByPatientIdAndStatus(theId, status);
    }

    @Override
    public List<Booking> findBookingByDentistId(int id, Pageable pageable) {
        return null;
    }

    @Override
    public List<Booking> findBookingByStatusNotLike(String status) {
        return null;
    }

    @Override
    public List<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Booking> findBookingByPatientId(int id, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Booking> findBookingByDentistClinicNameAndPatientId(String clinicName, String patientId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Booking> findAll(String role, int id, String clinicName, Specification<Booking> bookingSpecification, Pageable pageable) {
        Page<Booking> bookingList = null;
        switch (role) {
            case "CUSTOMER_SERVICE":
//                bookingList = findAll(bookingSpecification, pageable);
                break;
            case "PATIENT":
                if (clinicName == null || clinicName.isBlank()) {
                    bookingList = bookingRepository.findBookingByPatientId(id, pageable);
                } else {
                    bookingList = bookingRepository.findBookingByDentistClinicNameAndPatientId(clinicName, id, pageable);
                }

                break;
            case "DENTIST":
//                bookingList = bookingRepository.findBookingByDentistId(id, pageable);
                break;
        }
        return bookingList;
    }

    @Override
    public List<Booking> findAllByCustomerService(CustomerService customerService) {
        return bookingRepository.findAllByCustomerService(customerService);
    }
}
