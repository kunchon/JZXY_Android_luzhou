package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;



import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


public class WasteWaterBottleHolder extends BaseHolder<SamplingFormStand> {

    @BindView(R.id.bottle_name)
    TextView bottle_name;
    @BindView(R.id.bottle_norms)
    TextView bottle_norms;
    @BindView(R.id.bottle_desc)
    TextView bottle_desc;
    @BindView(R.id.bottle_hold_time)
    TextView bottle_hold_time;
    @BindView(R.id.bottle_order)
    TextView bottle_order;
    @BindView(R.id.bottle_preserve_method)
    TextView bottle_preserve_method;
    @BindView(R.id.bottle_analyse_address)
    TextView bottle_analyse_address;

    public WasteWaterBottleHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SamplingFormStand data, int position, Context context) {
        bottle_name.setText(data.getMonitemName());
        bottle_norms.setText("采样量："+data.getSamplingAmount());
        String amount=data.getCount()==0?"":data.getCount()+"";
        bottle_desc.setText("数量(瓶)："+amount);
        bottle_hold_time.setText("保存时间："+data.getSaveTimes());
        bottle_order.setText("排序："+data.getIndex());
        String saveMethod=RxDataTool.isEmpty(data.getSaveMehtod())?"":data.getSaveMehtod();
        bottle_preserve_method.setText("保存方法："+saveMethod);
        String place=RxDataTool.isEmpty(data.getAnalysisSite())?"":data.getAnalysisSite();
        bottle_analyse_address.setText("分析地点："+place);
    }

    @Override
    protected void onRelease() {
        this.bottle_name = null;
        this.bottle_norms = null;
        this.bottle_desc = null;
        this.bottle_hold_time = null;
        this.bottle_order = null;
        this.bottle_preserve_method = null;
        this.bottle_analyse_address = null;
    }
}
