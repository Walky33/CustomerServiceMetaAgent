package com.atome.bot.util;

public final class TextMatchUtil {

    private TextMatchUtil() {
    }

    public static boolean looselyMatches(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        String left = a.trim().toLowerCase();
        String right = b.trim().toLowerCase();
        return left.equals(right) || left.contains(right) || right.contains(left);
    }
}
