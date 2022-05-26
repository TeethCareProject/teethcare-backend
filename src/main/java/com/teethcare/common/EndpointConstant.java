package com.teethcare.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointConstant {
    private static final String ROOT_ENDPOINT = "/api";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Authentication {
        public static final String AUTHENTICATION_ENDPOINT = ROOT_ENDPOINT + "/auth";
        public static final String LOGIN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/login";
        public static final String LOGOUT_ENDPOINT = AUTHENTICATION_ENDPOINT + "/logout";
        public static final String REFRESH_TOKEN_ENDPOINT = AUTHENTICATION_ENDPOINT + "/refresh-token";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Patient {
        public static final String PATIENT_ENDPOINT = ROOT_ENDPOINT + "/patients";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Clinic {
        public static final String CLINIC_ENDPOINT = ROOT_ENDPOINT + "/clinics";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Service {
        public static final String SERVICE_ENDPOINT = ROOT_ENDPOINT + "/services";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Manager {
        public static final String MANAGER_ENDPOINT = ROOT_ENDPOINT + "/managers";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Dentist {
        public static final String DENTIST_ENDPOINT = ROOT_ENDPOINT + "/dentists";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CustomerService {
        public static final String CUSTOMER_SERVICE_ENDPOINT = ROOT_ENDPOINT + "/customer-services";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Account {
        public static final String ACCOUNT_ENDPOINT = ROOT_ENDPOINT + "/accounts";
    }
}
