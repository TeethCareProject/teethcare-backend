package com.teethcare.config.security;

import com.teethcare.common.Status;
import com.teethcare.exception.UnauthorizedException;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserDetailServiceImp implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getAccountByUsername(username);
        if (account != null && !account.getStatus().equals(Status.Account.INACTIVE.name())) {
            return UserDetailsImpl.build(account);
        } else if (account.getStatus().equals(Status.Account.INACTIVE.name())) {
            throw new UnauthorizedException("Your account is currently inactive.");
        } else {
            throw new UnauthorizedException("Username " + username + " not found ");
        }
    }
}
