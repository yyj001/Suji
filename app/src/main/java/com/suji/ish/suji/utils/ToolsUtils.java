package com.suji.ish.suji.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.TextUtils;

import java.text.SimpleDateFormat;

/**
 * 各种工具集合类
 * @author ish
 */
public class ToolsUtils {

    private volatile static ToolsUtils instance = null;

    private ToolsUtils(){
    }

    public static ToolsUtils getInstance(){
        if(instance == null){
            synchronized (ToolsUtils.class){
                if(instance == null){
                    instance = new ToolsUtils();
                }
            }
        }
        return instance;
    }

    public int dp2px(Context context, float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(Context context,float pxValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getColor(Context context,int colorId){
        Resources resources = context.getResources();
        int color = resources.getColor(colorId);
        return color;
    }

    public float getDimension(Context context,int dimId){
        Resources resources = context.getResources();
        float dim = resources.getDimension(dimId);
        return dim;
    }

    /**
     * 获取时间戳
     */
    public long getInstanceTime(){
        return System.currentTimeMillis();
    }

    /**
     * 时间戳转string日期
     */
    public String getDateFormat(long time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

    public void viberate(Context context,int time){
        Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        long [] pattern = {0,time};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern,-1);
    }

    /**
     * 限制字长
     * @param str
     * @param maxLen
     * @return
     */
    public String handleText(String str, int maxLen) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int count = 0;
        int endIndex=0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if(maxLen==count || (item>=128 && maxLen+1==count)){
                endIndex=i;
            }
        }
        if (count <= maxLen) {
            return str;
        } else {

            return str.substring(0, endIndex) + "...";
        }

    }

}
