package com.teethcare.service.impl.booking;

import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.BookingResponse;
import com.teethcare.repository.BookingRepository;
import com.teethcare.repository.PatientRepository;
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

    private final PatientRepository patientRepository;
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
    public Page<Booking> findBookingByDentistId(int id, Pageable pageable) {
        return bookingRepository.findBookingByPatientId(id, pageable);
    }

    @Override
    public List<Booking> findBookingByStatusNotLike(String status) {
        return null;
    }

    @Override
    public Page<Booking> findAll(Specification<Booking> bookingSpecification, Pageable pageable) {
        return bookingRepository.findAll(bookingSpecification, pageable);
    }

    @Override
    public Page<Booking> findBookingByPatientId(int id, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Booking> findBookingByPatientIdAndDentistClinicNameLike(int patientId, String clinicName, Pageable pageable) {
        return bookingRepository.findBookingByPatientIdAndDentistClinicNameLike(patientId, clinicName, pageable);
    }

    @Override
    public Page<Booking> findAll(String role, int id, String clinicName, Specification<Booking> bookingSpecification, Pageable pageable) {
        Page<Booking> bookingList = null;
        switch (role) {
            case "CUSTOMER_SERVICE":
                bookingList = bookingRepository.findAll(bookingSpecification, pageable);
                break;
            case "PATIENT":
                if (clinicName == null || clinicName.isBlank()) {
                    bookingList = bookingRepository.findBookingByPatientId(id, pageable);
                } else {
                    bookingList = bookingRepository.findBookingByPatientIdAndDentistClinicNameLike(
                            id, "%" + clinicName + "%", pageable);
                }
                break;
            case "DENTIST":
//                bookingList = bookingRepository.findBookingByDentistId(id, pageable);
                break;
        }
        return bookingList;
    }

//    @Override
//    public Page<Booking> findBookingByPatientAndDentist(int patientId, String patientName, int dentistId) {
////        Patient patient = patientRepository.
//        return null;
//    }

    @Override
    public List<Booking> findAllByCustomerService(CustomerService customerService) {
        return bookingRepository.findAllByCustomerService(customerService);
    }

    @Override
    public Booking findBookingById(int id) {
        Booking booking = bookingRepository.findBookingById(id);
        if (booking == null) {
            throw new NotFoundException();
        } else {
            return bookingRepository.findBookingById(id);
        }
    }
}
