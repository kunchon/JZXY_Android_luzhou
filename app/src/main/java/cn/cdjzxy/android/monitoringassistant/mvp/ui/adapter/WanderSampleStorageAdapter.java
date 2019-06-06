package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WanderDetailHolder;


public class WanderSampleStorageAdapter extends DefaultAdapter<WanderSampleStorage> {
    public WanderSampleStorageAdapter(List<WanderSampleStorage> infos, Context context) {
        super(infos,context);
    }

    @Override
    public BaseHolder<WanderSampleStorage> getHolder(View v, int viewType) {
        return new WanderDetailHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wander_task_detail;
    }
}
