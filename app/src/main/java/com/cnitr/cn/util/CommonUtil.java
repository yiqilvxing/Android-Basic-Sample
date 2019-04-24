package com.cnitr.cn.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.cnitr.cn.R;

import java.io.DataOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangChen on 2018/7/5.
 */

public class CommonUtil {

    /**
     * 执行命令
     *
     * @param command 1、获取root权限 "chmod 777 "+getPackageCodePath()
     *                2、关机 reboot -p
     *                3、重启 reboot
     */
    public static boolean execCmd(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static void showActionMessage(String msg, View v) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT)
                .setAction(android.R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public static void showActionMessage(View v) {
        Snackbar.make(v, R.string.copyright, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    /**
     * 日期时间格式化
     *
     * @param time
     * @return
     */
    public static String dateFormat(String time) {

        String result = "";
        try {
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM-dd");

            Date date = yearFormat.parse(time);

            long currentTime = System.currentTimeMillis();
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(new Date(currentTime));

            // 同一年
            if (time.startsWith(currentCalendar.get(Calendar.YEAR) + "-")) {
                result = monthFormat.format(date.getTime());
            } else {
                result = yearFormat.format(date.getTime());
            }
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    /**
     * 日期时间格式化
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(long time) {

        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MM-dd");
            SimpleDateFormat dayFormat = new SimpleDateFormat("HH:mm");

            long currentTime = System.currentTimeMillis();
            Calendar currentCalendar = Calendar.getInstance();
            Date currentDate = new Date(currentTime);
            currentCalendar.setTime(currentDate);

            Calendar timeCalendar = Calendar.getInstance();
            Date date = new Date(time);
            timeCalendar.setTime(date);

            // 当年
            if (currentCalendar.get(Calendar.YEAR) == (timeCalendar.get(Calendar.YEAR))) {

                // 当月
                if (currentCalendar.get(Calendar.DAY_OF_MONTH) == timeCalendar.get(Calendar.DAY_OF_MONTH)) {

                    // 当天
                    if (currentCalendar.get(Calendar.DAY_OF_YEAR) == timeCalendar.get(Calendar.DAY_OF_YEAR)) {
                        result = dayFormat.format(date);

                    } else {
                        result = monthFormat.format(date);
                    }
                } else {
                    result = monthFormat.format(date);
                }
            } else {
                result = yearFormat.format(date);
            }
        } catch (Exception e) {
            return "";
        }
        return result;
    }


    /**
     * 日期时间格式化
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String dateFormat(String time, String formatStr) {

        String result = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            SimpleDateFormat tempFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            Date date = tempFormat.parse(time);
            if (date != null) {
                result = format.format(date);
            }
        } catch (Exception e) {
            return "";
        }
        return result;
    }


    /**
     * 取得App版本号
     *
     * @param context
     * @return
     */
    public static String getsystemVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 递归删除文件和文件夹
     */
    public static void recursionDeleteFile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    recursionDeleteFile(f);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化缩放图片地址
     * <p>
     * 宽度：dpWidth
     * 高度：dpHeight
     */
    public static String formatZoomImageDownLoadUrl(Context context, String url, int dpWidth, int dpHeight) {

        int width = dp2px(context, dpWidth);
        int height = dp2px(context, dpHeight);

        return url + "?x-oss-process=image/resize,m_fill,h_" + height + ",w_" + width;
    }

    /**
     * 格式化缩放图片地址
     * <p>
     * 宽度：pxWidth
     * 高度：pxHeight
     */
    public static String formatZoomImageDownLoadUrl(String url, int pxWidth, int pxHeight) {

        return url + "?x-oss-process=image/resize,m_fill,h_" + pxHeight + ",w_" + pxWidth;
    }

    /**
     * dp2px
     * <p>
     * Value of dp to value of px.
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     * <p>
     * Value of px to value of dp.
     */
    public static int px2dp(Context context, final float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp2px
     * <p>
     * Value of sp to value of px.
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px2sp
     * <p>
     * Value of px to value of sp.
     */
    public static int px2sp(Context context, final float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /***
     * 屏幕宽度（像素）
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }

    /***
     * 屏幕高度（像素）
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }


}
