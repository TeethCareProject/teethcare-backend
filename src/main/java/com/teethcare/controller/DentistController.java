package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.request.DentistRegisterRequest;
import com.teethcare.model.response.DentistResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.DentistService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Dentist.DENTIST_ENDPOINT)
public class DentistController {
    private final DentistService dentistService;
    private final AccountMapper accountMapper;

    @GetMapping()
    public ResponseEntity<Page<DentistResponse>> getAll(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.pagingAndSorting(size, page, field, direction);
        Page<Dentist> dentists = dentistService.findAllWithPaging(pageable);
        Page<DentistResponse> dentistResponses = dentists.map(accountMapper::mapDentistToDentistResponse);
        return new ResponseEntity<>(dentistResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistResponse> get(@PathVariable int id) {
        Dentist theDentist = dentistService.findActive(id);
        if (theDentist == null) {
            throw new NotFoundException("Dentist id " + id + "not found");
        }
        DentistResponse dentistResponse = accountMapper.mapDentistToDentistResponse(theDentist);
        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DentistResponse> add(@Valid @RequestBody DentistRegisterRequest dentistRegisterRequest,
                                               @RequestHeader(AUTHORIZATION) String token) {
        Dentist dentist = dentistService.addNew(dentistRegisterRequest, token.substring("Bearer ".length()));
        DentistResponse dentistResponse = accountMapper.mapDentistToDentistResponse(dentist);
        return new ResponseEntity<>(dentistResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@PathVariable("id") int id) {
        Dentist dentist = dentistService.findById(id);

        dentist.setId(id);
        dentist.setStatus(Status.Account.INACTIVE.name());

        dentistService.save(dentist);

        MessageResponse messageResponse = new MessageResponse(Message.SUCCESS_FUNCTION.name());
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
