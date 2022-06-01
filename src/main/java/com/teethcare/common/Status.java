package com.teethcare.common;

public class Status {
    private Status() {
    }

    ;

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
