package com.jcloud.spark.plugin.cdnLog.dc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by chenpei1 on 2017/5/11.
 */
public class DataUtil {

    public static final String getMonthId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getDayId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getHourId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    public static final String getMinuteId() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String todayStr = df.format(new Date());
        return todayStr;
    }

    /**
     * 取当前时间的前amount天的日期
     * @param amount
     * @return String
     * @throws ParseException
     * @author Chen Pei
     * @Date: 2015-06-18
     */
    public static String getAmountDate(int amount) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, amount);
        Date monday = calendar.getTime();
        String preMonday = simpleDateFormat.format(monday);
        return preMonday;
    }

    /**
     * 取当前时间的前amount的小时
     * @param amount
     * @return String
     * @throws ParseException
     * @author Chen Pei
     * @Date: 2015-06-18
     */
    public static String getAmountHour(int amount) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, amount);
        Date monHour = calendar.getTime();
        String preMonHour = sdf.format(monHour);
        return preMonHour;
    }

    /**
     * 取当前时间的前amount的秒
     * @param amount
     * @return String
     * @throws ParseException
     * @author Chen Pei
     * @Date: 2015-06-18
     */
    public static String getAmountMinute(int amount) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, amount);
        Date monMinute = calendar.getTime();
        String preMonMinute = simpleDateFormat.format(monMinute);
        return preMonMinute;
    }

    /**
     * 取当前时间的前amount的秒
     * @param amount
     * @return String
     * @throws ParseException
     * @author Chen Pei
     * @Date: 2015-06-18
     */
    public static String getAmountSecond(int amount) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, amount);
        Date monSecond = calendar.getTime();
        String preMonSecond = simpleDateFormat.format(monSecond);
        return preMonSecond;
    }

    /**
     * 根据dayId获取每小时
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDayHour(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYYMMddHH");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.HOUR, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

    /**
     * 根据dayId获取每分钟
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDayMinute(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("YYYYMMddHHmm");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.MINUTE, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

    /**
     * 根据dayId获取每秒
     * @param dayId
     * @return
     * @throws ParseException
     */
    public static List<String> getDaySecond(String dayId) throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = simpleDateFormat.parse(dayId);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        calendar2.add(Calendar.DAY_OF_MONTH, 1);
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        for (; calendar1.compareTo(calendar2) < 0; calendar1.add(Calendar.SECOND, 1)) {
            dateList.add(simpleDateFormat1.format(calendar1.getTime()));
        }
        return dateList;
    }

}
