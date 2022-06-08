package com.teethcare.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointConstant {
    private static final String ROOT_ENDPOINT = "/api";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Booking {
        public static final String BOOKING_ENDPOINT = ROOT_ENDPOINT + "/bookings";
    }

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
    public static final class Staff {
        public static final String STAFF_ENDPOINT = ROOT_ENDPOINT + "/staffs";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CustomerService {
        public static final String CUSTOMER_SERVICE_ENDPOINT = ROOT_ENDPOINT + "/customer-services";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Account {
        public static final String ACCOUNT_ENDPOINT = ROOT_ENDPOINT + "/accounts";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Province {
        public static final String PROVINCE_ENDPOINT = ROOT_ENDPOINT + "/provinces";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Feedback {
        public static final String FEEDBACK_ENDPOINT = ROOT_ENDPOINT + "/feedbacks";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Report {
        public static final String REPORT_ENDPOINT = ROOT_ENDPOINT + "/reports";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Notification {
        public static final String NOTIFICATION_ENDPOINT = ROOT_ENDPOINT + "/notifications";
        public static final String FCM_TOKEN_ENDPOINT = ROOT_ENDPOINT + "/fcm-tokens";
    }
}
