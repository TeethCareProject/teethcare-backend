package com.teethcare.service;

import com.teethcare.model.entity.Account;
import com.teethcare.model.request.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface AccountService extends CRUDService<Account> {
    Account getAccountByUsername(String username);

    Account getActiveAccountByUsername(String username);

    Account findById(int id);

    boolean isDuplicated(String username);

    List<Account> findByRoleId(int id);

    List<Account> findAllAccounts(Pageable pageable);

    List<Account> searchAccountsByFullName(String search, Pageable pageable);

    Page<Account> findAllByFilter(AccountFilterRequest filter, Pageable pageable);

    void updateStatus(AccountUpdateStatusRequest accountUpdateStatusRequest, int id);

    void setStaffPassword(int staffId, StaffPasswordRequest staffPasswordRequest);

    Account updateProfile(ProfileUpdateRequest updateRequest, String username);
    Account updateImage(MultipartFile multipartFile, String username);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
