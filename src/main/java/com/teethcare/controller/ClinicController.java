package com.teethcare.controller;

import com.teethcare.common.Message;
import com.teethcare.config.MapStructMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CRUDService;
import com.teethcare.service.CSService;
import com.teethcare.service.DentistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.teethcare.common.Message.SUCCESS_FUNCTION;

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
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody ClinicRequest clinicRequest, @PathVariable int id) {

        clinicRequest.setId(id);
        Clinic clinic = clinicService.findById(id);
        mapstructMapper.updateClinicFromDTO(clinicRequest, clinic);
        clinicService.save(clinic);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
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
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable int id) {
        List<Account> staffList = new ArrayList<>();
        List<AccountResponse> staffResponseList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(id, "ACTIVE");
        List<CustomerService> customerServiceList = CSService.findByClinicIdAndStatus(id, "ACTIVE");

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        staffResponseList = mapstructMapper.mapAccountListToAccountDTOList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }
}
