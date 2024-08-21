package com.winning.hmap.portal.util;

import com.winning.hmap.portal.task.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class TimeTool {

    private static final Log logger = LogFactory.getLog(TimeTool.class);

    /**
     * <p>yyyy-MM-dd HH:mm:ss格式解析对象</p>
     */
    private SimpleDateFormat dateTimeFormat = null;
    /**
     * <p>yyyy-MM-dd格式解析对象</p>
     */
    private SimpleDateFormat dateFormat = null;
    /**
     * <p>HH:mm:ss格式解析对象</p>
     */
    private SimpleDateFormat timeFormat = null;
    /**
     * <p>yyyy-MM-dd HH:mm:ss模式</p>
     */
    private static final int TYPE_DATE_TIME = 1;
    /**
     * <p>yyyy-MM-dd模式</p>
     */
    private static final int TYPE_DATE = 2;
    /**
     * <p>HH:mm:ss模式</p>
     */
    private static final int TYPE_TIME = 3;

    private static final String errPreFix = "com.wondertek.contract.util.TimeTool.";

    /**
     * <p>格式搜索的解析位置对象，也可以查询解析解析错误信息，-1表示正确</p>
     */
    private static ParsePosition parsePos = null;

    /**
     * 获取 当前年、月、日、 时  分 秒开始时间
     */
    private static final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat millisecondSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * <p>初始化</p>
     *
     * @param type 初始化类型
     */
    private TimeTool(int type) {
        if (type == TYPE_DATE_TIME) {
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (type == TYPE_DATE) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if (type == TYPE_TIME) {
            timeFormat = new SimpleDateFormat("HH:mm:ss");
        } else {
            throw new IllegalArgumentException(errPreFix + "type error:type = "
                    + type);
        }
        parsePos = new ParsePosition(0);
    }

    /**
     * 判断第一个年月是否小于第二个年月
     *
     * @param yearMonth01 第一个年月
     * @param yearMonth02 第二个年月
     * @return true or false
     */
    public static boolean before(int[] yearMonth01, int[] yearMonth02) {
        return yearMonth01[0] < yearMonth02[0]
                || yearMonth01[0] == yearMonth02[0] && yearMonth01[1] < yearMonth02[1];
    }

    /**
     * 判断第一个年月是否大于第二个年月
     *
     * @param yearMonth01 第一个年月
     * @param yearMonth02 第二个年月
     * @return true or false
     */
    public static boolean after(int[] yearMonth01, int[] yearMonth02) {
        return yearMonth01[0] > yearMonth02[0]
                || yearMonth01[0] == yearMonth02[0] && yearMonth01[1] > yearMonth02[1];
    }

    /**
     * <p>得到当前时间，“yyyy-MM-dd HH:mm:ss”</p>
     *
     * @return 当前时间
     */
    public static String getDateTime() {
        return dateToStrDateTime(new Date());
    }

    /**
     * <p>得到当前时间，“yyyy-MM-dd”</p>
     *
     * @return 当前时间
     */
    public static String getDate() {
        return dateToStrDate(new Date());
    }

    /**
     * <p>得到当前时间，“HH:mm:ss”</p>
     *
     * @return 当前时间
     */
    public static String getTime() {
        return dateToStrTime(new Date());
    }

    /**
     * <p>“yyyy-MM-dd”时间格式转换成Date格式</p>
     *
     * @param str “yyyy-MM-dd”时间格式
     * @return 处理后的Date时间格式
     */
    public static Date strDateToDate(String str) {
        if (str == null || str.trim().length() == 0) {
            return null;
        }
        if (str.length() == 7) {
            str += "-01 00:00:00";
        }
        return new TimeTool(TYPE_DATE).dateFormat.parse(str, parsePos);
    }

    /**
     * <p>Date转换成“yyyy-MM-dd HH:mm:ss”时间格式</p>
     *
     * @param date Date格式的时间
     * @return “yyyy-MM-dd HH:mm:ss”时间格式
     */
    public static String dateToStrDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return new TimeTool(TYPE_DATE_TIME).dateTimeFormat.format(date);
    }

    /**
     * <p>Date转换成“yyyy-MM-dd”时间格式</p>
     *
     * @param date Date格式的时间
     * @return “yyyy-MM-dd”时间格式
     */
    public static String dateToStrDate(Date date) {
        if (date == null) {
            return null;
        }
        return new TimeTool(TYPE_DATE).dateFormat.format(date);
    }

    /**
     * <p>Date转换成“HH:mm:ss”时间格式</p>
     *
     * @param date Date格式的时间
     * @return “HH:mm:ss”时间格式
     */
    public static String dateToStrTime(Date date) {
        if (date == null) {
            return null;
        }
        return new TimeTool(TYPE_TIME).timeFormat.format(date);
    }

    /**
     * 返回。输入日期+dayCount 输入负数，就是减少天数
     *
     * @param inputDate
     * @return
     */
    public static String getDateIncDayCount(String inputDate, int dayCount) {
        Date date = strDateToDate(inputDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, dayCount);
        return dateToStrDate(c.getTime());

    }

    /**
     * 返回。输入日期+yearCount 输入负数，就是减少年数
     *
     * @param inputDate
     * @return
     */
    public static String getDateIncYearCount(String inputDate, int yearCount) {
        Date date = strDateToDate(inputDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, yearCount);
        return dateToStrDate(c.getTime());
    }

    public static int getDayCountBetweenTwoData(String beginData, String endData) throws ParseException {
        SimpleDateFormat benginDateSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = benginDateSdf.parse(beginData);
        //写两个是为了避免线程安全问题
        SimpleDateFormat endDateSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = endDateSdf.parse(endData);
        long dayCount = (endDate.getTime() - beginDate.getTime()) / (1000 * 3600 * 24);
        return (int) dayCount;
    }

    /**
     * 获得本天的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        try {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 获得本天的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentDayEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        try {
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(longSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 获得昨天的开始时间，即2011-12-31 00:00:00
     *
     * @return
     */
    public static Date getCurrentYesterDayStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        try {
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 获得昨天的结束时间，即2011-012-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentYesterDayEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        try {
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(longSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }


    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 00);
            c.set(Calendar.MINUTE, 00);
            c.set(Calendar.SECOND, 00);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(longSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 当前年的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            c.set(Calendar.HOUR_OF_DAY, 00);
            c.set(Calendar.MINUTE, 00);
            c.set(Calendar.SECOND, 00);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常信息", e);
        }
        return now;
    }

    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * <p>得到当前时间，“yyyy-MM-dd HH:mm:ss:SSS”</p>
     *
     * @return 当前时间
     */
    public static String getMillisecondTime() {
        return millisecondSdf.format(new Date());
    }

    /**
     * 比较两个日期不能为空，且前面日期不得大于后面日期
     *
     * @throws ParseException
     */

    public static String verifyDate(String shrq_from, String shrq_info) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != shrq_from && null != shrq_info) {
            // 开始时间
            Date kssj = sdf.parse(shrq_from);
            // 结束时间
            Date sjsj = sdf.parse(shrq_info);
            if (kssj.getTime() > sjsj.getTime()) {
                return Constant.KSSJBNDYJSSJ;
            }
        } else if (StringUtils.isBlank(shrq_from) && StringUtils.isNotBlank(shrq_info)) {
            return Constant.KSSJBNWK;
        } else if (StringUtils.isBlank(shrq_info) && StringUtils.isNotBlank(shrq_from)) {
            return Constant.JSSJBNWK;
        } else if (StringUtils.isBlank(shrq_info) && StringUtils.isBlank(shrq_from)) {
            return Constant.KSJSSJBNWK;
        }
        return null;
    }

    public static String checkDate(String start, String end, DateTimeFormatter sdf) {
        if (StringUtils.isNotBlank(end) && StringUtils.isNotBlank(start)) {
            LocalDate startDate = LocalDate.parse(start, sdf);
            LocalDate endDate = LocalDate.parse(end, sdf);
            if (endDate.isBefore(startDate)) {
                return Constant.KSSJBNDYJSSJ;
            }
        } else if (StringUtils.isBlank(start) && StringUtils.isNotBlank(end)) {
            return Constant.KSSJBNWK;
        } else if (StringUtils.isBlank(end) && StringUtils.isNotBlank(start)) {
            return Constant.JSSJBNWK;
        } else if (StringUtils.isBlank(end) && StringUtils.isBlank(start)) {
            return Constant.KSJSSJBNWK;
        }
        return null;
    }

    public static String checkDate(String start, String end) {
        return checkDate(start, end, DEFAULT_FORMATTER);
    }

    /**
     * 得到本年指定月份的开始时间，即2018-06-01 00:00:00
     */

    public static Date getCurrentYearAppointMonthStartTime(String month) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            now = longSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }


    /**
     * 得到本年指定月份的结束时间，即2018-06-31 23:59:59
     */
    public static Date getCurrentYearAppointMonthEndTime(String month) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            int actualMaximum = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DAY_OF_MONTH, actualMaximum);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 得到去年指定月份的开始时间，即2017-06-01 00:00:00
     */
    public static Date getLastYearAppointMonthStartTime(String month) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.add(Calendar.YEAR, -1);
            c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            now = longSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 得到去年指定月份的结束时间，即2018-06-31 23:59:59
     */
    public static Date getLastYearAppointMonthEndTime(String month) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.add(Calendar.YEAR, -1);
            c.set(Calendar.MONTH, Integer.parseInt(month) - 1);
            int actualMaximum = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            c.set(Calendar.DAY_OF_MONTH, actualMaximum);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            now = longSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**

     * 获取当前季度的时间范围

     * @return current quarter

     */

