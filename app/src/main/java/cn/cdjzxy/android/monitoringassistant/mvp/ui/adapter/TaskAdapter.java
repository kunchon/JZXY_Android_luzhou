package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.TaskHolder;

/**
 * 主页tab
 */

public class TaskAdapter extends DefaultAdapter<Project> {


    public TaskAdapter(List<Project> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_task;
    }

    @Override
    public BaseHolder<Project> getHolder(View v, int viewType) {
        return new TaskHolder(v);
    }
}
