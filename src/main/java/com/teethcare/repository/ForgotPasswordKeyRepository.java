package com.teethcare.repository;

import com.teethcare.model.entity.ForgotPasswordKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordKeyRepository extends JpaRepository<ForgotPasswordKey, String> {
    ForgotPasswordKey findForgotPasswordKeyByKey(String key);
}