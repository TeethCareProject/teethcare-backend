package com.teethcare.config.security;

import com.teethcare.common.Status;
import com.teethcare.exception.AccountNotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        Account account = accountRepository.findAccountByUsernameAndStatus(username, Status.Account.ACTIVE.name());
        if (account != null) {
            return UserDetailsImpl.build(account);
        } else {
            throw new AccountNotFoundException("Username " + username + " not found ");
        }
    }
}
