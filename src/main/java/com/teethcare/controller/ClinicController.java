package com.teethcare.controller;

import com.teethcare.config.MapStructMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.entity.Clinic;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {

    private CRUDService<Clinic> clinicService;
    private MapStructMapper mapstructMapper;

    @Autowired
    public ClinicController (@Qualifier("clinicServiceImpl") CRUDService<Clinic> clinicService, MapStructMapper mapstructMapper) {
        this.clinicService = clinicService;
        this.mapstructMapper = mapstructMapper;
    }

    @PutMapping("/{id}")
    public Clinic update(@Valid @RequestBody ClinicDTO clinicDTO, @PathVariable int id) {

        System.out.println(clinicDTO);
        clinicDTO.setId(id);
        Clinic clinic = clinicService.findById(id);
        mapstructMapper.updateClinicFromDTO(clinicDTO, clinic);
        System.out.println(clinic);
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
}
