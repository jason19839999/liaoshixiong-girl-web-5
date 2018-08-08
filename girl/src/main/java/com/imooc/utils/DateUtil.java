package com.imooc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 返回当前日期Date
     */
    public static Date getNow() {
        Date today = new Date();
        return today;
    }

    /**
     * 返回预设Format的当前日期字符串
     */
    public static String getNowStr() {
        Date today = new Date();
        return format(today);
    }


    /**
     * 使用预设Format格式化Date成字符串
     */
    public static String format(Date date) {
        return date == null ? " " : format(date, getDatePattern());
    }

    /**
     * 使用预设格式将字符串转化成预设格式字符串
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String format(String date) throws ParseException {
        return format(parse(date));
    }

    /**
     * 使用参数Format格式化Date成字符串
     */
    public static String format(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 使用参数Format将String格式化成对应格式的字符串
     */
    public static String format(String date, String pattern) throws ParseException {
        return format(parse(date, pattern), pattern);
    }

    /**
     * 将时间戳格式化Date成字符串
     */
    public static Date parse(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String strDate) throws ParseException {
        return (strDate == null || strDate.length() == 0) ? null : parse(strDate,
                getDatePattern());
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String pattern)
            throws ParseException {
        return (strDate == null || strDate.length() == 0) ? null : new SimpleDateFormat(
                pattern, Locale.US).parse(strDate);
    }

    /**
     * 使用自定义时间pattern将Date转化为Date
     *
     * @param date
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(Date date, String pattern) throws ParseException {
        if (date == null) {
            return null;
        } else {
            return parse(format(date, pattern), pattern);
        }
    }

    /**
     * 使用预设时间格式将Date转化为Date
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parse(Date date) throws ParseException {
        if (date == null) {
            return null;
        } else {
            return parse(format(date));
        }
    }

    /**
     * 在日期上增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加数个整日
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加数个整小时
     */
    public static Date addHour(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, n);
        return cal.getTime();
    }


    public static Date addMinute(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, n);
        return cal.getTime();
    }

    /**
     * 返回最近的下一分钟开始时刻
     *
     * @param date
     * @return
     */
    public static Date getNextMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String getLastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        // 年
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        // 月，因为Calendar里的月是从0开始，所以要-1
        // cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        // 日，设为一号
        cal.set(Calendar.DATE, 1);
        // 月份加一，得到下个月的一号
        cal.add(Calendar.MONTH, 1);
        // 下一个月减一为本月最后一天
        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 获得月末是几号
    }


    /**
     * 当前输入日期所在星期的某一天的日期
     *
     * @param date the given date
     * @param n    nth day of week
     */
    public static Date getDayOfWeek(Date date, int n) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, n);
        return cal.getTime();
    }

    /**
     * 当前输入日期所在月份的某一天的日期
     *
     * @param date the given date
     * @param n    nth day of month
     */
    public static Date getDayOfMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }


    /**
     * 当前输入日期所在天的某个时间
     *
     * @param date the given date
     * @param n    nth day of month
     */
    public static Date getTimeOfDay(Date date, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        if (hour != -1)
            cal.set(Calendar.HOUR_OF_DAY, hour);
        if (minute != -1)
            cal.set(Calendar.MINUTE, minute);
        if (second != -1)
            cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 当前输入日期所在天的某个时间
     *
     * @param date the given date
     * @param n    nth day of month
     */
    public static Date getTimeOfDay(Date date, String hourStr, String minuteStr, String secondStr) {
        int hour = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);
        int second = Integer.parseInt(secondStr);

        return getTimeOfDay(date, hour, minute, second);
    }

    public static Date getDate(String year, String month, String day)
            throws ParseException {
        String result = year + "- "
                + (month.length() == 1 ? ("0 " + month) : month) + "- "
                + (day.length() == 1 ? ("0 " + day) : day);
        return parse(result);
    }


    /**
     * month和day都是从1开始,返回该天的开始时刻
     *
     * @param year
     * @param month
     * @param day
     * @return
     * @throws ParseException
     */
    public static Date getDate(int year, int month, int day)
            throws ParseException {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 取得日期：月
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        return month;
    }

    public static void main(String[] args) {
        Date date = DateUtil.getNow();
        System.out.println(DateUtil.format(DateUtil.getTimeOfDay(date, 23, -1, 59)));
        System.out.println(DateUtil.getTimeOfDay(date, 0, 0, 0));
    }

}
