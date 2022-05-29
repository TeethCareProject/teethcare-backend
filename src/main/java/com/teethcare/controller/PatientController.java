package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.exception.IdInvalidException;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.exception.RegisterAccountException;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.PatientService;
import com.teethcare.service.RoleService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
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

    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<PatientResponse>> getAll() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = accountMapper.mapPatientListToPatientResponseList(patients);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN, T(com.teethcare.common.Role).PATIENT," +
            "T(com.teethcare.common.Role).DENTIST, T(com.teethcare.common.Role).CUSTOMER_SERVICE)")
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
    @PreAuthorize("permitAll()")
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
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<String> delete(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Patient patient = patientService.findById(theID);
        if (patient != null) {
            patientService.delete(theID);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
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
