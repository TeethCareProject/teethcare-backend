package com.teethcare.mapper;

import java.sql.Date;
import java.sql.Timestamp;

public class ConfigurationMapping {
    static Long mapDateTimeToLong(Timestamp dateTime) {
        if (dateTime != null) {
            return dateTime.getTime();
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
}
