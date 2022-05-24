package com.teethcare.config.model.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Role;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.config.model.entity.Patient;
import com.teethcare.config.model.request.PatientRegisterRequest;
import com.teethcare.config.model.response.CustomErrorResponse;
import com.teethcare.config.model.response.PatientResponse;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.exception.RegisterAccountException;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import com.teethcare.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Patient.PATIENT_ENDPOINT)
public class PatientController {


    private final PasswordEncoder passwordEncoder;
    private final CRUDService<Patient> patientService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = accountMapper.mapPatientListToPatientResponseList(patients);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).PATIENT," +
            "T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity getPatient(@PathVariable("id") Integer id) {
        Optional<Patient> patient = patientService.findById(id);
        if (patient.isPresent()) {
            PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient.get());
            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        }
        throw new IdNotFoundException("Patient id " + id + " not found");

    }

    @PostMapping
    @PreAuthorize("permitAll()")
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
                throw new RegisterAccountException("confirm Password is not match with password");
        }
        throw new RegisterAccountException("User existed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<Patient> delPatient(@PathVariable("id") Integer id) {
        Patient deletedPatient = patientService.delete(id);
        if (deletedPatient != null) {
            return new ResponseEntity<>(deletedPatient, HttpStatus.OK);
        }
        throw new IdNotFoundException("Patient id " + id + " not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List errors = new ArrayList<String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + " " + errorMessage);
        });
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors

        );
        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }
}
