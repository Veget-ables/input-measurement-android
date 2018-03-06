package com.slpl.measure.util;

import android.support.annotation.Nullable;

public class StringUtil {
    @Nullable
    public static int diffIndex(String target, String source) {
        String tmp = "";
        for (char c : source.toCharArray()) {
            tmp += String.valueOf(c);
            if (!target.startsWith(tmp)) {
                return tmp.length() - 1;
            }
        }
        return source.length();
    }

    @Nullable
    public static String extractDiff(String target, String source) {
        String tmp = "";
        for (char c : source.toCharArray()) {
            tmp += String.valueOf(c);
            if (!target.startsWith(tmp)) {
                return String.valueOf(target.charAt(tmp.length() - 1));
            }
        }
        return null;
    }

}