//    public static DateRange getThisQuarter() {
//        Calendar startCalendar = Calendar.getInstance();
//
//        startCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3) * 3);
//
//        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
//
//        setMinTime(startCalendar);
//
//        Calendar endCalendar = Calendar.getInstance();
//
//        endCalendar.set(Calendar.MONTH, ((int) startCalendar.get(Calendar.MONTH) / 3) * 3 + 2);
//
//        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//
//        setMaxTime(endCalendar);
//
//        return new DateRange(startCalendar.getTime(), endCalendar.getTime());
//
//    }


    private static void setMinTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        calendar.set(Calendar.MINUTE, 0);

        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.MILLISECOND, 0);

    }
    private static void setMaxTime(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));

        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));

        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));

        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

    }

    public final static String DEFAULT_PATTERN = "MM/dd/yyyy HH:mm:ss";

    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);

        return sdf.format(date);

    }

    /**
     * 获得整小时
     *
     * @return
     */
    public static Date getCurrentStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        try {
            c.set(Calendar.DATE, c.get(Calendar.DATE));
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.HOUR_OF_DAY,-1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            logger.error("异常", e);
        }
        return now;
    }

    /**
     * 返回。输入日期+hourCount
     *
     * @param date
     * @return
     */
    public static Date getDateIncHourCount(Date date, int hourCount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hourCount);
        return c.getTime();

    }

}