package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WeatherHolder;


public class WeatherAdapter extends DefaultAdapter<String> {


    public WeatherAdapter(List<String> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new WeatherHolder(v);
    }
}
