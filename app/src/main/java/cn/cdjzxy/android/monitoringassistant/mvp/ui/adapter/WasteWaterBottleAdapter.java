package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WasteWaterBottleHolder;


public class WasteWaterBottleAdapter extends DefaultAdapter<SamplingFormStand> {


    public WasteWaterBottleAdapter(List<SamplingFormStand> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_bottle_split;
    }

    @Override
    public BaseHolder<SamplingFormStand> getHolder(View v, int viewType) {
        return new WasteWaterBottleHolder(v);
    }
}
