package com.suji.ish.suji.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.suji.ish.suji.R;
import com.suji.ish.suji.model.WordModel;
import com.suji.ish.suji.utils.ToolsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author ish
 */
public class StatisticFragment extends Fragment {

    private static final String TAG = "StatisticFragment";
    private PieChart mPieChart;
    private WordModel mModel;
    private boolean isFinishData = false;

    public StatisticFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        mPieChart = view.findViewById(R.id.statistic_pie_chart);
        mPieChart.setNoDataText("");
        mModel = new WordModel();
        return view;
    }

    public void initData() {
        float wordNumber = mModel.getWordNum();
        float unRememberNum = mModel.getUnRememberWordNum();
        float unclearNum = mModel.getUnClearWordNum();
        float knowNum = mModel.getKnowWordNum();
        float familiarNum = mModel.getFamiliarWordNum();

        Log.d(TAG, "initData: "+ unRememberNum + " " + unclearNum + " " + knowNum + " " + familiarNum);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(familiarNum/wordNumber, "熟悉"));
        entries.add(new PieEntry(unclearNum/wordNumber, "模糊"));
        entries.add(new PieEntry(unRememberNum/wordNumber, "未记忆"));
        entries.add(new PieEntry(knowNum/wordNumber, "认识"));

        PieDataSet set = new PieDataSet(entries,"");
        set.setValueLinePart1OffsetPercentage(familiarNum/wordNumber);
        set.setColors(ToolsUtils.getInstance().getColor(R.color.red),
                ToolsUtils.getInstance().getColor(R.color.colorAccent),
                ToolsUtils.getInstance().getColor(R.color.darkGreen),
                ToolsUtils.getInstance().getColor(R.color.AlphaColorPrimaryDark));
        set.setValueTextSize(10);
        set.setValueTextColor(ToolsUtils.getInstance().getColor(R.color.darkBlue));
        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        mPieChart.setData(data);
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription(null);
        mPieChart.animateXY(800,800);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && !isFinishData){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            },200);
            isFinishData = true;
        }
    }
}
