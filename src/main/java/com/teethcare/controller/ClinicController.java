package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.mapper.ClinicMapper;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.exception.ClinicNotFoundException;
import com.teethcare.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;


    @GetMapping
    @PreAuthorize("hasAnyAuthority((T(com.teethcare.common.Role).ADMIN), T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<List<ClinicResponse>> getAllActiveClinics() {
        List<Clinic> list = clinicService.findAllActive();
        List<ClinicResponse> clinicResponses = clinicMapper.mapClinicListToClinicResponseList(list);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicTmp = clinicService.findById(id);
        if (clinicTmp.isPresent()) {
            ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinicTmp.get());
            return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity delClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicData = clinicService.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity( HttpStatus.OK);
        } else {
            throw new ClinicNotFoundException("Clinic id " + id + " not found");
        }
    }

}
