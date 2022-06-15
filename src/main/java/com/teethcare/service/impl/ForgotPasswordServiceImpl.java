package com.teethcare.service.impl;

import com.teethcare.common.Constant;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.ForgotPasswordKey;
import com.teethcare.model.request.ForgotPasswordRequest;
import com.teethcare.repository.ForgotPasswordKeyRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.ForgotPasswordService;
import com.teethcare.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
    private final AccountService accountService;
    private final ForgotPasswordKeyRepository forgotPasswordKeyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void addKey(String username) {
        ForgotPasswordKey forgotPasswordKey = new ForgotPasswordKey();
        Account account = accountService.getAccountByUsername(username);
        if (account == null) {
            throw new NotFoundException("Username not found!");
        }
        forgotPasswordKey.setAccount(account);
        String generatedKey;
        do {
            generatedKey = StringUtils.generateRandom(Constant.PASSWORD.DEFAULT_FORGOT_PASSWORD_KEY_LENGTH);
        } while (forgotPasswordKeyRepository.findForgotPasswordKeyByKey(generatedKey) != null);
        forgotPasswordKey.setKey(generatedKey);
        forgotPasswordKey.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        save(new ForgotPasswordKey());
    }

    @Override
    public void changePassword(ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordKey forgotPasswordKey = forgotPasswordKeyRepository.findForgotPasswordKeyByKey(forgotPasswordRequest.getForgotPasswordKey());
        if (forgotPasswordKey == null || forgotPasswordKey.getStatus().equals(Status.ForgotPasswordKey.INACTIVE.name())) {
            throw new BadRequestException("Key is invalid");
        }
        if (!forgotPasswordRequest.getPassword().equals(forgotPasswordRequest.getConfirmedPassword())) {
            throw new BadRequestException("Confirm Password is not match with password");
        }
        Account account = forgotPasswordKey.getAccount();
        account.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
        accountService.update(account);
    }

    @Override
    public List<ForgotPasswordKey> findAll() {
        return null;
    }

    @Override
    public ForgotPasswordKey findById(int id) {
        return null;
    }

    @Override
    public void save(ForgotPasswordKey theEntity) {
        theEntity.setStatus(Status.ForgotPasswordKey.ACTIVE.name());
        forgotPasswordKeyRepository.save(theEntity);
    }

    @Override
    public void delete(int theId) {

    }

    @Override
    public void update(ForgotPasswordKey theEntity) {

    }
}
