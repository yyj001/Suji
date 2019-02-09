package com.suji.ish.suji.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;

import com.suji.ish.suji.R;
import com.suji.ish.suji.SujiApplication;
import com.suji.ish.suji.bean.Word;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 各种工具集合类
 *
 * @author ish
 */
public class ToolsUtils{
    private static final String TAG = "ToolsUtils";

    private volatile static ToolsUtils instance = null;

    private int mScreemWidth;
    private int mScreemHeight;

    private ToolsUtils() {
    }

    public static ToolsUtils getInstance() {
        if (instance == null) {
            synchronized (ToolsUtils.class) {
                if (instance == null) {
                    instance = new ToolsUtils();


                }
            }
        }
        return instance;
    }

    public void initScreemSize() {
        DisplayMetrics dm = SujiApplication.getInstance().getResources().getDisplayMetrics();
        this.mScreemHeight = px2dp(dm.heightPixels);
        this.mScreemWidth = px2dp(dm.widthPixels);
    }

    public int getmScreemWidth() {
        return mScreemWidth;
    }

    public int getmScreemHeight() {
        return mScreemHeight;
    }

    public int dp2px(float dpValue) {
        final float scale = SujiApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = SujiApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public int getColor(Context context, int colorId) {
        Resources resources = context.getResources();
        int color = resources.getColor(colorId);
        return color;
    }

    public int getColor(int colorId) {
        Resources resources = SujiApplication.getInstance().getResources();
        int color = resources.getColor(colorId);
        return color;
    }

    public float getDimension(Context context, int dimId) {
        Resources resources = context.getResources();
        float dim = resources.getDimension(dimId);
        return dim;
    }

    /**
     * 获取时间戳
     */
    public long getInstanceTime() {
        return System.currentTimeMillis();
    }

    public String getInstanceTimeStr(){
        return getDateFormat(getInstanceTime());
    }
    /**
     * 时间戳转string日期
     */
    public String getDateFormat(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

    public void viberate(Context context, int time) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        long[] pattern = {0, time};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }


    /**
     * 限制字长
     *
     * @param str
     * @param maxLen
     * @return
     */
    public String handleText(String str, int maxLen) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int count = 0;
        int endIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (maxLen == count || (item >= 128 && maxLen + 1 == count)) {
                endIndex = i;
            }
        }
        if (count <= maxLen) {
            return str;
        } else {

            return str.substring(0, endIndex) + "...";
        }

    }

    /**
     * 高亮单词
     *
     * @param sourceStr
     * @param word
     * @return
     */
    public SpannableString getHightLightSentence(String sourceStr, Word word) {

        SpannableString s = new SpannableString(sourceStr);
        if (word.getSpell() == null) {
            return null;
        }

        //不区分大小写
        Pattern p = Pattern.compile(word.getSpell(), Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        matcherStr(s, m);
        //pl
        if (word.getWordPl() != null) {
            p = Pattern.compile(word.getWordPl(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        //past
        if (word.getWordPast() != null) {
            p = Pattern.compile(word.getWordPast(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        if (word.getWordDone() != null) {
            p = Pattern.compile(word.getWordDone(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        if (word.getWordIng() != null) {
            p = Pattern.compile(word.getWordIng(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        if (word.getWordThird() != null) {
            p = Pattern.compile(word.getWordThird(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        if (word.getWordEr() != null) {
            p = Pattern.compile(word.getWordEr(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }

        if (word.getWordEst() != null) {
            p = Pattern.compile(word.getWordEst(), Pattern.CASE_INSENSITIVE);
            m = p.matcher(s);
            matcherStr(s, m);
        }
        return s;
    }

    /**
     * 判断单词前后是否为字母
     * 格式统一，句首必须是一个空格
     *
     * @param s
     * @param m
     */
    private void matcherStr(SpannableString s, Matcher m) {
        while (m.find()) {
            int start = m.start();
            int end = m.end();

            //格式:第一个字母不能为字母
            if (start == 0) {
                if (!judgeLegal(s.charAt(end))) {
                    continue;
                }
            } else if (end >= s.length() - 1) {
            } else if (!(judgeLegal(s.charAt(start - 1)) && judgeLegal(s.charAt(end)))) {
                continue;
            }
            s.setSpan(new ForegroundColorSpan(ToolsUtils.getInstance().getColor(R.color.colorAccent)),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 如果是字母，就不合法
     */
    private boolean judgeLegal(char c) {
        // 大写
        if ((int) c >= 65 && (int) c <= 90) {
            return false;
        } else if ((int) c >= 97 && (int) c <= 122) {
            return false;
        }
        return true;
    }

}
