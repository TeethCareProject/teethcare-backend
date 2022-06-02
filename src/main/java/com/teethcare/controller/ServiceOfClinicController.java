package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Service.SERVICE_ENDPOINT)
public class ServiceOfClinicController {
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;

    @GetMapping
    public ResponseEntity<List<ServiceOfClinicResponse>> getAll(@RequestParam(name = "status", required = false) String status,
                                                        @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        List<ServiceOfClinic> list = new ArrayList<>();
        if (status != null) {
            list = serviceOfClinicService.findByStatus(pageable, status);
        } else {
            list = serviceOfClinicService.findAll(pageable);
        }
        List<ServiceOfClinicResponse> responses = serviceOfClinicMapper.mapServiceListToServiceResponseList(list);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/clinics/{id}")
    public ResponseEntity<List<ServiceOfClinicResponse>> serviceOfClinicResponses(@PathVariable String id,
                                                                           @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                           @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                           @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                           @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {

        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);

        int theID = ConvertUtils.covertID(id);

        List<ServiceOfClinic> serviceList = serviceOfClinicService.findByClinicIdAndStatus(theID, Status.Service.ACTIVE.name(), pageable);

        List<ServiceOfClinicResponse> serviceResponseList =
                serviceOfClinicMapper.mapServiceListToServiceResponseList(serviceList);

        return new ResponseEntity<>(serviceResponseList, HttpStatus.OK);
    }
}
