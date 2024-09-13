package com.hasten.common;

/**
 * @author Hasten
 */
public class UserInfoHolder {
    private static final ThreadLocal<String> userHolder = new ThreadLocal<>();

    public static void set(String userId) {
        userHolder.set(userId);
    }

    public static String get() {
        return userHolder.get();
    }

    public static void remove() {
        userHolder.remove();
    }
}

