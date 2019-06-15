package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.WeatherAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;


public class WeatherActivity extends MyTitleActivity {

    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    private List<String> weatherList = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("天气选择");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabView.setVisibility(View.GONE);
        initMethodData();
        //tagId = getIntent().getStringExtra("tagId");
        List<Weather> weathers = DbHelpUtils.getWeatherList();
        if (!RxDataTool.isEmpty(weathers)) {
            for (Weather weather : weathers) {
                weatherList.addAll(weather.getWeathers());
            }
        }

        weatherAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(weatherList, mContext);
        weatherAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("weather", weatherList.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(weatherAdapter);
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
