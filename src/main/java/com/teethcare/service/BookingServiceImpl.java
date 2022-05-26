package com.teethcare.service;

import com.teethcare.model.entity.Booking;
import com.teethcare.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> findAll() {
        return null;
    }

    @Override
    public Booking findById(int id) {
        return null;
    }

    @Override
    public void save(Booking theEntity) {

    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public List<Booking> findBookingByPatientId(int theId) {
        return bookingRepository.findBookingByPatientId(theId);
    }
}
