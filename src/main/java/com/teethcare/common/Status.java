package com.teethcare.common;

public class Status {
    private Status() {
    }

    ;

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
        UNAVAILABLE
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
