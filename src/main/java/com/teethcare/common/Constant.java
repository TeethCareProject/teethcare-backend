package com.teethcare.common;

public class Constant {
    public static class PAGINATION {
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "5";
    }

    public static class SORT {
        public static final String DEFAULT_SORT_BY = "id";
        public static final String DEFAULT_SORT_DIRECTION = "asc";
    }

    public static class EMAIL {
        public static final String SENDER_EMAIL = "service.teethcare@gmail.com";
        public static final String SENDER_PASSWORD = "ptmbozrgnkgexitx";
    }

    public static class PASSWORD {
        public static final int DEFAULT_LENGTH = 6;
        public static final int DEFAULT_FORGOT_PASSWORD_KEY_LENGTH = 32;
    }
}
