package com.teethcare.utils;

import com.teethcare.exception.BadRequestException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {

    public static int covertID(String inputId) {
        int theID = 0;
        if (!NumberUtils.isCreatable(inputId)) {
            throw new BadRequestException("Id " + inputId + " invalid");
        }
        return Integer.parseInt(inputId);
    }

    public static Timestamp getTimestamp(long timestampInString) {
        Date date = new Date(timestampInString);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted = format.format(date);
        return Timestamp.valueOf(formatted);
    }

    public static Date getDate(long timestampInString) {
        Date date = new Date(timestampInString);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(date);
        return date;
    }
}
