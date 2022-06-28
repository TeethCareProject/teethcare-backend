package com.teethcare.utils;

import java.sql.Time;

public class TimeUtils {
    public static int ceilHour (Time time) {
        return time.toLocalTime().getMinute() > 0 ? time.toLocalTime().getHour() + 1 : time.toLocalTime().getHour();
    }

    public static int floorHour (Time time) {
        return time.toLocalTime().getMinute() < 30 ? time.toLocalTime().getHour() - 1 : time.toLocalTime().getHour();
    }
}
