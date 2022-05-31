package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.DentistRegisterRequest;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.AccountService;
import com.teethcare.service.ClinicService;
import com.teethcare.service.DentistService;
import com.teethcare.service.ManagerService;
import com.teethcare.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Dentist.DENTIST_ENDPOINT)
public class DentistController {
    private final DentistService dentistService;
    private final ClinicService clinicService;
    private final ManagerService managerService;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<List<DentistResponse>> getAll() {
        List<Dentist> dentistList = dentistService.findAll();
        List<DentistResponse> dentistResponses = accountMapper.mapDentistListToDentistResponseList(dentistList);
        return new ResponseEntity<>(dentistResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> get(@PathVariable String id) {
        int theID = ConvertUtils.covertID(id);

        Dentist theDentist = dentistService.findActiveDentist(theID);
        if (theDentist == null) {
            throw new NotFoundException("Dentist id " + id + "not found");
        }
        DentistResponse dentistResponse = accountMapper.mapDentistToDentistResponse(theDentist);
        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DentistResponse> add(@Valid @RequestBody DentistRegisterRequest dentistRegisterRequest,
                                               HttpServletRequest request) {
        boolean isDuplicated = accountService.isDuplicated(dentistRegisterRequest.getUsername());
        if (!isDuplicated) {
            if (dentistRegisterRequest.getPassword().equals(dentistRegisterRequest.getConfirmPassword())) {
                String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
                Account account = jwtTokenUtil.getAccountFromJwt(token);
                Clinic clinic = clinicService.getClinicByManager(managerService.findById(account.getId()));

                Dentist dentist = accountMapper.mapDentistRegisterRequestToDentist(dentistRegisterRequest);
                dentist.setClinic(clinic);
                dentistService.save(dentist);
                DentistResponse dentistResponse = accountMapper.mapDentistToDentistResponse(dentist);
                return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
            } else {
                throw new BadRequestException("confirm Password is not match with password");
            }
        } else {
            throw new BadRequestException("User existed!");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable("id") String id) {
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
