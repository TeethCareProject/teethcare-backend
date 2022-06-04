package com.teethcare.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Status {

    public enum Report {
        PENDING,
        REJECTED,
        APPROVED,
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
        REQUESTING,
        TREATING,
        DONE,
        REJECTED
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