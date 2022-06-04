package com.teethcare.utils;

import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.model.entity.Account;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserInfor {
    public static Account getFromToken(String token, AccountService accountService) {
        token = token.substring("Bearer ".length());

        JwtTokenUtil tokenUtil = new JwtTokenUtil(accountService);
        System.out.println(token);
        String username = tokenUtil.getUsernameFromJwt(token);
        System.out.println(username);
        return accountService.getAccountByUsername(username);
    }
}
