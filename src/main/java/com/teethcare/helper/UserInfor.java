package com.teethcare.helper;

import com.teethcare.config.security.JwtTokenUtil;
import com.teethcare.model.entity.Account;
import com.teethcare.service.AccountService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserInfor {
    private final AccountService accountService;
    public Account getFromToken(String token) {
        token = token.substring("Bearer ".length());

        JwtTokenUtil tokenUtil = new JwtTokenUtil(accountService);
        String username = tokenUtil.getUsernameFromJwt(token);

        return accountService.getAccountByUsername(username);
    }
}
