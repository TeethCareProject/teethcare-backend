package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.config.googlemap.GoogleMapConfig;
import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.config.security.UserDetailUtil;
import com.teethcare.exception.InternalServerError;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.mapper.ClinicMapper;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.*;
import com.teethcare.model.request.ClinicFilterRequest;
import com.teethcare.model.request.ClinicRequest;
import com.teethcare.model.request.ServiceFilterRequest;
import com.teethcare.model.request.location.LocationRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.ClinicResponse;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.*;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Clinic.CLINIC_ENDPOINT)
@Slf4j
public class ClinicController {

    private final AccountService accountService;
    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;
    private final CSService csService;
    private final DentistService dentistService;
    private final AccountMapper accountMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;
    private final ApplicationContext context;
    private final LocationService locationService;


    @GetMapping
    public ResponseEntity<Page<ClinicResponse>> getAllWithFilter(ClinicFilterRequest clinicFilterRequest,
                                                                 @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                 @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                 @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<Clinic> list = clinicService.findAllWithFilter(clinicFilterRequest, pageable);
        Page<ClinicResponse> clinicResponses = list.map(clinicMapper::mapClinicToClinicResponse);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping("/suggested")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).PATIENT)")
    public ResponseEntity<Page<ClinicResponse>> getNearClinic(@RequestParam(required = false) Double longitude,
                                                              @RequestParam(required = false) Double latitude,
                                                              @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                              @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                              @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        String username = UserDetailUtil.getUsername();
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<ClinicResponse> clinicResponses = clinicService.findNear(longitude, latitude, username, pageable);
        return ResponseEntity.ok(clinicResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicResponse> getClinic(@PathVariable("id") int id) {
        Clinic clinic = clinicService.findById(id);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        Clinic clinic = clinicService.findById(id);
        clinic.setStatus(Status.Clinic.INACTIVE.name());
        clinicService.save(clinic);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ClinicResponse> update(@Valid @RequestBody ClinicRequest clinicRequest,
                                                  @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Clinic clinic = clinicService.updateProfile(clinicRequest, username);
//        String address = clinic.getLocation().getFullAddress();
//        GoogleMapConfig googleMapConfig = context.getBean(GoogleMapConfig.class);
//        UriComponents uri;
//        if (clinicRequest.getClinicAddress() != null) {
//            try {
//                uri = UriComponentsBuilder.newInstance()
//                        .scheme("https")
//                        .host("maps.googleapis.com")
//                        .path("/maps/api/place/textsearch/json")
//                        .queryParam("query", address)
//                        .queryParam("key", googleMapConfig.getAPIKey2().getValue())
//                        .build();
//                log.info(uri.toUriString());
//                ResponseEntity<LocationRequest> locationRequest = new RestTemplate().getForEntity(uri.toUriString(), LocationRequest.class);
//
//                double latitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLatitude();
//                clinic.getLocation().setLatitude(latitude);
//                double longitude = Objects.requireNonNull(locationRequest.getBody()).getResults().get(0).getGeometry().getLocation().getLongitude();
//                clinic.getLocation().setLongitude(longitude);
//            } catch (IOException e) {
//                throw new InternalServerError("Getting clinic's location from API fail");
//            }
//        }

//        clinicService.save(clinic);
        ClinicResponse response = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/update-image")
    @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).MANAGER)")
    public ResponseEntity<ClinicResponse> updateImage(@RequestBody MultipartFile image,
                                                 @RequestHeader(value = AUTHORIZATION) String token) {
        token = token.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromJwt(token);
        Clinic clinic = clinicService.updateImage(image, username);
        ClinicResponse response = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/staffs")
    public ResponseEntity<List<AccountResponse>> findAllStaffs(@PathVariable int id) {

        List<Account> staffList = new ArrayList<>();

        List<Dentist> dentistList = dentistService.findByClinicIdAndStatus(id, Status.Account.ACTIVE.name());
        List<CustomerService> customerServiceList = csService.findByClinicIdAndStatus(id, Status.Account.ACTIVE.name());

        staffList.addAll(dentistList);
        staffList.addAll(customerServiceList);

        List<AccountResponse> staffResponseList = accountMapper.mapAccountListToAccountResponseList(staffList);

        if (staffResponseList == null || staffResponseList.size() == 0) {
            throw new NotFoundException("With id " + id + ", the list of hospital staff could not be found.");
        }

        return new ResponseEntity<>(staffResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<Page<ServiceOfClinicResponse>> getService(ServiceFilterRequest serviceFilterRequest,
                                                                    @RequestHeader(value = "AUTHORIZATION", required = false) String token,
                                                                    @PathVariable("id") int id,
                                                                    @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                    @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                    @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                    @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);


        serviceFilterRequest.setClinicID(id);

        Account account = null;
        if (token != null) {
            token = token.substring("Bearer ".length());
            String username = jwtTokenUtil.getUsernameFromJwt(token);
            account = accountService.getAccountByUsername(username);
        }

        Page<ServiceOfClinic> list = serviceOfClinicService.findAllWithFilter(serviceFilterRequest, pageable, account);

        Page<ServiceOfClinicResponse> responses = list.map(new Function<ServiceOfClinic, ServiceOfClinicResponse>() {
            @Override
            public ServiceOfClinicResponse apply(ServiceOfClinic service) {
                return serviceOfClinicMapper.mapServiceOfClinicToServiceOfClinicResponse(service);
            }
        });
        return new ResponseEntity<>(responses, HttpStatus.OK);

    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ClinicResponse> approve(@PathVariable("id") int id) throws MessagingException {
        Clinic clinic = clinicService.findById(id);
        clinic = clinicService.approve(clinic);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ClinicResponse> reject(@PathVariable("id") int id) throws MessagingException {
        Clinic clinic = clinicService.findById(id);
        clinic = clinicService.reject(clinic);
        ClinicResponse clinicResponse = clinicMapper.mapClinicToClinicResponse(clinic);
        return new ResponseEntity<>(clinicResponse, HttpStatus.OK);
    }
}
