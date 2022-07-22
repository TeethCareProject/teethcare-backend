package com.teethcare.service.impl.account;

import com.teethcare.common.Message;
import com.teethcare.common.Status;
import com.teethcare.exception.BadRequestException;
import com.teethcare.exception.NotFoundException;
import com.teethcare.mapper.AccountMapper;
import com.teethcare.model.entity.Account;
import com.teethcare.model.request.AccountFilterRequest;
import com.teethcare.model.request.AccountUpdateStatusRequest;
import com.teethcare.model.request.ProfileUpdateRequest;
import com.teethcare.model.request.StaffPasswordRequest;
import com.teethcare.repository.AccountRepository;
import com.teethcare.service.AccountService;
import com.teethcare.service.FileService;
import com.teethcare.utils.ConvertUtils;
import com.teethcare.utils.PaginationAndSortFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FileService fileService;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> findAllAccounts(Pageable pageable) {
        return accountRepository.findAllByStatusIsNotNull(pageable);
    }

    @Override
    public Page<Account> findAllByFilter(AccountFilterRequest filter, Pageable pageable) {
        List<Account> accounts = accountRepository.findAll(pageable.getSort());
        accounts = accounts.stream().filter(filter.getPredicate()).collect(Collectors.toList());
        return PaginationAndSortFactory.convertToPage(accounts, pageable);
    }

    @Override
    public Account findById(int id) {
        return accountRepository.findById(id);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void delete(int id) {
        Account account = accountRepository.findById(id);
        account.setStatus(Status.Account.INACTIVE.name());
        accountRepository.save(account);
    }

    @Override
    public void update(Account theEntity) {
        accountRepository.save(theEntity);
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }


    @Override
    public Account getActiveAccountByUsername(String username) {
        return accountRepository.findAccountByUsernameAndStatus(username, Status.Account.ACTIVE.name());
    }

    @Override
    public List<Account> findByRoleId(int id) {
        return accountRepository.findByRoleId(id);
    }

    @Override
    public boolean isDuplicated(String username) {
        return accountRepository.getAccountByUsername(username) != null;
    }

    @Override
    public List<Account> searchAccountsByFullName(String search, Pageable pageable) {
        return accountRepository.searchAccountsByFullName(search, pageable);
    }

    @Override
    public void updateStatus(AccountUpdateStatusRequest accountUpdateStatusRequest, int id) {
        String status = accountUpdateStatusRequest.getStatus();
        if (status != null
                && (status.toUpperCase().trim().equals(Status.Account.INACTIVE.name())
                || status.toUpperCase().trim().equals(Status.Account.ACTIVE.name()))) {
            Account account = findById(id);
            if (account != null) {
                account.setStatus(status.toUpperCase().trim());
                update(account);
            } else {
                throw new NotFoundException("Account not found!");
            }
        } else {
            throw new BadRequestException(Message.INVALID_STATUS.name());
        }
    }

    @Override
    public Account updateProfile(ProfileUpdateRequest updateRequest, String username) {
        Account account = accountRepository.getAccountByUsername(username);
        accountMapper.updateAccountFromProfileUpdateRequest(updateRequest, account);

        long milliseconds = updateRequest.getDateOfBirth();
        Date dob = ConvertUtils.getDate(milliseconds);
        account.setDateOfBirth(dob);

        save(account);
        return account;
    }

    @Override
    public Account updateImage(MultipartFile image, String username) {
        Account account = accountRepository.getAccountByUsername(username);
        account.setAvatarImage(fileService.uploadFile(image));
        save(account);
        return account;
    }

    @Override
    public void setStaffPassword(int staffId, StaffPasswordRequest staffPasswordRequest) {
        if (staffPasswordRequest.getPassword().equals(staffPasswordRequest.getConfirmPassword())) {
            Account account = accountRepository.findAccountsById(staffId);
            account.setPassword(passwordEncoder.encode(staffPasswordRequest.getPassword()));
            account.setStatus(Status.Account.ACTIVE.name());
            update(account);
        }
        throw new BadRequestException("Confirm Password is not match with password");
    }
}
