package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.ProgramPointHolder;


/**
 * 方案点位adapter
 */
public class ProgramPointAdapter extends DefaultAdapter<EnvirPoint> {


    public ProgramPointAdapter(List<EnvirPoint> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_program_point;
    }

    @Override
    public BaseHolder<EnvirPoint> getHolder(View v, int viewType) {
        return new ProgramPointHolder(v);
    }
}
