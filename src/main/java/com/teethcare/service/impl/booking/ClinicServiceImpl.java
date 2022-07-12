package com.teethcare.service.impl.booking;

import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Location;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.repository.ClinicRepository;
import com.teethcare.repository.ManagerRepository;
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {
    private final ClinicRepository clinicRepository;
    private final AccountService accountService;
    private final ManagerRepository managerRepository;
    private final ClinicMapper clinicMapper;
    private final LocationService locationService;
    private final FileService fileService;
    private final EmailService emailService;
    private final WardService wardService;

    @Override
    public List<Clinic> findAll() {
        return clinicRepository.findAll();
    }


    @Override
    public List<Clinic> findAll(Pageable pageable) {
        return clinicRepository.findAllByStatusIsNotNull(pageable);
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
    public void save(Clinic clinic) {
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
        if (clinic.getStartTimeShift1() != null && clinic.getStartTimeShift2() != null
                && clinic.getEndTimeShift1() != null && clinic.getEndTimeShift2() != null) {
            LocalTime endTimeShift1 = clinic.getEndTimeShift1().toLocalTime();
            LocalTime startTimeShift2 = clinic.getStartTimeShift2().toLocalTime();
            if (endTimeShift1.isAfter(startTimeShift2)) {
                throw new BadRequestException(Message.WORKING_TIME_INVALID.name());
            }
        }
        if (clinicRequest.getClinicAddress() != null) {
            Location location = new Location();
            location.setAddressString(clinicRequest.getClinicAddress());
            if (clinicRequest.getWardId() != null) {
                location.setWard(wardService.findById(clinicRequest.getWardId()));
            } else {
                location.setWard(clinic.getLocation().getWard());
            }
            locationService.save(location);
            clinic.setLocation(location);
        }
        clinicRepository.save(clinic);
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
    public Clinic approve(Clinic clinic) throws MessagingException {
        if (!clinic.getStatus().equals(Status.Clinic.PENDING.name())) {
            throw new BadRequestException("This Clinic has been approved/rejected before!");
        }
        clinic.setStatus(Status.Clinic.ACTIVE.name());
        Account manager = clinic.getManager();
        manager.setStatus(Status.Account.ACTIVE.name());
        managerRepository.save((Manager) manager);
        update(clinic);
        emailService.sendClinicApprovementEmail(clinic);
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
    public String findFacebookPageIdByClinicId(String id) {
        try {
            Clinic clinic = findById(Integer.parseInt(id));
            return clinic.getFacebookPageId();
        } catch (NumberFormatException e) {
            throw new NotFoundException("Clinic is not found");
        }
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
