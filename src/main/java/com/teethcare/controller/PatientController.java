package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.PatientService;
import com.teethcare.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Patient.PATIENT_ENDPOINT)
public class PatientController {

    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final RoleService roleService;

    @GetMapping
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = accountMapper.mapPatientListToPatientResponseList(patients);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).PATIENT," +
    //        "T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity getPatient(@PathVariable("id") String  id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Patient patient = patientService.findById(theID);
        if (patient != null) {
            PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");

    }

    @PostMapping
    //@PreAuthorize("permitAll()")
    public ResponseEntity addPatient(@Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(patientRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (patientRegisterRequest.getPassword().equals(patientRegisterRequest.getConfirmPassword())) {
                Patient patient = accountMapper.mapPatientRegisterRequestToPatient(patientRegisterRequest);

                patient.setStatus(Status.ACTIVE.name());
                patient.setRole(roleService.getRoleByName(Role.PATIENT.name()));
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));
                patientService.save(patient);

                PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
                return new ResponseEntity<>(patientResponse, HttpStatus.OK);
            } else
                throw new BadRequestException("confirm Password is not match with password");
        }
        throw new BadRequestException("User existed!");
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delPatient(@PathVariable("id") String id) {
        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);
        Patient patient = patientService.findById(theID);
        if(patient != null) {
            patientService.delete(theID);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");
    }

}
