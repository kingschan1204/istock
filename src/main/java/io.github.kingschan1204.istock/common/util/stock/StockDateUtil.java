package io.github.kingschan1204.istock.common.util.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 代码定时更新任务
 * @author kings.chan
 * @date 2018-6-29
 */
public class StockDateUtil {

    private static Logger log = LoggerFactory.getLogger(StockDateUtil.class);

    /**
     * 得到当前系统的年月日时间
     *
     * @return YYYY-MM-DD
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(System
                .currentTimeMillis()));
    }

    /**
     * 返回当前日期星期几
     *
     * @return 1-6 星期一至星期六   0：星期日
     */
    public static int getCurrentWeekDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek - 1;
    }

    /**
     * 当前是否是开市时间
     *
     * @return true:开市 false :不开市
     */
    public static boolean stockOpenTime() {
        int week = getCurrentWeekDay();
        if (week == 0 || week == 6) {
            log.debug("星期六日不开市");
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //上午 9:30 ~ 11:30
        if (hour >= 9 && hour <= 11) {
            if (hour == 11 && minute > 30) {
                return false;
            } else if (hour == 9 && minute < 30) {
                return false;
            } else {
                return true;
            }
        } else if (hour >= 13 && hour < 15) {
            return true;
        }
        return false;

    }

    /**
     * 传入年数返回具体的每个月日期
     *
     * @param years 年数
     * @return
     */
    public static String[] getTimeLine(int years) {
        ArrayList<String> list = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        int year = c.getWeekYear();
        for (int i = 0; i < years; i++) {
            int currentYear = year - i;
            for (int j = 1; j < 13; j++) {
                String date = String.format("%s-%s-15", currentYear, j < 10 ? "0" + j : j);
                list.add(date);
            }
        }
        return list.toArray(new String[]{});
    }


    /**
     * 当前时间戳
     *
     * @return 10位的数字
     */
    public static long getCurrentTimestamp() {
        long date = System.currentTimeMillis() / 1000;
        return date;
    }


    /**
     * 输入一个时间,获取该时间的时间戳
     *
     * @param dateString 日期字符串(yyyy-MM-dd HH:mm:ss)
     * @return long类型的10位数字
     * @throws ParseException
     */
    public static long dateToUnixTime(String dateString) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse(dateString);
        // JAVA的时间戳长度是13位
        long temp = date1.getTime();
        return temp / 1000;
    }

    /**
     * 格式化unix 时间戳成日期字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param time 10位
     * @return yyyy-MM-dd HH:mm:ss的字符串
     */
    public static String formatUnixDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(
                time * 1000));
    }

    /**
     * 得到当前系统的年月日数字
     *
     * @return YYYYMMDD
     */
    public static Integer getCurrentDateNumber() {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date(System
                .currentTimeMillis()));
        return Integer.valueOf(date);
    }

    /**
     * 得到当前时间的Long数字
     * @return
     */
    public static Long getCurrentDateTimeNumber() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System
                .currentTimeMillis()));
        return Long.valueOf(date);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return String  传入年份的第一天
     */
    public static String getCurrYearFirstDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearFirst);
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return String  传入年份的最后一天
     */
    public static String getCurrYearLastDay(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast);
    }

    /**
     * 得到当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        String year = new SimpleDateFormat("yyyy").format(new Date(System
                .currentTimeMillis()));
        return Integer.parseInt(year);
    }


}
