package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.PatientService;
import com.teethcare.service.RoleService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Patient.PATIENT_ENDPOINT)
public class PatientController {

    private final PatientService patientService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = accountMapper.mapPatientListToPatientResponseList(patients);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Patient patient = patientService.findById(theID);
        if (patient != null) {
            PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");

    }

    @PostMapping
    public ResponseEntity<PatientResponse> add(@Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(patientRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (patientRegisterRequest.getPassword().equals(patientRegisterRequest.getConfirmPassword())) {
                Patient patient = accountMapper.mapPatientRegisterRequestToPatient(patientRegisterRequest);
                patientService.save(patient);
                PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
                return new ResponseEntity<>(patientResponse, HttpStatus.OK);
            } else
                throw new BadRequestException("confirm Password is not match with password");
        }
        throw new BadRequestException("User existed!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Patient patient = patientService.findById(theID);
        if (patient != null) {
            patientService.delete(theID);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");
    }

}
