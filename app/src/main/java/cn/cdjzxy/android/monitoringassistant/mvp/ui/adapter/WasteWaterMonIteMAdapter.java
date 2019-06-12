package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WasteWaterMonItemHolder;



/**
 * 主页tab
 */

public class WasteWaterMonIteMAdapter extends DefaultAdapter<MonItems> {


    public WasteWaterMonIteMAdapter(List<MonItems> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<MonItems> getHolder(View v, int viewType) {
        return new WasteWaterMonItemHolder(v);
    }
}
