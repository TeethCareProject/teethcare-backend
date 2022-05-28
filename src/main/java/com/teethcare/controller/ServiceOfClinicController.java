package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.mapper.ServiceOfClinicMapper;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.model.response.ServiceOfClinicResponse;
import com.teethcare.service.ServiceOfClinicService;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Service.SERVICE_ENDPOINT)
public class ServiceOfClinicController {
    private final ServiceOfClinicService serviceOfClinicService;
    private final ServiceOfClinicMapper serviceOfClinicMapper;

    @GetMapping("/clinics/{id}")
    ResponseEntity<List<ServiceOfClinicResponse>> serviceOfClinicResponses(@PathVariable String id,
                                                                           @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                           @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                           @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                           @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {

        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);

        int theID = 0;
        if(!NumberUtils.isCreatable(id)){
            throw new BadRequestException("Id " + id + " invalid");
        }
        theID = Integer.parseInt(id);

        List<ServiceOfClinic> serviceList = serviceOfClinicService.findByClinicIdAndStatus(theID, Status.ACTIVE.name(), pageable);

        List<ServiceOfClinicResponse> serviceResponseList = new ArrayList<>();

        serviceResponseList = serviceOfClinicMapper.mapServiceListToServiceResponseList(serviceList);

        return new ResponseEntity<>(serviceResponseList, HttpStatus.OK);
    }
}
