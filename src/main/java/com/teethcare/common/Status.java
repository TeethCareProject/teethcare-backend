package com.teethcare.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Status {

    public enum Report {
        PENDING,
        REJECTED,
        APPROVED,
        INACTIVE
    }

    public enum Feedback {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    public enum Account {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    public enum Booking {
        PENDING,
        REQUEST,
        TREATMENT,
        DONE,
        REJECTED,
        UNAVAILABLE,
    }

    public enum Appointment {
        ACTIVE,
        INACTIVE;

        public static List<String> getNames() {
            return Arrays.stream(Appointment.values()).map(Enum::name).collect(Collectors.toList());
        }
    }

    public enum Service {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    public enum Clinic {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    public enum CheckTime {
        AVAILABLE,
        UNAVAILABLE
    }

    public enum Voucher {
        AVAILABLE,
        UNAVAILABLE
    }
}