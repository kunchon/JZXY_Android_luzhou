package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.TaskDetailHolder;


/**
 * 主页tab
 */

public class TaskDetailAdapter extends DefaultAdapter<Sampling> {

    private OnSamplingListener onSamplingListener;

    public TaskDetailAdapter(List<Sampling> infos, Context context, OnSamplingListener onSamplingListener) {
        super(infos, context);
        this.onSamplingListener = onSamplingListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_task_detail;
    }

    @Override
    public BaseHolder<Sampling> getHolder(View v, int viewType) {
        return new TaskDetailHolder(v, onSamplingListener);
    }


    public interface OnSamplingListener {
        void onSelected(View view, int position);

        void onClick(View view, int position);

        void onLongClick(View view, int position);

        void onUpload(View view, int position);
    }
}
