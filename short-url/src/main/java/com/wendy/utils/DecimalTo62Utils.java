package com.wendy.utils;

import java.util.Objects;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 14:52
 * @Version 1.0
 */
public class DecimalTo62Utils {

    private static char[] chars;

    public static String decimalTo62(long num) {
        if (Objects.isNull(chars)) {
            initChars();
        }
        if (num == 0) {
            return String.valueOf(0);
        }
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int remainder = (int) (num % 62);
            sb.append(chars[remainder]);
            num = num / 62;
        }
        return sb.reverse().toString();
    }

    private static void initChars() {
        int index = 0;
        chars = new char[62];
        for (int i = '0'; i <= '9'; i++) {
            chars[index++] = (char) i;
        }

        for (int i = 'a'; i <= 'z'; i++) {
            chars[index++] = (char) i;
        }

        for (int i = 'A'; i <= 'Z'; i++) {
            chars[index++] = (char) i;
        }
    }
}
