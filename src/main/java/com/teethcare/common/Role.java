package com.teethcare.common;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MANAGER,
    CUSTOMER_SERVICE,
    PATIENT,
    DENTIST;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
