package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.EnterRelatePointSelectHolder;


public class EnterRelatePointSelectAdapter extends DefaultAdapter<EnterRelatePoint> {


    public EnterRelatePointSelectAdapter(List<EnterRelatePoint> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<EnterRelatePoint> getHolder(View v, int viewType) {
        return new EnterRelatePointSelectHolder(v);
    }
}