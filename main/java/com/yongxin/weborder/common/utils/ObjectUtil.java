package com.yongxin.weborder.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectUtil
{

    public static String getString(Object obj)
    {
        return obj == null ? "" : obj.toString();
    }

    public static long getLong(Object obj)
    {
        return isNull(obj) ? 0l : Long.parseLong(obj.toString());
    }

    public static int getInt(Object obj)
    {
        if (obj instanceof Integer)
        {
            return ((Integer) obj).intValue();
        }
        return isNull(obj) ? 0 : Integer.parseInt(obj.toString());
    }

    public static double getDouble(Object obj)
    {
        return obj == null ? 0.0 : Double.parseDouble(obj.toString());
    }

    public static boolean getBoolean(Object obj)
    {
        return isNull(obj) ? false : Boolean.parseBoolean(obj.toString());
    }

    /**
     * 将时间转为格式 2014-01-02 14:11:49
     */
    public static String getTimeStampString(long time)
    {
        //2014-01-02 14:11:49.128
        String str = new Timestamp(time).toString();
        return str.substring(0, 19);
    }

    public static String getTimeStampString(Timestamp timestamp)
    {
        if (timestamp == null)
        {
            return null;
        }
        //2014-01-02 14:11:49.128
        String str = timestamp.toString();
        return str.substring(0, 19);
    }

    /**
     * 把格式如20131122格式的字串转化为时间
     * @param date 字符串日期
     * @return 时间
     */
    public static Timestamp parseTimestamp(String date,String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        try
        {
            return new Timestamp(df.parse(date).getTime());
        }
        catch (ParseException e)
        {
            return null;
        }
    }


    /**
     * 获取当前时间
     * @return
     */
    public static Timestamp getTimestampNow()
    {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return now;
    }

    /**
     * 获取差异时间
     * @param early  分钟  正数向后， 负数向前
     * @return
     */
    public static Timestamp getTimestampEarly(int early)
    {
        long timestamp = System.currentTimeMillis() + early * 60 * 1000;
        Timestamp now = new Timestamp(timestamp);
        return now;
    }

    /**
     * 获取差异时间
     * @param early  天  正数向后， 负数向前
     * @return
     */
    public static String getDay(int early)
    {
        long timestamp = System.currentTimeMillis() + early * 24 * 60 * 60 * 1000;

        //2014-01-02 14:11:49.128
        String str = new Timestamp(timestamp).toString();
        return str.substring(0, 19);
    }




    public static boolean isNull(Object param)
    {
        return param == null || "".equals(param);
    }

    public static boolean isNotNull(Object param)
    {
        return !isNull(param);
    }


    /**
     * 正则匹配
     * @param regex
     * @param str
     * @return
     */
    public static String regString(String regex, String str)
    {
        Pattern pa = Pattern.compile(regex);
        Matcher ma = pa.matcher(str);
        if (ma.find())
        {
            return ma.group();
        }
        return "";
    }

    /**
     * 休眠
     * @param sec 秒
     */
    public static void sleep(int sec)
    {
        try
        {
            Thread.sleep(sec * 1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 休眠
     * @param millis 毫秒
     */
    public static void sleepMillis(int millis)
    {
        try
        {
            Thread.sleep(millis );
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 将字符串转换成时间错
     * @param s
     * @param format
     * @return
     */
    public static long getTimeStampByString(String s,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try
        {
            date = simpleDateFormat.parse(s);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return date.getTime();
    }




    public static <E> boolean isNotEmpty(Collection<E> coll)
    {
        return coll != null && !coll.isEmpty();
    }

    public static <E> boolean isEmpty(Collection<E> coll)
    {
        return !isNotEmpty(coll);
    }


    public static boolean hasNull(String... param)
    {
        for (String str : param)
        {
            if (isNull(str))
            {
                return true;
            }
        }
        return false;
    }



    public static List<String> spiltStringToList(String str,String regex)
    {
        List<String> list = new ArrayList<>();
        String[] words = str.split(regex);
        for (int i = 0 ; i<words.length ; i ++)
        {
            list.add(words[i]);
        }
        return list;
    }


    /**
     * notes:使用HashSet实现List去重
     * @param list
     * @return
     */
    public static List repeatListWayTwo(List<String> list){
        //初始化HashSet对象，并把list对象元素赋值给HashSet对象
        HashSet set = new HashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }


    /**
     * 获得当天日期如 2013-05-23
     *
     * @return 当天日期
     */
    public static String getDay() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


}
