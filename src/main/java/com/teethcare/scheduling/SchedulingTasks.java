package com.teethcare.scheduling;

import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.Appointment;
import com.teethcare.model.entity.Voucher;
import com.teethcare.service.AppointmentService;
import com.teethcare.service.BookingService;
import com.teethcare.service.VoucherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class SchedulingTasks {
    private final VoucherService voucherService;
    private final BookingService bookingService;
    private final AppointmentService appointmentService;

    @Async
    @Transactional
    @Scheduled(fixedDelay = 1_000, initialDelay = 1_000)
    public void checkExpiredVoucher() {
        long now = System.currentTimeMillis() / 1_000;
        List<Voucher> vouchers = voucherService.findAllVouchersByExpiredTime(now * 1_000);
        if (vouchers.size() > 0) {
            vouchers.forEach(voucherService::disable);
            log.info("voucher status updated!");
        }
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 1_000 * 60, initialDelay = 1_000)
    public void checkExpiredAppointment() {
        long now = System.currentTimeMillis() / (1_000 * 60);
        List<Appointment> appointments = appointmentService.findAllByExpiredDate(now * 1_000 * 60);
        if (appointments.size() > 0) {
            appointments.forEach(appointmentService::disableAppointment);
            log.info("appointment status updated!");
        }
    }

    @Async
    @Transactional
    @Scheduled(fixedDelay = 1_000 * 60, initialDelay = 1_000)
    public void checkExpiredBooking() {
        long now = System.currentTimeMillis() / 1_000;
        List<Booking> bookings = bookingService.findAllBookingByExpiredTime();
        if (bookings.size() > 0) {
            bookings.forEach(bookingService::expired);
        }
    }
}
