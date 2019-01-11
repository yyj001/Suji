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

        //延迟弹出键盘
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView = (FloatingSearchView)findViewById(R.id.search_searchbar);
                searchView.setSearchFocused(true);
            }
        },500);

        LinearLayout i = (LinearLayout)findViewById(R.id.test);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(SearchActivity.this, view, Gravity.RIGHT);
                // 获取布局文件
                popupMenu.getMenuInflater().inflate(R.menu.notebook_menu, popupMenu.getMenu());


                // 通过上面这几行代码，就可以把控件显示出来了
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // 控件每一个item的点击事件
                        return true;
                    }
                });
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        // 控件消失时的事件
                    }
                });
                popupMenu.show();
            }
        });

    }
}
