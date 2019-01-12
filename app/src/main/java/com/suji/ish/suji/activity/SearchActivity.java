package com.suji.ish.suji.activity;

import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.suji.ish.suji.R;

public class SearchActivity extends BaseActivity {
    private FloatingSearchView searchView;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = (FloatingSearchView)findViewById(R.id.search_searchbar);
        //延迟弹出键盘
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.setSearchFocused(true);
            }
        },500);

    }

}
