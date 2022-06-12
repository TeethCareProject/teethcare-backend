package com.teethcare.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
        APPOINTMENT
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
}