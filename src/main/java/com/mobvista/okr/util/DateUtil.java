package com.mobvista.okr.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author Weier Gu (顾炜)
 */
public class DateUtil {

    public final static String Y_M_D = "yyyy-MM-dd";
    public final static String FULL_Y_M_D = "yyyy-MM-dd HH:mm:ss";
    public final static String yyyyMMdd = "yyyyMMdd";

    /**
     * 格式化日期，默认返回：yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return toString(date, FULL_Y_M_D);
    }

    /**
     * 格式化日期，默认返回：yyyyMMddHHmmss 格式
     *
     * @param date
     * @return
     */
    public static String toStringYyyyMMdd(Date date) {
        return toString(date, yyyyMMdd);
    }

    /**
     * 格式化日期，默认返回：yyyy-MM-dd HH:mm:ss 格式
     *
     * @param date
     * @return
     */
    public static String toStringYMD(Date date) {
        return toString(date, Y_M_D);
    }

    public static Date endDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(FULL_Y_M_D);
        try {
            Date newDate = dateFormat.parse(addHmsEnd(DateFormatUtils.format(date, Y_M_D)));
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 格式化时间yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static Date formatYMD(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(Y_M_D);
        try {
            Date newDate = formatter.parse(toString(date));
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 是否是当天
     *
     * @param date
     * @return
     */
    public static boolean isTodayDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(Y_M_D);
        String dateStr = formatter.format(date);
        return dateStr.equals(today());
    }


    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    /**
     * 增加时分秒
     *
     * @param date
     * @return
     */
    public static String addHmsStart(String date) {
        return date + " 00:00:00";
    }

    /**
     * 增加时分秒
     *
     * @param date
     * @return
     */
    public static String addHmsEnd(String date) {
        return date + " 23:59:59";
    }

    /**
     * 转成成日期，模式使用："yyyy-MM-dd HH:mm:ss" 格式
     *
     * @param dateStr
     * @return
     */
    public static Date toDate(String dateStr) {
        return toDate(dateStr, FULL_Y_M_D);
    }

    /**
     * 把日期字符串转换成日期
     *
     * @param dateStr 日期字符串
     * @param pattern
     * @return
     */
    public static Date toDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }
        if (pattern == null) {
            pattern = FULL_Y_M_D;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern);
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date nowDate() {
        return new Date();
    }

    /**
     * 获取当前时间
     */
    public static String now() {
        return toString(new Date(), FULL_Y_M_D);
    }

    /**
     * 获取当前时间
     */
    public static String today() {
        return toString(new Date(), Y_M_D);
    }

    /**
     * 设置日期，时分秒为0
     *
     * @param date
     * @return
     */
    public static Date setDateStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * 设置日期，时分秒为：23：59：59
     *
     * @param date
     * @return
     */
    public static Date setDateEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }


    /**
     * 设置日期，时分秒为：23：59：59
     *
     * @param date
     * @return
     */
    public static Date setDateEnd(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 增加日期
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 获取当前时间
     */
    public static Long nowMillis() {
        return System.currentTimeMillis();
    }


    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            if (str.indexOf(":") > 0) {
                format1.setLenient(false);
                format1.parse(str);
            } else {
                format2.setLenient(false);
                format2.parse(str);
            }
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static String getFormatStrDate(String str) {
        if (isValidDate(str)) {
            if (str.indexOf(":") <= 0) {
                return str + " 00:00:00";
            }
        } else {
            return str;
        }
        return "";
    }


    /**
     * 毫秒级时间，解析为字符串
     * 日志打印使用
     *
     * @param milliseconds
     * @return
     */
    public static String getLogTimeByMilliseconds(long milliseconds) {
        long time = milliseconds / 1000;
        long minute = time / 60;
        long seconds = time % 60;
        long millis = milliseconds % 1000;
        return String.format("%s 分 %s 秒 %s 毫秒", minute, seconds, millis);
    }
}
