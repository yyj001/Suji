package com.suji.ish.suji.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.adapter.TabPageIndicatorAdapter;
import com.suji.ish.suji.utils.ToolsUtils;
import com.suji.ish.suji.view.HomeViewPager;
import com.suji.ish.suji.view.MenuPopupWindow;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

    private EasyPopup mCirclePop;

    private MenuPopupWindow mMenuPopupWindow;
    //navigation fragment id 默认是笔记本页面;
    private int mNavgraphId = R.id.nav_host_notebook;

    LinearLayout mAddNoteBtn;
    LinearLayout mAddNoteBookBtn;



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
        mTextViews[mSelectBtnNum].setTextColor(getResources().getColor(R.color.grey8));
        //设置新的按钮
        mSelectBtnNum = i;
        mImaheViews[i].setImageResource(imageSelected[i]);
        mTextViews[i].setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navbtn1:
                setSelectBtn(0);
                mViewPager.setCurrentItem(0);
                mNavgraphId = R.id.nav_host_notebook;
                break;
            case R.id.navbtn2:
                setSelectBtn(1);
                mViewPager.setCurrentItem(1);
                break;
            case R.id.navbtn3:
                setSelectBtn(2);
                mViewPager.setCurrentItem(2);
                break;
            case R.id.navbtn4:
                setSelectBtn(3);
                mViewPager.setCurrentItem(3);
                break;
            case R.id.navbtn5:
                ToolsUtils.getInstance().viberate(this,10);
                showPopupMenu(view);
                break;
            default:
        }
    }

    public void showPopupMenu(View view){
        mMenuPopupWindow = MenuPopupWindow.create(this)
                .apply();
        mMenuPopupWindow.showAtAnchorView(view, YGravity.ABOVE, XGravity.CENTER, 0,0);
        FloatingActionButton addNotebookFbtn = mMenuPopupWindow.findViewById(R.id.add_notebook_btn);
        FloatingActionButton addWordFbtn = mMenuPopupWindow.findViewById(R.id.add_word_btn);

        addNotebookFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuPopupWindow.dismiss();
                goTo(new AddNoteBookActivity());
            }
        });
        addWordFbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMenuPopupWindow.dismiss();
                goTo(new AddWordActivity());
            }
        });
  }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        NavController navController = (NavController) Navigation
                .findNavController(HomeActivity.this, mNavgraphId);
        if(!navController.popBackStack()){
            super.onBackPressed();
        }
    }
}
