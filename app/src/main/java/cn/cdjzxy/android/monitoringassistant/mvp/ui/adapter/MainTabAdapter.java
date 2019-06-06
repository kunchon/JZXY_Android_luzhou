package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.MainTabHolder;


/**
 * 主页tab
 */

public class MainTabAdapter extends DefaultAdapter<Tab> {


    public MainTabAdapter(List<Tab> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_main_tab;
    }

    @Override
    public BaseHolder<Tab> getHolder(View v, int viewType) {
        return new MainTabHolder(v);
    }
}
