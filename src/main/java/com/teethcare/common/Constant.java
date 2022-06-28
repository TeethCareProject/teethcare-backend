package com.teethcare.common;

import io.grpc.internal.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class Constant {
    @Value("${front.end.origin}")
    private static String FRONT_END_ORIGIN;

    public static class PAGINATION {
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "5";
    }

    public static class SORT {
        public static final String DEFAULT_SORT_BY = "id";
        public static final String DEFAULT_SORT_DIRECTION = "asc";
    }

    @Component
    public static class LOCATION {
        public static final double DEFAULT_DISTANCE = 15;
    }

    public static class EMAIL {
        public static final String SENDER_EMAIL = "service.teethcare@gmail.com";
        public static final String SENDER_PASSWORD = "ptmbozrgnkgexitx";
        public static String BOOKING_DETAIL_CONFIRM = FRONT_END_ORIGIN +"/confirmBooking/";
    }

    public static class PASSWORD {
        public static final int DEFAULT_LENGTH = 6;
    }
}
