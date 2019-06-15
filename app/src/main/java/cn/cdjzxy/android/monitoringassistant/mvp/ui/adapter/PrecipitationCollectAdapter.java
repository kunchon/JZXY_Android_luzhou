package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.PrecipitationCollectHolder;


/**
 * 降水样品采集
 */

public class PrecipitationCollectAdapter extends DefaultAdapter<SamplingDetail> {


    public PrecipitationCollectAdapter(List<SamplingDetail> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_precipitation_collect;
    }

    @Override
    public BaseHolder<SamplingDetail> getHolder(View v, int viewType) {
        return new PrecipitationCollectHolder(v);
    }

}
