package com.suji.ish.suji;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;

public class SujiApplication extends Application{

    private static SujiApplication instance;

    public static SujiApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();
        instance = this;

    }

}
