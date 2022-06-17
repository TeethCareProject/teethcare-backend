package com.teethcare.mapper;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import static com.teethcare.utils.ConvertUtils.convertToTime;
public class ConfigurationMapping {
    static Long mapDateToLong(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return null;
        }

    }

    static Long mapDateTimeToLong(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return null;
        }

    }

    static Time mapLongToTime(Long milliseconds) {
        if (milliseconds != null) {
            return convertToTime(milliseconds);
        } else {
            return null;
        }
    }

    static Long mapTimeToLong(Time time) {
        if (time != null) {
            return time.getTime();
        } else {
            return null;
        }
    }
}
