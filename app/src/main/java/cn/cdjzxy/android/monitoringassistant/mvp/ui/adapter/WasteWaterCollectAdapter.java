package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.WasteWaterCollectHolder;


/**
 * 水和废水采样
 */

public class WasteWaterCollectAdapter extends DefaultAdapter<SamplingContent> {

    private OnWasteWaterCollectListener collectListener;

    public WasteWaterCollectAdapter(List<SamplingContent> details, Context context,
                                    OnWasteWaterCollectListener collectListener) {
        super(details,context);
        this.collectListener = collectListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_collect;
    }

    @Override
    public BaseHolder<SamplingContent> getHolder(View v, int viewType) {
        return new WasteWaterCollectHolder(v, collectListener);
    }

    public interface OnWasteWaterCollectListener {
        void onSelected(View view, int position, boolean isSelected);
    }

    @Override
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
