package com.teethcare.config.security;

import com.teethcare.common.Status;
import com.teethcare.exception.AuthorException;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailServiceImp implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.getAccountByUsername(username);
        if (account != null && !account.getStatus().equals(Status.Account.INACTIVE.name())) {
            return UserDetailsImpl.build(account);
        } else if (account.getStatus().equals(Status.Account.INACTIVE.name())) {
            throw new AuthorException("Your account is currently inactive.");
        } else {
            throw new AuthorException("Username " + username + " not found ");
        }
    }
}

