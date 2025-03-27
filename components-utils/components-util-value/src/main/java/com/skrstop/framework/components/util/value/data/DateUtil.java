package com.skrstop.framework.components.util.value.data;

import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUnit;
import com.skrstop.framework.components.util.constant.DateFormatConst;
import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author 蒋时华
 * @description: 时间处理工具类
 * @date: 2018/10/16
 */
@UtilityClass
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 格式化当前时间
     *
     * @param format
     * @return
     */
    public static String now(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 格式化指定时间
     *
     * @param format
     * @param timestamp
     * @return
     */
    public static String format(Timestamp timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timestamp);
    }

    /**
     * 格式化指定时间戳
     *
     * @param format
     * @param timestamp
     * @return
     */
    public static String format(Long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Timestamp(timestamp));
    }

    /**
     * 将字符串时间转换为指定格式的Date
     *
     * @param time   时间字符串
     * @param format 格式
     * @return
     * @Description: 将 String 按照固定格式转换为 DateTime
     */
    public static Date format(String time, String format) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDatetime = LocalDateTime.parse(time, dateTimeFormatter);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = localDatetime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将字符串时间转换为指定格式的LocalDateTime
     *
     * @param time
     * @param format
     * @return
     */
    public static LocalDateTime formatLocalDateTime(String time, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }


    /**
     * 时间比较
     *
     * @param date1
     * @param date2
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(String date1, String date2) {
        return compareDate(date1, date2, DateFormatConst.NORM_DATETIME_PATTERN);
    }

    /**
     * 时间比较
     *
     * @param date1
     * @param date2
     * @param format 格式
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(String date1, String date2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            return compareDate(dt1, dt2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 时间比较
     *
     * @param date1
     * @param date2
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else if (date1.getTime() < date2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 时间比较
     *
     * @param date1
     * @param date2
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(LocalDateTime date1, LocalDateTime date2) {
        long date1Time = date1.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long date2Time = date2.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if (date1Time > date2Time) {
            return 1;
        } else if (date1Time < date2Time) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 判断两个日期相差的时长，只保留绝对值
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @param unit      相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
     * @return 日期差
     */
    public static long between(LocalDateTime beginDate, LocalDateTime endDate, DateUnit unit) {
        return between(beginDate, endDate, unit, true);
    }

    /**
     * 判断两个日期相差的时长
     *
     * @param beginDate 起始日期
     * @param endDate   结束日期
     * @param unit      相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
     * @param isAbs     日期间隔是否只保留绝对值正数
     * @return 日期差
     */
    public static long between(LocalDateTime beginDate, LocalDateTime endDate, DateUnit unit, boolean isAbs) {
        ZoneId zoneId = ZoneOffset.of("+8");
        ZonedDateTime beginDateTime = beginDate.atZone(zoneId);
        ZonedDateTime endDateTime = endDate.atZone(zoneId);
        return new DateBetween(Date.from(beginDateTime.toInstant()), Date.from(endDateTime.toInstant()), isAbs).between(unit);
    }

    /**
     * 把给定的时间减掉给定的分钟数
     *
     * @param date
     * @param minute
     * @return Date
     */
    public static Date plusMillis(Date date, int minute) {
        if (ObjectUtil.isNull(date)) {
            return null;
        }
        return new Date(new Date(date.getTime()).toInstant().plusMillis(minute).toEpochMilli());
    }

    /**
     * 把给定的时间减掉给定的秒数
     *
     * @param date
     * @param seconds
     * @return Date
     */
    public static Date plusSeconds(Date date, int seconds) {
        if (ObjectUtil.isNull(date)) {
            return null;
        }
        return new Date(new Date(date.getTime()).toInstant().plusSeconds(seconds).toEpochMilli());
    }

    /**
     * 判断时间字符串是否为指定格式
     *
     * @param dateTimeStr 时间字符串
     * @param format      格式
     * @return boolean
     */
    public static boolean isValidDefaultFormat(String dateTimeStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.parse(dateTimeStr);
            return true;
        } catch (Exception e) {
            // 如果抛出异常，说明格式不正确
            return false;
        }
    }

    public static Long getTimeSecond(LocalDateTime localDateTime) {
        if (ObjectUtil.isNull(localDateTime)) {
            return null;
        }
        return localDateTime.toEpochSecond(ZoneOffset.of("+8"));
    }

    public static Long getTimeMilli(LocalDateTime localDateTime) {
        if (ObjectUtil.isNull(localDateTime)) {
            return null;
        }
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        if (ObjectUtil.isNull(localDateTime)) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneOffset.of("+8")).toInstant());

    }

}
