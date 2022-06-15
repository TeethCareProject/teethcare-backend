package com.teethcare.service;

import com.teethcare.model.entity.ForgotPasswordKey;
import com.teethcare.model.request.ForgotPasswordRequest;

public interface ForgotPasswordService extends CRUDService<ForgotPasswordKey> {
    void addKey(String username);

    void changePassword(ForgotPasswordRequest forgotPasswordRequest);
}
