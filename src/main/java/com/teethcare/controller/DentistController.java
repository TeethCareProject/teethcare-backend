package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.DentistService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Dentist.DENTIST_ENDPOINT)
public class DentistController {
    private final DentistService dentistService;
    private final AccountMapper accountMapper;

    @GetMapping()
    public List<Dentist> findAll() {
        return dentistService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> getDentist(@PathVariable String id) {
        int theID = ConvertUtils.covertID(id);

        Dentist theDentist = dentistService.findActiveDentist(theID);
        if (theDentist == null) {
            throw new NotFoundException("Dentist id " + id + "not found");
        }
        DentistResponse dentistResponse = new DentistResponse();
        dentistResponse = accountMapper.mapDentistToDentistResponse(theDentist);
        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateAccountStatus(@PathVariable("id") String id) {
        int theID = ConvertUtils.covertID(id);
        Dentist dentist = dentistService.findById(theID);

        if (dentist == null) {
            throw new NotFoundException("Dentist id " + id + "not found");
        }

        dentist.setId(theID);
        dentist.setStatus(Status.INACTIVE.name());

        dentistService.save(dentist);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
