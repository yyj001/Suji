package com.suji.ish.suji.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.suji.ish.suji.R;

public class SearchActivity extends BaseActivity {
    private FloatingSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (FloatingSearchView)findViewById(R.id.search_searchbar);
        searchView.setSearchFocused(true);
    }
}
