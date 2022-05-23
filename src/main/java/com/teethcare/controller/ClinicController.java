package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@EnableSwagger2
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
public class ClinicController {

    private ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority((T(com.teethcare.common.Role).ADMIN), T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<List<ClinicResponse>> getAllActiveClinics() {
        List<Clinic> list = clinicService.findAllActive();
        List<ClinicResponse> clinicResponses = new ArrayList<>();
        for (Clinic clinicTmp : list) {
            AccountResponse accountResponse = new AccountResponse(
                    clinicTmp.getManager().getId(),
                    clinicTmp.getManager().getUsername(),
                    clinicTmp.getManager().getRole().getName(),
                    clinicTmp.getManager().getFirstName(),
                    clinicTmp.getManager().getLastName(),
                    clinicTmp.getManager().getAvatarImage(),
                    clinicTmp.getManager().getDateOfBirth(),
                    clinicTmp.getManager().getEmail(),
                    clinicTmp.getManager().getPhone(),
                    clinicTmp.getManager().getGender(),
                    clinicTmp.getManager().getStatus()
            );
            clinicResponses.add(new ClinicResponse(
                    clinicTmp.getId(),
                    accountResponse,
                    clinicTmp.getLocation(),
                    clinicTmp.getName(),
                    clinicTmp.getDescription(),
                    clinicTmp.getImageUrl(),
                    clinicTmp.getTaxCode(),
                    clinicTmp.getAvgRatingScore(),
                    clinicTmp.getStatus()

            ));
        }
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicTmp = clinicService.findById(id);
        AccountResponse accountResponse = new AccountResponse(
                clinicTmp.get().getManager().getId(),
                clinicTmp.get().getManager().getUsername(),
                clinicTmp.get().getManager().getRole().getName(),
                clinicTmp.get().getManager().getFirstName(),
                clinicTmp.get().getManager().getLastName(),
                clinicTmp.get().getManager().getAvatarImage(),
                clinicTmp.get().getManager().getDateOfBirth(),
                clinicTmp.get().getManager().getEmail(),
                clinicTmp.get().getManager().getPhone(),
                clinicTmp.get().getManager().getGender(),
                clinicTmp.get().getManager().getStatus()
        );
        if (clinicTmp.isPresent()) {
            ClinicResponse clinicResponse = new ClinicResponse(
                    clinicTmp.get().getId(),
                    accountResponse,
                    clinicTmp.get().getLocation(),
                    clinicTmp.get().getName(),
                    clinicTmp.get().getDescription(),
                    clinicTmp.get().getImageUrl(),
                    clinicTmp.get().getTaxCode(),
                    clinicTmp.get().getAvgRatingScore(),
                    clinicTmp.get().getStatus()

            );
            return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<Clinic> delClinic(@PathVariable("id") int id) {
        Optional<Clinic> clinicData = clinicService.findById(id);
        if (clinicData.isPresent()) {
            Clinic clinic = clinicData.get();
            clinic.setStatus(Status.INACTIVE.name());
            return new ResponseEntity<>(clinicService.save(clinic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
