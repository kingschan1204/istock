package io.github.kingschan1204.istock.module.spider.util;

import io.github.kingschan1204.istock.common.util.cache.EhcacheUtil;
import io.github.kingschan1204.istock.module.spider.crawl.tradingdate.TradingDateSpider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.FutureTask;

/**
 * 交易时间工具类
 *
 * @author chenguoxiang
 * @create 2019-03-28 12:57
 **/
@Slf4j
@Component
public class TradingDateUtil {

    private final String CACHE_NAME = "TradingDate";
    @Autowired
    private EhcacheUtil ehcacheUtil;


    /**
     * 以20190606这种方式返回当前日期
     *
     * @return
     */
    public static String getDateYYYYMMdd() {
        LocalDate localDate = LocalDate.now();
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(localDate);
    }

    /**
     * 得到当前时间以数字形式返回  包含时分秒
     *
     * @return
     */
    public static String getDateTime() {
        LocalDateTime localDate = LocalDateTime.now();
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(localDate);
    }

    /**
     * 当前减传入参数值得到计算后的日期
     *
     * @param year              减去年份数
     * @param month             减去月份数
     * @param day               减去天数
     * @param dateFormatPattern 格式化后的日期格式 如（yyyyMMdd）
     * @return
     */
    public static String minusDate(int year, int month, int day, String dateFormatPattern) {
        DateTimeFormatter sf = DateTimeFormatter.ofPattern(dateFormatPattern);
        LocalDate localDate = LocalDate.now();
        return sf.format(localDate.minusYears(year).minusMonths(month).minusDays(day));
    }

    /**
     * 当前是否为交易时间
     *
     * @return
     */
    public boolean isTradingTimeNow() {
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String day = dtf.format(dateTime);
            if (isTradingDay(day)) {
                if (isTradingTime(dateTime.getHour(), dateTime.getMinute())) {
//                    log.info("现在是交易时间");
                    return true;
                } else {
//                    log.info("现在不是交易时间");
                    return false;
                }
            }
        } catch (Exception ex) {
            log.error("{}", ex);
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 今天是否是交易日
     *
     * @return
     * @throws Exception
     */
    public boolean isTradingDay() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String day = dtf.format(dateTime);
        try {
            return isTradingDay(day);
        } catch (Exception e) {
            log.error("{}", e);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否为交易日
     *
     * @param date 日期例如：2019-03-28
     * @return
     */
    public boolean isTradingDay(String date) throws Exception {
        if (!date.matches("\\d{4}\\-\\d{2}\\-\\d{2}")) {
            throw new Exception("传入日期格式不正确：yyyy-MM-dd");
        }
        if (ehcacheUtil.contrainKey(CACHE_NAME, date)) {
            return (boolean) ehcacheUtil.getKey(CACHE_NAME, date);
        } else {
            String month = date.substring(0, 7);
            TradingDateSpider spider = new TradingDateSpider(month);
            FutureTask<Map<String, Boolean>> futureTask = new FutureTask<Map<String, Boolean>>(spider);
            new Thread(futureTask).start();
            Map<String, Boolean> map = futureTask.get();
            map.keySet().stream().forEach(key -> {
                ehcacheUtil.addKey(CACHE_NAME, key, map.get(key));
            });
            return map.get(date);
        }
    }

    /**
     * 是否为交易日间 9：30 ~ 11：30 13：00 ~ 15：00
     *
     * @param hour
     * @param minute
     * @return
     */
    public boolean isTradingTime(int hour, int minute) {
        if (hour >= 9 && hour <= 11) {
            if (hour == 9 && minute < 30) {
                //未到9点半
                return false;
            } else if (hour == 11 && minute > 30) {
                //超过11点半
                return false;
            } else {
                return true;
            }
        } else if (hour >= 13 && hour <= 15) {
            if (hour == 15 && minute >= 1) {
                return false;
            }
            //下午1点到3点
            return true;
        }
        return false;
    }

}
