package com.teethcare.utils;

import java.util.Random;

public class StringUtils {
    public static boolean containsIgnoreCase(String s1, String s2) {
        return s1.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase()
                .contains(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim().toUpperCase());
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        return s1.replaceAll("\\s\\s+", " ").trim()
                .equalsIgnoreCase(s2.toUpperCase().replaceAll("\\s\\s+", " ").trim());
    }

    public static String generateRandom(int length) {
        int leftLimit = 65; // letter 'A'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
