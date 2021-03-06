package cn.yumutech.weight;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils1 {

    private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static long getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormater2.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return Long.parseLong(curDate);
    }

    /**
     * 获取正规时间表达 2009-9-9 12:12:12
     *
     * @param mmtime
     * @return
     */
    public static String getfullTime(long mmtime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return formatter.format(new Date(mmtime));
    }

    /**
     * 转化为视频或者音乐播放时长
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {

        int i = (int) (time / 1000);
        int minute = i / 60;
        int hour = minute / 60;
        int second = i % 60;
        minute %= 60;

        return String.format("%02d:%02d:%02d", hour, minute, second);

    }

    /**
     *
     * @param time
     * @return
     */
    public static String getym(String time){
        Log.e("info",time);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf1=new SimpleDateFormat("MM月dd日");
          String datest1r=sdf1.format(date);
        return datest1r;
    }

    public static long formatTime_turn(String time) {

        String[] substr = time.split(":");

        if (substr.length != 3) {
            return 0;
        }

        int h = toInt(substr[0]);
        int m = toInt(substr[1]);
        int s = toInt(substr[2]);

        return (h * 60 * 60 + m * 60 + s) * 1000;

    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 将文件大小以友好的方式展示
     *
     * @param filesize
     * @return
     */
    public static String getFileSize(long filesize) {
        DecimalFormat df = new DecimalFormat("#.00");
        StringBuffer mstrbuf = new StringBuffer();

        if (filesize < 1024) {
            mstrbuf.append(filesize);
            mstrbuf.append(" B");
        } else if (filesize < 1048576) {
            mstrbuf.append(df.format((double) filesize / 1024));
            mstrbuf.append(" K");
        } else if (filesize < 1073741824) {
            mstrbuf.append(df.format((double) filesize / 1048576));
            mstrbuf.append(" M");
        } else {
            mstrbuf.append(df.format((double) filesize / 1073741824));
            mstrbuf.append(" G");
        }

        df = null;

        return mstrbuf.toString();
    }

    /**
     * 格式化显示输出文件大小
     *
     * @param fileSize
     * @return
     */
    public static String getFileSize_BK(float fileSize) {
        long kb = 1024;
        long mb = (kb * 1024);
        long gb = (mb * 1024);

        if (fileSize < kb)
            return String.format("%d B", (int) fileSize);
        else if (fileSize < mb)
            return String.format("%.2f KB", fileSize / kb);
        else if (fileSize < gb)
            return String.format("%.2f MB", fileSize / mb);
        else
            return String.format("%.2f GB", fileSize / gb);
    }

    /**
     * 检查当前语言环境是否为中文
     *
     * @param context
     * @return
     */
    public static boolean checkZhLanguageEnv(Context context) {
        boolean b = false;
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            b = true;
        }
        return b;
    }


    public static String formatDuration(long duration) {
        long time = duration ;
        String times = "";
//        if (time <= 0) {
//            return "00:00";
//        }
        long min = time / 60 % 60;
        long hour = time / 60 / 60;
        long second = time % 60;
        if (hour > 0) {
            times = String.format("%02d:%02d:%02d", hour, min, second);
        } else {
            times = String.format("%02d:%02d", min, second);
        }
        return times;
    }
/**
 * 获取当前日期
 */
    public static String getCurrentData(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static  boolean isAppOnForeground(Context contxt) {
        ActivityManager activityManager =(ActivityManager) contxt.getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName =contxt.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取当前年月
     */
    public static String getYearsAndMonth(){
        SimpleDateFormat sDateFormat =new SimpleDateFormat("yyyy-MM");
        String date=sDateFormat.format(new Date());
        return date;
    }
    /**
     * 获取当前日期
     */
    public static String getDay(){
        Calendar calendar=Calendar.getInstance();
        String day = calendar.get(Calendar.DAY_OF_MONTH)+"";
        return day;
    }
    /**
     * 获取当前时
     */
    public static String getHOUR(){
        Calendar calendar=Calendar.getInstance();
        String day = calendar.get(Calendar.HOUR_OF_DAY)+"";
        return day;
    }
    /**
     * 获取当前分
     */
    public static String getMINUTE(){
        Calendar calendar=Calendar.getInstance();
        String day = calendar.get(Calendar.MINUTE)+"";
        return day;
    }
    /**
     * 获取当前秒
     */
    public static String getSECOND(){
        Calendar calendar=Calendar.getInstance();
        String day = calendar.get(Calendar.SECOND)+"";
        return day;
    }
    /**
     * 获取当前星期几
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        String mweek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mweek)) {
            mweek = "天";
        } else if ("2".equals(mweek)) {
            mweek = "一";
        } else if ("3".equals(mweek)) {
            mweek = "二";
        } else if ("4".equals(mweek)) {
            mweek = "三";
        } else if ("5".equals(mweek)) {
            mweek = "四";
        } else if ("6".equals(mweek)) {
            mweek = "五";
        } else if ("7".equals(mweek)) {
            mweek = "六";
        }
        return mweek;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getLocalIPAddress() {

        String ip = "";

        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }

        } catch (Exception e) {

            ip = "";
        }

        return "192.168.1.109";
    }
    /**
     * 判断是否是手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}
