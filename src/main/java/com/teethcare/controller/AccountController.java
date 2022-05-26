package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.config.mapper.AccountMapper;
import com.teethcare.exception.IdNotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.CustomErrorResponse;
import com.teethcare.service.AccountService;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
@EnableSwagger2
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Account.ACCOUNT_ENDPOINT)
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
   // @PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                                @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                                @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                                @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        List<Account> accounts = accountService.findAllAccounts(pageable);
        List<AccountResponse> accountResponses = accountMapper.mapAccountListToAccountResponseList(accounts);
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    //@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
    public ResponseEntity getAccountById(@PathVariable("id") int id) {
        Account account = accountService.findById(id);
        if (account != null) {
            AccountResponse accountResponse = accountMapper.mapAccountToAccountResponse(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        } else throw new IdNotFoundException("Account id " + id + " not found!");
    }
}
