package com.teethcare.utils;

import com.teethcare.exception.BadRequestException;
import org.apache.commons.lang3.math.NumberUtils;

public class ConvertUtils {
    private ConvertUtils(){};
    public static int covertID(String inputId){
        int theID = 0;
        if(!NumberUtils.isCreatable(inputId)){
            throw new BadRequestException("Id " + inputId + " invalid");
        }
        return Integer.parseInt(inputId);
    }
}
