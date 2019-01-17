package com.suji.ish.suji.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;

import com.suji.ish.suji.R;

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

}
