package com.teethcare.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {
    public static Matcher get(String paramString) {
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        return pattern.matcher(paramString + ",");
    }
}
