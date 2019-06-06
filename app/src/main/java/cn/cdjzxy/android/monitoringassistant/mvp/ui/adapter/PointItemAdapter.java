package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.PointItemHolder;



/**
 * 主页tab
 */

public class PointItemAdapter extends DefaultAdapter<EnvirPoint> {


    public PointItemAdapter(List<EnvirPoint> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_item;
    }

    @Override
    public BaseHolder<EnvirPoint> getHolder(View v, int viewType) {
        return new PointItemHolder(v);
    }
}
