package com.teethcare.utils;

public class StringUtils {
    public static boolean ContainsIgnoreCase(String s1, String s2) {
        return s1.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase()
                .contains(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase());
    }

    public static boolean EqualsIgnoreCase(String s1, String s2) {
        return s1.replaceAll("\\s\\s+", " ").trim()
                .equalsIgnoreCase(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim());
    }
}
