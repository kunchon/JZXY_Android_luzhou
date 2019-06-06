package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WanderTaskHolder;


public class WanderTaskAdapter extends DefaultAdapter<ProjectSampleStorage.DataBean> {
    public WanderTaskAdapter(List<ProjectSampleStorage.DataBean> infos, Context context) {
        super(infos,context);
    }

    @Override
    public BaseHolder<ProjectSampleStorage.DataBean> getHolder(View v, int viewType) {
        return new WanderTaskHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wander_task;
    }
}
