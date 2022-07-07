package com.teethcare.service.impl.booking;

import com.teethcare.common.Constant;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.InternalServerError;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.LocationMapper;
import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.dto.LocationDTO;
import com.teethcare.model.dto.ManagerDTO;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.PatientRepository;
import com.teethcare.repository.ManagerRepository;
import com.teethcare.service.*;
import com.teethcare.utils.LocationUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    private final PatientRepository patientRepository;
    private final AccountService accountService;
    private final ClinicMapper clinicMapper;
    private final LocationService locationService;
    private final FileService fileService;
    private final EmailService emailService;
    private final WardService wardService;
    private final LocationMapper locationMapper;
    private final ManagerRepository managerRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }

    @Override
    public List<Clinic> findAll(Pageable pageable) {
        return clinicRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public Page<ClinicResponse> findNear(Double longitude, Double latitude, String username, Pageable pageable) {
        Patient patient = patientRepository.findPatientByUsername(username);
        Location patientLocation = patient.getLocation();
        if (patientLocation == null) {
            throw new NotFoundException("Your account does not have location .No clinic is found near you.");
        }

        List<Clinic> clinics = clinicRepository.findAll(pageable.getSort());

        if (longitude != null && latitude != null) {
            log.info("input longitude: " + longitude + " latitude: " + latitude);
            clinics = clinics.stream()
                    .filter(clinic -> LocationUtils.distance(latitude, clinic.getLocation().getLatitude(), longitude, clinic.getLocation().getLongitude()) < Constant.LOCATION.DEFAULT_DISTANCE)
                    .collect(Collectors.toList());
        } else {
            log.info("Patient longitude: " + patientLocation.getLongitude() + " latitude: " + patientLocation.getLatitude());
            log.info("Clinic");
            clinics = clinics.stream()
                    .filter(clinic -> LocationUtils.distance(patientLocation.getLatitude(), clinic.getLocation().getLatitude(), patientLocation.getLongitude(), clinic.getLocation().getLongitude()) < Constant.LOCATION.DEFAULT_DISTANCE)
                    .collect(Collectors.toList());
        }
        List<ClinicResponse> clinicResponse = clinicMapper.mapClinicListToClinicResponseList(clinics);
        return PaginationAndSortFactory.convertToPage(clinicResponse, pageable);
    }

    @Override
    public Page<Clinic> findAllWithFilter(ClinicFilterRequest filter, Pageable pageable) {
        List<Clinic> list = clinicRepository.findAll(pageable.getSort());
        list = list.stream().filter(filter.getPredicate()).collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(list, pageable);
    }

    public Clinic getClinicByManager(Manager manager) {
        return clinicRepository.getClinicByManager(manager);
    }

    @Override
    public Clinic findClinicByCustomerServiceId(int csId) {
        return clinicRepository.findClinicByCustomerServicesId(csId);
    }

    @Override
    public Clinic findById(int theId) {
        Optional<Clinic> result = clinicRepository.findById(theId);
        if (result.isEmpty()) {
            throw new NotFoundException("Clinic id " + theId + " not found!");
        }
        return result.get();
    }

    @Override
    @Transactional
    public Clinic create(ClinicDTO clinicDTO, LocationDTO locationDTO, Manager manager) {

        clinicDTO.setBookingGap(30);
        clinicDTO.setExpiredDay(3);

        Location location = locationMapper.mapLocationDTOToLocation(locationDTO);
        location.setWard(wardService.findById(locationDTO.getWardId()));
        locationService.save(location);
        locationService.updateLongitudeAndLatitudeByFullAddress(location);

        Clinic clinic = clinicMapper.mapClinicDTOToClinic(clinicDTO);
        clinic.setLocation(location);
        clinic.setManager(manager);
        save(clinic);
        log.info("Save clinic success!");
        return clinic;
    }

    @Override
    public void save(Clinic clinic) {
        clinic.setStatus(Status.Clinic.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    @Transactional
    public void saveWithManagerAndLocation(Clinic clinic, Manager manager, Location location) {
        clinic.setManager(manager);
        clinic.setLocation(location);
        clinic.setStatus(Status.Clinic.PENDING.name());
        clinicRepository.save(clinic);
    }

    @Override
    public Clinic updateProfile(ClinicRequest clinicRequest, String username) {
        Account manager = accountService.getAccountByUsername(username);
        Clinic clinic = clinicRepository.getClinicByManager(manager);
        clinicMapper.updateClinicFromClinicRequest(clinicRequest, clinic);
        if (clinicRequest.getClinicAddress() != null) {
            Location location = new Location();
            location.setAddressString(clinicRequest.getClinicAddress());
            if (clinicRequest.getWardId() != null) {
                location.setWard(wardService.findById(clinicRequest.getWardId()));
            } else {
                location.setWard(clinic.getLocation().getWard());
            }
            locationService.save(location);
            locationService.updateLongitudeAndLatitudeByFullAddress(location);
            clinic.setLocation(location);
        }
        save(clinic);
        return clinic;
    }

    @Override
    public Clinic updateImage(MultipartFile image, String username) {
        Account manager = accountService.getAccountByUsername(username);
        Clinic clinic = clinicRepository.getClinicByManager(manager);
        clinic.setImageUrl(fileService.uploadFile(image));
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    @Transactional
    public Clinic approve(Clinic clinic) {
        if (!clinic.getStatus().equals(Status.Clinic.PENDING.name())) {
            throw new BadRequestException("This Clinic has been approved/rejected before!");
        }
        clinic.setStatus(Status.Clinic.ACTIVE.name());
        Account manager = clinic.getManager();
        manager.setStatus(Status.Account.ACTIVE.name());
        managerRepository.save((Manager) manager);
        update(clinic);
        try {
            emailService.sendClinicApprovementEmail(clinic);
        } catch (MessagingException e) {
            throw new InternalServerError("Errors occurs when sending mails");
        }
        return clinic;
    }

    @Override
    @Transactional
    public Clinic reject(Clinic clinic) throws MessagingException {
        if (!clinic.getStatus().equals(Status.Clinic.PENDING.name())) {
            throw new BadRequestException("This Clinic has been approved/rejected before!");
        }
        clinic.setStatus(Status.Clinic.INACTIVE.name());
        Account manager = clinic.getManager();
        manager.setStatus(Status.Account.INACTIVE.name());
        managerRepository.save((Manager) manager);
        update(clinic);
        emailService.sendClinicRejectionEmail(clinic);
        return clinic;
    }

    @Override
    public void delete(int id) {
        Optional<Clinic> clinicData = clinicRepository.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.Clinic.INACTIVE.name());
            clinicRepository.save(clinic);
        } else {
            throw new NotFoundException("ID not found");
        }
    }

    @Override
    public void update(Clinic theEntity) {
        clinicRepository.save(theEntity);
    }
}
