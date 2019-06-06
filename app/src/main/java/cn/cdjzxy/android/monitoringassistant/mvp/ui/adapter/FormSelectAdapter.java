package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;


import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.FormSelectHolder;


/**
 * 主页tab
 */

public class FormSelectAdapter extends DefaultAdapter<FormSelect> {


    public FormSelectAdapter(List<FormSelect> infos, Context context) {
        super(infos, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<FormSelect> getHolder(View v, int viewType) {
        return new FormSelectHolder(v);
    }
}
