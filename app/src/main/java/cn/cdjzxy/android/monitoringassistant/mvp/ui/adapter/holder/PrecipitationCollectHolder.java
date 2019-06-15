package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;


/**
 *降水样品采集
 */

public class PrecipitationCollectHolder extends BaseHolder<SamplingDetail> {

    @BindView(R.id.tv_frequency)
    TextView tvFrequency;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_precipitation)
    TextView tvPrecipitation;
    @BindView(R.id.tv_rainwater_volume)
    TextView tvRainwaterVolume;
    @BindView(R.id.tv_remark)
    TextView tvRemark;

    public PrecipitationCollectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SamplingDetail data, int position, Context context) {
        tvFrequency.setText("频次：" + data.getFrequecyNo());
        try {
            JSONObject privateData = new JSONObject(data.getPrivateData());
            tvTime.setText(privateData.getString("SDataTime") + "--" +privateData.getString("EDataTime"));
            tvRainwaterVolume.setText("接雨体积(ml)：" + privateData.getString("RainVol"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvPrecipitation.setText("降水量(mm)：" + data.getValue());
        tvRemark.setText("备注：" + data.getDescription());
    }

    @Override
    protected void onRelease() {
        this.tvFrequency = null;
        this.tvTime = null;
        this.tvRainwaterVolume = null;
        this.tvPrecipitation = null;
        this.tvRemark = null;
    }

}
