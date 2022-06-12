package com.teethcare.service.impl.booking;

import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.AppointmentFilterRequest;
import com.teethcare.model.request.AppointmentRequest;
import com.teethcare.repository.AppointmentRepository;
import com.teethcare.repository.ServiceRepository;
import com.teethcare.service.*;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
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
    private final ManagerService managerService;
    private final ClinicService clinicService;
    private final DentistService dentistService;
    private final CSService csService;

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
        appointment.setStatus(Status.Appointment.ACTIVE.name());
        save(appointment);
        return appointment;
    }

    @Override
    public Appointment findAppointmentById(int id) {
        return appointmentRepository.findByStatusInAndId(Status.Appointment.getNames(), id);
    }

    @Override
    public Page<Appointment> findAllWithFilter(String jwtToken, Pageable pageable, AppointmentFilterRequest appointmentFilterRequest) {
        String username = jwtTokenUtil.getUsernameFromJwt(jwtToken);
        Account account = accountService.getAccountByUsername(username);
        List<Appointment> appointments;
        switch (Role.valueOf(account.getRole().getName())) {
            case MANAGER:
                Manager manager = managerService.findById(account.getId());
                Clinic clinic = clinicService.getClinicByManager(manager);
                appointments = appointmentRepository.findAllByStatusInAndClinicId(Status.Appointment.getNames(), clinic.getId(), pageable.getSort());
                appointments.stream().filter(appointmentFilterRequest.getPredicate()).collect(Collectors.toList());
                break;

            case CUSTOMER_SERVICE:
                CustomerService customerService = csService.findById(account.getId());
                appointments = appointmentRepository.findAllByStatusInAndClinicId(Status.Appointment.getNames(), customerService.getClinic().getId(), pageable.getSort());
                appointments.stream().filter(appointmentFilterRequest.getPredicate()).collect(Collectors.toList());
                break;

            case DENTIST:
                Dentist dentist = dentistService.findById(account.getId());
                appointments = appointmentRepository.findAllByStatusInAndClinicId(Status.Appointment.getNames(), dentist.getClinic().getId(), pageable.getSort());
                appointments.stream().filter(appointmentFilterRequest.getPredicate()).collect(Collectors.toList());
                break;

            case PATIENT:
                appointments = appointmentRepository.findAllByStatusInAndPatientId(Status.Appointment.getNames(), account.getId(), pageable.getSort());
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
