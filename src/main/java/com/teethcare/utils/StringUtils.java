package com.teethcare.utils;

public class StringUtils {
    public static boolean containsIgnoreCase(String s1, String s2) {
        return s1.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase()
                .contains(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase());
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        return s1.replaceAll("\\s\\s+", " ").trim()
                .equalsIgnoreCase(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim());
    }
}
