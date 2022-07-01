package com.teethcare.utils;

import com.teethcare.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {

    public static Date getDate(long timestampInString) {
        Date date = new Date(timestampInString);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(date);
        return date;
    }

    public static Time convertToTime(long milliseconds) {
        Time time = new Time(milliseconds);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String formatted = format.format(time);
        return Time.valueOf(formatted);
    }
}
