package com.roadmap.challenge_short_url.utils;

import java.util.Random;

public class ShortCodeGenerator {

    private static final String CHARACTERS
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 7;
    private static final Random RANDOM = new Random();

    public static String generate() {
        StringBuilder shortCode = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            shortCode.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return shortCode.toString();
    }

}
