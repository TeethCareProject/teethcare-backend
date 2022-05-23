package com.teethcare.controller;

import com.teethcare.config.MapStructMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.dto.ClinicPreDTO;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.service.CRUDService;
import com.teethcare.service.CSService;
import com.teethcare.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/clinics")
public class ClinicController {

    private CRUDService<Clinic> clinicService;
    private DentistService dentistService;
    private CSService CSService;
    private MapStructMapper mapstructMapper;

    @Autowired
    public ClinicController (@Qualifier("clinicServiceImpl") CRUDService<Clinic> clinicService,
                             DentistService dentistService,
                             CSService CSService,
                             MapStructMapper mapstructMapper) {
        this.dentistService = dentistService;
        this.CSService = CSService;
        this.clinicService = clinicService;
        this.mapstructMapper = mapstructMapper;
    }

    @PutMapping("/{id}")
    public Clinic update(@Valid @RequestBody ClinicPreDTO clinicPreDTO, @PathVariable int id) {

        clinicPreDTO.setId(id);
        Clinic clinic = clinicService.findById(id);
        mapstructMapper.updateClinicFromDTO(clinicPreDTO, clinic);
        clinicService.save(clinic);
        return clinic;
    }

    @GetMapping("/{id}")
    public Clinic getClinicById(@PathVariable int id) {
        Clinic clinic = clinicService.findById(id);

        if (clinic == null) {
            throw new NotFoundException();
        }

        return clinic;
    }

    @GetMapping("/{id}/staffs")
    public List<AccountResponse> findAllStaffs(@PathVariable int id) {
        List<Account> staffList = new ArrayList<>();
        List<AccountResponse> staffResponseList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(id, "ACTIVE");
        List<CustomerService> customerServiceList = CSService.findByClinicIdAndStatus(id, "ACTIVE");

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        staffResponseList = mapstructMapper.mapAccountListToAccountDTOList(staffList);

        return staffResponseList;
    }
}
