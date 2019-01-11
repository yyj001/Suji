package com.suji.ish.suji.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.TabPageIndicatorAdapter;
import com.suji.ish.suji.view.HomeViewPager;

/**
 * @author ish
 * @date 2019/1/8
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 导航栏设置
     **/
    private ImageView[] mImaheViews = new ImageView[5];
    private TextView[] mTextViews = new TextView[4];
    private RelativeLayout[] mRelativityLayouts = new RelativeLayout[5];
    private int[] imageId = {R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5};
    private int[] textId = {R.id.txview1, R.id.txview2, R.id.txview3, R.id.txview4};
    private int[] rlId = {R.id.navbtn1, R.id.navbtn2, R.id.navbtn3, R.id.navbtn4, R.id.navbtn5};
    private int[] imageUnSelect = {R.drawable.notebook, R.drawable.memo, R.drawable.rank, R.drawable.user};
    private int[] imageSelected = {R.drawable.notebook_fill, R.drawable.memo_fill,
            R.drawable.rank_fill, R.drawable.user_fill};
    private int mSelectBtnNum = 0;

    private HomeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        for (int i = 0; i < 5; i++) {
            mImaheViews[i] = (ImageView) findViewById(imageId[i]);
        }

        for (int i = 0; i < 4; i++) {
            mTextViews[i] = (TextView) findViewById(textId[i]);
        }

        for (int i = 0; i < 5; i++) {
            mRelativityLayouts[i] = (RelativeLayout) findViewById(rlId[i]);
            mRelativityLayouts[i].setOnClickListener(this);
        }

        setSelectBtn(0);

        FragmentPagerAdapter fragmentPagerAdapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        mViewPager = (HomeViewPager) findViewById(R.id.home_viewpager);
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
    }

    private void setSelectBtn(int i) {
        //将之前选中的变没有选中
        mImaheViews[mSelectBtnNum].setImageResource(imageUnSelect[mSelectBtnNum]);
        mTextViews[mSelectBtnNum].setTextColor(getResources().getColor(R.color.greya));
        //设置新的按钮
        mSelectBtnNum = i;
        mImaheViews[i].setImageResource(imageSelected[i]);
        mTextViews[i].setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navbtn1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                setSelectBtn(0);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.navbtn2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                setSelectBtn(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.navbtn3:
                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                setSelectBtn(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.navbtn4:
                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                setSelectBtn(3);
                mViewPager.setCurrentItem(3);
                break;
            case R.id.navbtn5:
                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

}
