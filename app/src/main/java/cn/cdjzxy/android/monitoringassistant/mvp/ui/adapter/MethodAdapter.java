package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.MethodHolder;



/**
 * 主页tab
 */

public class MethodAdapter extends DefaultAdapter<Methods> {


    public MethodAdapter(List<Methods> infos, Context context) {
        super(infos, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<Methods> getHolder(View v, int viewType) {
        return new MethodHolder(v);
    }
}
