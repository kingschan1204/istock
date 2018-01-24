package io.github.kingschan1204.istock.common.util.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kingschan on 2017/6/29.
 */
public class StockDateUtil {

    private static Logger log = LoggerFactory.getLogger(StockDateUtil.class);

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
        } else if (hour >= 13 && hour <= 15) {
            return true;
        }
        return false;

    }

    /**
     * 传入年数返回具体的每个月日期
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


}
