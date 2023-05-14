package com.dp.spring.parallel.hestia.utils;

import com.dp.spring.springcore.exceptions.UnsupportedCallToPrivateConstructor;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for random password generation matching either custom or default regex and length.
 */
public class RandomPasswordUtils {
    public static final String DEFAULT_PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 8;
    private static final int DEFAULT_PASSWORD_LENGTH = 16;

    public static final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPERCASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "1234567890";
    public static final String SPECIAL_CHARACTERS = "@#$%^&+=!";
    private static final String DEFAULT_PASSWORD_ALPHABET =
            LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS + SPECIAL_CHARACTERS;

    private RandomPasswordUtils() {
        throw new UnsupportedCallToPrivateConstructor();
    }

    public static String generateRandomPassword() {
        return RandomStringUtils.random(DEFAULT_PASSWORD_LENGTH, DEFAULT_PASSWORD_ALPHABET);
    }

    public static String generateRandomPassword(final String alphabet, final int length) {
        return RandomStringUtils.random(length, alphabet);
    }
}
