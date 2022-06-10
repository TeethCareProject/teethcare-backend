package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.model.response.PatientResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Patient.PATIENT_ENDPOINT)
public class PatientController {

    private final PatientService patientService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll() {
        List<Patient> patients = patientService.findAll();
        List<PatientResponse> patientResponses = accountMapper.mapPatientListToPatientResponseList(patients);
        return new ResponseEntity<>(patientResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable("id") int id) {
        Patient patient = patientService.findById(id);
        if (patient != null) {
            PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
            return new ResponseEntity<>(patientResponse, HttpStatus.OK);
        }
        throw new NotFoundException("Patient id " + id + " not found");

    }

    @PostMapping
    public ResponseEntity<PatientResponse> add(@Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        Patient patient = patientService.addNew(patientRegisterRequest);
        PatientResponse patientResponse = accountMapper.mapPatientToPatientResponse(patient);
        return new ResponseEntity<>(patientResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable("id") int id) {
        Patient patient = patientService.findById(id);
        patientService.delete(id);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

}
