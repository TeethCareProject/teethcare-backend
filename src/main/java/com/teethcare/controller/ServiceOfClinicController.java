package com.teethcare.controller;

import com.teethcare.common.Status;
import com.teethcare.config.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.service.ServiceOfClinicServiceImpl;
import com.teethcare.utils.PaginationAndSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceOfClinicController {
    private ServiceOfClinicService serviceOfClinicService;
    private ServiceOfClinicMapper serviceOfClinicMapper;

    @Autowired
    public ServiceOfClinicController(ServiceOfClinicService serviceOfClinicService,
                                     ServiceOfClinicMapper serviceOfClinicMapper) {
        this.serviceOfClinicService = serviceOfClinicService;
        this.serviceOfClinicMapper = serviceOfClinicMapper;
    }
    @GetMapping("/clinics/{id}")
    ResponseEntity<List<ServiceOfClinicResponse>> serviceOfClinicResponses(@PathVariable int id,
                                                                           @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                           @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
                                                                           @RequestParam(name = "sortBy", required = false, defaultValue = "id") String field,
                                                                           @RequestParam(name = "sortDir", required = false, defaultValue = "ASC") String direction) {

        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);

        List<ServiceOfClinic> serviceList = serviceOfClinicService.findByClinicIdAndStatus(id, Status.ACTIVE.name(), pageable);

        List<ServiceOfClinicResponse> serviceResponseList = new ArrayList<>();

        serviceResponseList = serviceOfClinicMapper.mapServiceListToServiceResponseList(serviceList);

        return new ResponseEntity<>(serviceResponseList, HttpStatus.OK);
    }
}
