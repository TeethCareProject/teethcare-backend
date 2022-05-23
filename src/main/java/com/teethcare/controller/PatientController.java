package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.exception.RegisterAccountException;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.Role;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final CRUDService<Patient> patientService;
    private final AccountService accountService;

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        TypeToken<List<PatientResponse>> typeToken = new TypeToken<>() {
        };
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = mapper.map(patients, typeToken.getType());
        patientResponses.forEach(patientResponse -> patientResponse.setRole(com.teethcare.common.Role.PATIENT.name()));
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).PATIENT," +
            "T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
    public ResponseEntity getPatient(@PathVariable("id") Integer id) {
        Optional<Patient> patient = patientService.findById(id);
        if (patient.isPresent()) {
            PatientResponse patientResponse = mapper.map(patientService.findById(id).get(), PatientResponse.class);
            patientResponse.setRole(com.teethcare.common.Role.PATIENT.name());
            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        }
        throw new IdNotFoundException("Patient id " + id + " not found");

    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity addPatient(@Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        boolean isDuplicated = accountService.isDuplicated(patientRegisterRequest.getUsername(), Status.INACTIVE.name());
        if (!isDuplicated) {
            if (patientRegisterRequest.getPassword().equals(patientRegisterRequest.getConfirmPassword())) {
                Patient patient = mapper.map(patientRegisterRequest, Patient.class);
                patient.setRole(new Role(3, com.teethcare.common.Role.PATIENT.name()));
                patient.setStatus(Status.ACTIVE.name());
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));
                Patient addedPatient = patientService.save(patient);
                PatientResponse patientResponse = mapper.map(addedPatient, PatientResponse.class);
                patientResponse.setRole(com.teethcare.common.Role.PATIENT.name());
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
