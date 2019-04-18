package com.mobvista.okr.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.NumberFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;
    private static final String PATTERN_DATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private RandomUtil() {
    }

    /**
     * Generate a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generate a captcha code
     *
     * @return
     */
    public static String generateCaptcha() {
        return RandomStringUtils.randomNumeric(4);
    }

    /**
     * Generate a fix code
     *
     * @param id
     * @param fixLength
     * @return
     */
    public static String generateFixCode(Long id, int fixLength, int randomLength) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(fixLength);
        formatter.setGroupingUsed(false);
        if (randomLength > 0) {
            return formatter.format(id) + RandomStringUtils.randomNumeric(randomLength);
        } else {
            return formatter.format(id);
        }
    }

    /**
     * Generate a filename
     *
     * @return the generated filename
     */
    public static String generateRandomFileName() {
        return DateFormatUtils.format(new Date(), PATTERN_DATE_YYYYMMDDHHMMSS) + RandomStringUtils.randomNumeric(6);
    }

    private static final String[] CHARS = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    /**
     * 经测试，在生成一千万个数据也没有出现重复，完全满足大部分需求。
     *
     * @return
     */
    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(CHARS[x % 0x3E]);
        }
        return shortBuffer.toString();

    }
}
