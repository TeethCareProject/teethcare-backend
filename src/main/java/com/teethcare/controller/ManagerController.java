package com.teethcare.controller;

import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.config.googlemap.GoogleMapConfig;
import com.teethcare.exception.InternalServerError;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.location.LocationRequest;
import com.teethcare.model.response.ClinicInfoResponse;
import com.teethcare.model.response.ManagerResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.ClinicService;
import com.teethcare.service.LocationService;
import com.teethcare.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Manager.MANAGER_ENDPOINT)
@Slf4j
public class ManagerController {

    private final ManagerService managerService;
    private final ClinicService clinicService;
    private final LocationService locationService;
    private final ClinicMapper clinicMapper;
    private final AccountMapper accountMapper;
    private final ApplicationContext context;


    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAll() {
        List<Manager> managers = managerService.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for (Manager manager : managers) {
            Clinic clinic = clinicService.getClinicByManager(manager);
            if (clinic != null) {
                ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
                managerResponses.add(accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse));
            }
        }
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ManagerResponse> getActive(@PathVariable("id") int id) {
        Manager manager = managerService.getActiveManager(id);
        Clinic clinic = clinicService.getClinicByManager(manager);
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
        ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(manager, clinicInfoResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ManagerResponse> add(@Valid @RequestBody ManagerRegisterRequest managerRegisterRequest) {
        Clinic clinic = managerService.addNew(managerRegisterRequest);
        String address = clinic.getLocation().getFullAddress();
        log.info(address);
        GoogleMapConfig googleMapConfig = context.getBean(GoogleMapConfig.class);
        UriComponents uri = null;
        try {
            uri = UriComponentsBuilder.newInstance()
                    .scheme("https")
                    .host("maps.googleapis.com")
                    .path("/maps/api/place/textsearch/json")
                    .queryParam("query", address)
                    .queryParam("key", googleMapConfig.getAPIKey2().getValue())
                    .build();
            log.info(uri.toUriString());
            ResponseEntity<LocationRequest> locationRequest = new RestTemplate().getForEntity(uri.toUriString(), LocationRequest.class);

            double latitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLatitude();
            clinic.getLocation().setLatitude(latitude);
            double longitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLongitude();
            clinic.getLocation().setLongitude(longitude);
        } catch (IOException e) {
            throw new InternalServerError("Getting clinic's location from API fail");
        }

        clinicService.save(clinic);
        ClinicInfoResponse clinicInfoResponse = clinicMapper.mapClinicListToClinicInfoResponse(clinic);
        ManagerResponse managerResponse = accountMapper.mapManagerToManagerResponse(clinic.getManager(), clinicInfoResponse);
        return new ResponseEntity<>(managerResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable("id") int id) {
        Manager manager = managerService.findById(id);
        managerService.delete(id);
        Clinic clinic = clinicService.getClinicByManager(manager);
        clinicService.delete(clinic.getId());
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }

}
