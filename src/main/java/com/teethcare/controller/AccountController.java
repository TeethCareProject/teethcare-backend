package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.common.Message;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.AccountFilterRequest;
import com.teethcare.model.request.AccountUpdateStatusRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.MessageResponse;
import com.teethcare.service.AccountService;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Account.ACCOUNT_ENDPOINT)
@PreAuthorize("hasAuthority(T(com.teethcare.common.Role).ADMIN)")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<Page<AccountResponse>> getAll(AccountFilterRequest filter,
                                                        @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size,
                                                        @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field,
                                                        @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSortFactory.getPagable(size, page, field, direction);
        Page<Account> accounts = accountService.findAllByFilter(filter, pageable);
        Page<AccountResponse> clinicResponses = accounts.map(accountMapper::mapAccountToAccountResponse);
        return new ResponseEntity<>(clinicResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable("id") int id) {
        Account account = accountService.findById(id);
        if (account != null) {
            AccountResponse accountResponse = accountMapper.mapAccountToAccountResponse(account);
            return new ResponseEntity<>(accountResponse, HttpStatus.OK);
        } else {
            throw new NotFoundException("Account id " + id + " not found!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        Account account = accountService.findById(id);
        accountService.delete(id);
        return new ResponseEntity<>("Delete successfully.", HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateStatus(@RequestBody AccountUpdateStatusRequest accountUpdateStatusRequest, @PathVariable int id) {
        accountService.updateStatus(accountUpdateStatusRequest, id);
        return new ResponseEntity<>(new MessageResponse(Message.SUCCESS_FUNCTION.name()), HttpStatus.OK);
    }
}
