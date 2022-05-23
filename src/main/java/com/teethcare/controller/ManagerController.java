package com.teethcare.controller;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.service.AccountService;
import com.teethcare.service.CRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private AccountService accountService;

    @Autowired
    public ManagerController(AccountService accountService) {
        this.accountService = accountService;
    }

//    @GetMapping("/staffs")
//    public List<Account> getAllStaffs() {
//        List<Account> staffList = new ArrayList<>();
//        List<Account> dentistList = accountService.findByRoleId(4);
//        List<Account> customerServiceList = accountService.findByRoleId(2);
//
//        staffList.addAll(dentistList);
//        staffList.addAll(customerServiceList);
//
//        return staffList;
//    }
}
