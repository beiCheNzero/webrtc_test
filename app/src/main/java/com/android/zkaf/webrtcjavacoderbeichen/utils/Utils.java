package com.android.zkaf.webrtcjavacoderbeichen.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 常用功能封装
 */
public class Utils {


    public static void showStatusBar(Context context) {
        Intent hide_NoticeIntent = new Intent();
        hide_NoticeIntent.setAction("com.android.systemui.statusbar.phone.statusopen");
//        hide_NoticeIntent.putExtra("statusbar_drop", "on");
        context.sendOrderedBroadcast(hide_NoticeIntent, (String) null);
    }

    public static void hideStatusBar(Context context) {
        Intent hide_NoticeIntent = new Intent();
        hide_NoticeIntent.setAction("com.android.systemui.statusbar.phone.statusclose");
        context.sendOrderedBroadcast(hide_NoticeIntent, (String) null);
    }


    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (context != null) {
            try {
                return context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0)
                        .versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "3.0.0";
    }

    /**
     * 时间格式转化
     *
     * @param timeStamp
     * @param pattern
     * @return
     */
    public static String formatTime(long timeStamp, String pattern) {
        Date date = new Date(timeStamp);
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    //获取系统属性
    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) get.invoke(c, key, defaultValue);
            Log.i("getProperty", "---" + value);
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return value;
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber() {
//        return "R68NS0225N0001";
        Log.e("test", "getSerialNumber==" + Build.VERSION.SDK_INT);
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("test", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

    /*
     * 反转卡号
     * 输出10进制卡号
     * */
    public static String formatCardNo(String cardhex) {
        String newStr = "";
        if (!isBlank(cardhex)) {
            int length = cardhex.length();
            if (length > 1 && length % 2 == 0) {
                for (int i = 0; i < length; i++) {
                    if (i % 2 == 0) {
                        newStr += cardhex.charAt(length - i - 2);
                    } else {
                        newStr += cardhex.charAt(length - i);
                    }
                }
            }
        }
        return String.valueOf(Long.parseLong(newStr, 16));
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                // 判断字符是否为空格、制表符、tab
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 比较时间大小(HH:mm)
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean compTime(String s1, String s2) {
        try {
            if (!s1.contains(":") || !s2.contains(":")) {
                Log.d("test", "时间格式不正确");
            } else {
                String[] array1 = s1.split(":");
                int total1 = Integer.parseInt(array1[0]) * 3600 + Integer.parseInt(array1[1]) * 60;
                String[] array2 = s2.split(":");
                int total2 = Integer.parseInt(array2[0]) * 3600 + Integer.parseInt(array2[1]) * 60;
                return total1 - total2 > 0;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            return false;
        }
        return false;

    }


    /**
     * 获取时间(HH:mm)
     *
     * @param time
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(new Date(time));
    }

    /**
     * 获取分号后面的内容
     */
    public static String getSubstringAfterColon(String input) {
        int colonIndex = input.lastIndexOf(':');
        if (colonIndex != -1 && colonIndex + 1 < input.length()) {
            return input.substring(colonIndex + 1);
        } else {
            return ""; // 如果没有找到分号，或分号后没有内容，返回空字符串
        }
    }
}


