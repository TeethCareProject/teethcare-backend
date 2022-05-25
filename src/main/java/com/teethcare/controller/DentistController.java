package com.teethcare.controller;

import com.teethcare.common.Message;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@EnableSwagger2
@PreAuthorize("hasAuthority('MANAGER')")
@RequestMapping("/api/dentists")
public class DentistController {
    private CRUDService<Dentist> dentistService;
    private AccountMapper accountMapper;

    @Autowired
    public DentistController (@Qualifier("dentistServiceImpl") CRUDService<Dentist> dentistService,
                              AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.dentistService = dentistService;
    }

    @GetMapping()
    public List<Dentist> findAll() {
        return dentistService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> getDentist(@PathVariable int id) {

        Dentist theDentist = dentistService.findById(id);

        if (theDentist == null) {
            throw new NotFoundException();
        }

        DentistResponse dentistResponse = new DentistResponse();
        dentistResponse = accountMapper.mapDentistToDentistResponse(theDentist);
        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateAccountStatus(@PathVariable("id") int id) {
        if (id < 1) {
            throw new NotFoundException();
        }

        Dentist dentist = dentistService.findById(id);

        if (dentist == null) {
            throw new NotFoundException();
        }

        dentist.setId(id);
        dentist.setStatus("INACTIVE");

        dentistService.save(dentist);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
