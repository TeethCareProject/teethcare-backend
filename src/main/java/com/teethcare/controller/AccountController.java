package com.teethcare.controller;

import com.teethcare.common.Constant;
import com.teethcare.common.EndpointConstant;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.service.AccountService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = EndpointConstant.Account.ACCOUNT_ENDPOINT)
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAll(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_NUMBER) int page, @RequestParam(name = "size", required = false, defaultValue = Constant.PAGINATION.DEFAULT_PAGE_SIZE) int size, @RequestParam(name = "sortBy", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_BY) String field, @RequestParam(name = "sortDir", required = false, defaultValue = Constant.SORT.DEFAULT_SORT_DIRECTION) String direction) {
        Pageable pageable = PaginationAndSort.pagingAndSorting(size, page, field, direction);
        List<Account> accounts;
        if (search != null) {
            search = search.replaceAll("\\s\\s+", " ").trim();
            accounts = accountService.searchAccountsByFullName(search, pageable);
        } else {
            accounts = accountService.findAllAccounts(pageable);
        }
        List<AccountResponse> accountResponses = accountMapper.mapAccountListToAccountResponseList(accounts);
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
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
    public ResponseEntity<String> delete(@PathVariable("id") String id){
        int theID = ConvertUtils.covertID(id);
        Account account = accountService.findById(theID);
        if (account != null) {
            accountService.delete(theID);
            return new ResponseEntity<>("Delete successfuly.",HttpStatus.OK);
        }
        throw new NotFoundException("Account id " + id + " was not found!");
    }

}
