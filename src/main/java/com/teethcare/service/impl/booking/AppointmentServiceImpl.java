package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Appointment;
import com.teethcare.model.entity.Booking;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.request.AppointmentFilterRequest;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.repository.AppointmentRepository;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.AppointmentService;
import com.teethcare.service.BookingService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final BookingService bookingService;
    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;
    private final AccountService accountService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment();

        Timestamp now = new Timestamp(System.currentTimeMillis());
        appointment.setCreateBookingDate(now);
        if (appointmentRequest.getAppointmentDate() - now.getTime() < 0) {
            throw new BadRequestException("Appointment Date invalid");
        }
        appointment.setAppointmentDate(ConvertUtils.getTimestamp(appointmentRequest.getAppointmentDate()));
        if (appointmentRequest.getExpirationAppointmentDate().compareTo(appointmentRequest.getAppointmentDate()) < 0) {
            throw new BadRequestException("Expiration Appointment Date invalid");
        }
        appointment.setExpireAppointmentDate(ConvertUtils.getTimestamp(appointmentRequest.getExpirationAppointmentDate()));

        Booking preBooking = bookingService.findBookingById(appointmentRequest.getPreBookingId());
        appointment.setPreBooking(preBooking);

        ServiceOfClinic service = serviceRepository.findByIdAndStatus(appointmentRequest.getServiceId(), Status.Service.ACTIVE.name());
        List<ServiceOfClinic> services = new ArrayList<>();
        services.add(service);
        appointment.setServices(services);

        appointment.setPatient(preBooking.getPatient());

        appointment.setNote(appointment.getNote());

        appointment.setTotalPrice(service.getPrice());

        appointment.setClinic(preBooking.getClinic());
        appointment.setStatus(Status.Booking.APPOINTMENT.name());
        save(appointment);
        return appointment;
    }

    @Override
    public Page<Appointment> getAllWithFilter(String jwtToken, Pageable pageable, AppointmentFilterRequest appointmentFilterRequest) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        List<Appointment> appointments;
        switch (Role.valueOf(account.getRole().getName())) {
            case ADMIN:
                appointments = appointmentRepository.findAllByStatusIsNotNullAndExaminationTimeIsNull(pageable.getSort());
                appointments.stream().filter(appointmentFilterRequest.getPredicate()).collect(Collectors.toList());
                break;
            case PATIENT:
                appointments = appointmentRepository.findAllByStatusIsNotNullAndPatientIdAndExaminationTimeIsNull(account.getId(), pageable.getSort());
                appointments.stream().filter(appointmentFilterRequest.getPredicate()).collect(Collectors.toList());
                break;
            default:
                return null;
        }
        return PaginationAndSortFactory.convertToPage(appointments, pageable);
    }

    @Override
    public List<Appointment> findAll() {
        return null;
    }

    @Override
    public Appointment findById(int id) {
        return null;
    }

    @Override
    public void save(Appointment theEntity) {
        appointmentRepository.save(theEntity);
    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(Appointment theEntity) {

    }
}
