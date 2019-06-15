package cn.cdjzxy.android.monitoringassistant.utils.rx;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool.stringToInt;


public class RxDateTool {
    //Date格式
    public static final String DATE_FORMAT_LINK = "yyyyMMddHHmmssSSS";

    //Date格式 常用
    public static final String DATE_FORMAT_DETACH = "yyyy-MM-dd HH:mm:ss";

    //Date格式 常用  年月日
    public static String DATE_FORMAT = "yyyy-MM-dd";
    //Date格式 常用  年月
    public static String DATE_YYYY_MMM = "yyyy-MM";
    //Date格式 带毫秒
    public static final String DATE_FORMAT_DETACH_SSS = "yyyy-MM-dd HH:mm:ss SSS";

    //Date格式 年月日时分
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    //时间格式 分钟：秒钟 一般用于视频时间显示
    public static final String DATE_FORMAT_MM_SS = "mm:ss";

    //时间格式 时分秒
    public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";

    //时间格式 时分
    public static final String DATE_FORMAT_HH_MM = "HH:mm";

    public static final SimpleDateFormat DEFAULT_SDF =
            new SimpleDateFormat(DATE_FORMAT_DETACH, Locale.getDefault());

    public enum TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }

    /**
     * 毫秒与毫秒的倍数
     */
    public static final int MSEC = 1;

    /******************** 时间相关常量 ********************/
    /**
     * 秒与毫秒的倍数
     */
    public static final int SEC = 1000;
    /**
     * 分与毫秒的倍数
     */
    public static final int MIN = 60000;
    /**
     * 时与毫秒的倍数
     */
    public static final int HOUR = 3600000;
    /**
     * 天与毫秒的倍数
     */
    public static final int DAY = 86400000;

    /**
     * 根据日期判断星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getAstro(int month, int day) {
        String[] starArr = {"魔羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座"};
        int[] DayArr = {22, 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22};  // 两个星座分割日

        if (month <= 0 || day <= 0) {
            return "猴年马月座";
        } else if (month > 12 || day > 31) {
            return "猴年马月座";
        }

        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < DayArr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return starArr[index % 12];
    }

    /**
     * 年份判断生肖
     *
     * @param year
     * @return
     */
    public static String getAnimalYearName(int year) {//---------计算生肖方法-------------
        String[] animalYear = new String[]{"猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"};
        String name = animalYear[(year) % 12];
        return name;
    }


    /**
     * 将时间戳转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param milliseconds 毫秒时间戳
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds) {
        return milliseconds2String(milliseconds, DEFAULT_SDF);
    }

    /**
     * 将时间戳转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param milliseconds 毫秒时间戳
     * @param format       时间格式
     * @return 时间字符串
     */
    public static String milliseconds2String(long milliseconds, SimpleDateFormat format) {
        return format.format(new Date(milliseconds));
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time) {
        return string2Milliseconds(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Milliseconds(String time, SimpleDateFormat format) {
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return Date类型
     */
    public static Date string2Date(String time) {
        return string2Date(time, DEFAULT_SDF);
    }

    /**
     * 将时间字符串转为Date类型
     * <p>格式为用户自定义</p>
     *
     * @param time   时间字符串
     * @param format 时间格式
     * @return Date类型
     */
    public static Date string2Date(String time, SimpleDateFormat format) {
        return new Date(string2Milliseconds(time, format));
    }


    /**
     * 将Date类型转为时间字符串
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time Date类型时间
     * @return 时间字符串
     */
    public static String date2String(Date time) {
        return date2String(time, DEFAULT_SDF);
    }

    /**
     * 将Date类型转为时间字符串
     * <p>格式为用户自定义</p>
     *
     * @param time   Date类型时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String date2String(Date time, SimpleDateFormat format) {
        return format.format(time);
    }

    /**
     * 将Date类型转为时间戳
     *
     * @param time Date类型时间
     * @return 毫秒时间戳
     */
    public static long date2Milliseconds(Date time) {
        return time.getTime();
    }

    /**
     * 将时间戳转为Date类型
     *
     * @param milliseconds 毫秒时间戳
     * @return Date类型时间
     */
    public static Date milliseconds2Date(long milliseconds) {
        return new Date(milliseconds);
    }

    /**
     * 毫秒时间戳单位转换（单位：unit）
     *
     * @param milliseconds 毫秒时间戳
     * @param unit         <ul>
     *                     <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *                     <li>{@link TimeUnit#SEC }: 秒</li>
     *                     <li>{@link TimeUnit#MIN }: 分</li>
     *                     <li>{@link TimeUnit#HOUR}: 小时</li>
     *                     <li>{@link TimeUnit#DAY }: 天</li>
     *                     </ul>
     * @return unit时间戳
     */
    private static long milliseconds2Unit(long milliseconds, TimeUnit unit) {
        switch (unit) {
            case MSEC:
                return milliseconds / MSEC;
            case SEC:
                return milliseconds / SEC;
            case MIN:
                return milliseconds / MIN;
            case HOUR:
                return milliseconds / HOUR;
            case DAY:
                return milliseconds / DAY;
            default:
                break;
        }
        return -1;
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2格式都为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time0 时间字符串1
     * @param time1 时间字符串2
     * @param unit  <ul>
     *              <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *              <li>{@link TimeUnit#SEC }: 秒</li>
     *              <li>{@link TimeUnit#MIN }: 分</li>
     *              <li>{@link TimeUnit#HOUR}: 小时</li>
     *              <li>{@link TimeUnit#DAY }: 天</li>
     *              </ul>
     * @return unit时间戳
     */
    public static long getIntervalTime(String time0, String time1, TimeUnit unit) {
        return getIntervalTime(time0, time1, unit, DEFAULT_SDF);
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2格式都为format</p>
     *
     * @param time0  时间字符串1
     * @param time1  时间字符串2
     * @param unit   <ul>
     *               <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *               <li>{@link TimeUnit#SEC }: 秒</li>
     *               <li>{@link TimeUnit#MIN }: 分</li>
     *               <li>{@link TimeUnit#HOUR}: 小时</li>
     *               <li>{@link TimeUnit#DAY }: 天</li>
     *               </ul>
     * @param format 时间格式
     * @return unit时间戳
     */
    public static long getIntervalTime(String time0, String time1, TimeUnit unit, SimpleDateFormat format) {
        return Math.abs(milliseconds2Unit(string2Milliseconds(time0, format)
                - string2Milliseconds(time1, format), unit));
    }

    /**
     * 获取两个时间差（单位：unit）
     * <p>time1和time2都为Date类型</p>
     *
     * @param time1 Date类型时间1
     * @param time2 Date类型时间2
     * @param unit  <ul>
     *              <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *              <li>{@link TimeUnit#SEC }: 秒</li>
     *              <li>{@link TimeUnit#MIN }: 分</li>
     *              <li>{@link TimeUnit#HOUR}: 小时</li>
     *              <li>{@link TimeUnit#DAY }: 天</li>
     *              </ul>
     * @return unit时间戳
     */
    public static long getIntervalTime(Date time1, Date time2, TimeUnit unit) {
        return Math.abs(milliseconds2Unit(date2Milliseconds(time2)
                - date2Milliseconds(time1), unit));
    }

    /**
     * 获取当前时间
     *
     * @return 毫秒时间戳
     */
    public static long getCurTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @return 时间字符串
     */
    public static String getCurTimeString() {
        return date2String(new Date());
    }

    /**
     * 获取当前时间
     * <p>格式为用户自定义</p>
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getCurTimeString(SimpleDateFormat format) {
        return date2String(new Date(), format);
    }

    /**
     * 获取当前时间
     * <p>格式为用户自定义</p>
     *
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String getCurTimeString(String format) {
        return date2String(new Date(), new SimpleDateFormat(format));
    }

    /**
     * 获取当前时间
     * <p>Date类型</p>
     *
     * @return Date类型时间
     */
    public static Date getCurTimeDate() {
        return new Date();
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @param unit <ul>
     *             <li>{@link TimeUnit#MSEC}:毫秒</li>
     *             <li>{@link TimeUnit#SEC }:秒</li>
     *             <li>{@link TimeUnit#MIN }:分</li>
     *             <li>{@link TimeUnit#HOUR}:小时</li>
     *             <li>{@link TimeUnit#DAY }:天</li>
     *             </ul>
     * @return unit时间戳
     */
    public static long getIntervalByNow(String time, TimeUnit unit) {
        return getIntervalByNow(time, unit, DEFAULT_SDF);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time格式为format</p>
     *
     * @param time   时间字符串
     * @param unit   <ul>
     *               <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *               <li>{@link TimeUnit#SEC }: 秒</li>
     *               <li>{@link TimeUnit#MIN }: 分</li>
     *               <li>{@link TimeUnit#HOUR}: 小时</li>
     *               <li>{@link TimeUnit#DAY }: 天</li>
     *               </ul>
     * @param format 时间格式
     * @return unit时间戳
     */
    public static long getIntervalByNow(String time, TimeUnit unit, SimpleDateFormat format) {
        return getIntervalTime(getCurTimeString(), time, unit, format);
    }

    /**
     * 获取与当前时间的差（单位：unit）
     * <p>time为Date类型</p>
     *
     * @param time Date类型时间
     * @param unit <ul>
     *             <li>{@link TimeUnit#MSEC}: 毫秒</li>
     *             <li>{@link TimeUnit#SEC }: 秒</li>
     *             <li>{@link TimeUnit#MIN }: 分</li>
     *             <li>{@link TimeUnit#HOUR}: 小时</li>
     *             <li>{@link TimeUnit#DAY }: 天</li>
     *             </ul>
     * @return unit时间戳
     */
    public static long getIntervalByNow(Date time, TimeUnit unit) {
        return getIntervalTime(getCurTimeDate(), time, unit);
    }

    /**
     * 判断闰年
     *
     * @param year 年份
     * @return {@code true}: 闰年<br>
     * {@code false}: 平年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 将date转换成format格式的日期
     *
     * @param format 格式
     * @param date   日期
     * @return
     */
    public static String simpleDateFormat(String format, Date date) {
        if (RxDataTool.isNullString(format)) {
            format = DATE_FORMAT_DETACH;
        }
        String content = new SimpleDateFormat(format).format(date);
        return content;
    }


    /**
     * 将制定DATE_FORMAT_DETACH格式时间date转化为想要的时间格式nowFormat
     * 只能将长时间转化为短时间
     *
     * @param date      日期
     * @param nowFormat 期望的时间格式
     * @return
     */
    public static String simpleDateFormat(String date, String nowFormat) {
        if (RxDataTool.isEmpty(date)) return "";
        try {
            Date simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_DETACH).parse(date);
            return getDate(nowFormat, simpleDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }
    //--------------------------------------------字符串转换成时间戳-----------------------------------

    /**
     * 将指定格式的日期转换成时间戳
     *
     * @param mDate
     * @return
     */
    public static String Date2Timestamp(Date mDate) {
        return String.valueOf(mDate.getTime()).substring(0, 10);
    }

    /**
     * 将日期字符串 按照 指定的格式 转换成 DATE
     * 转换失败时 return null;
     *
     * @param format
     * @param datess
     * @return
     */
    public static Date string2Date(String format, String datess) {
        SimpleDateFormat sdr = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdr.parse(datess);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将 yyyy年MM月dd日 转换成 时间戳
     *
     * @param format
     * @param datess
     * @return
     */
    public static String string2Timestamp(String format, String datess) {
        Date date = string2Date(format, datess);
        return Date2Timestamp(date);
    }
    //===========================================字符串转换成时间戳====================================

    /**
     * 获取当前日期时间 / 得到今天的日期
     * str yyyyMMddhhMMss 之类的
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateTime(String format) {
        return simpleDateFormat(format, new Date());
    }

    /**
     * 将指定为：时间为年月日时分秒的字符串 转化为想要的字符串
     *
     * @param date   时间
     * @param format 想要的格式
     * @return
     */
    public static String getDataNewFormat(String format, String date) {
        if (RxDataTool.isEmpty(date)) return "";
        try {
            Date simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_DETACH).parse(date);
            return getDate(format, simpleDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 将指定格式字符串的时间转化为自己想要的字符串时间
     *
     * @param date      时间字符串
     * @param oldFormat 指定的格式
     * @param newFormat 想要的格式
     * @return 返回想要的格式时间
     */
    public static String getDate(String date, String oldFormat, String newFormat) {
        if (RxDataTool.isEmpty(date)) return "";
        try {
            Date simpleDateFormat = new SimpleDateFormat(oldFormat).parse(date);
            return getDate(newFormat, simpleDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
    }

    /**
     * 获取当前年月日
     *
     * @param format z指定格式
     * @return 当前年月日
     */
    public static String getDate(String format) {
        return getDate(format, new Date());
    }


    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDate() {
        return getDate(new Date());
    }

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDateYYYY_MM_DD() {
        return getDate(DATE_FORMAT);
    }

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDateYYYY_MM_DD(Date date) {
        return getDate(DATE_FORMAT, date);
    }

    /**
     * 获取当前时间
     *
     * @return 当前年月日时分秒
     */
    public static String getDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_DETACH).format(date);
    }

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDate(String format, Date date) {
        if (RxDataTool.isEmpty(format))
            format = DATE_FORMAT_DETACH;
        try {
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            return new SimpleDateFormat(DATE_FORMAT_DETACH).format(date);
        }
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static String getWeek() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 得到昨天的日期
     *
     * @param format 日期格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getYestoryDate(String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return simpleDateFormat(format, calendar.getTime());
    }

    /**
     * 视频时间 转换成 "mm:ss"
     *
     * @param milliseconds
     * @return
     */
    public static String formatTime(long milliseconds) {
        String format = DATE_FORMAT_MM_SS;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        String video_time = sdf.format(milliseconds);
        return video_time;
    }

    /**
     * "mm:ss" 转换成 视频时间
     *
     * @param time
     * @return
     */
    public static long formatSeconds(String time) {
        String format = DATE_FORMAT_MM_SS;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date;
        long times = 0;
        try {
            date = sdf.parse(time);
            long l = date.getTime();
            times = l;
            Log.d("时间戳", times + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param strDate 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常<br>
     */
    public static int stringForWeek(String strDate) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(strDate));
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return 7;
        } else {
            return c.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }

    /**
     * 判断当前日期是星期几
     *
     * @param strDate 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常<br>
     */
    public static int stringForWeek(String strDate, SimpleDateFormat simpleDateFormat) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(simpleDateFormat.parse(strDate));
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return 7;
        } else {
            return c.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }

    //判断结束时间大于起始时间
    public static Boolean compareTime(String startTime, String endTime) {
        long longstr1 = Long.valueOf(startTime.replaceAll("[-\\s:]", ""));
        long longstr2 = Long.valueOf(endTime.replaceAll("[-\\s:]", ""));
        return longstr1 - longstr2 < 0;
    }

    //获取剩余时间
    public static int getLastDays(String startTime, String endTime) {
        int time1 = Integer.valueOf(startTime.replaceAll("[-\\s:]", ""));
        int time2 = Integer.valueOf(endTime.replaceAll("[-\\s:]", ""));
        return time2 - time1;
    }
}
