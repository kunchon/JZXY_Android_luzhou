package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;


public class WanderDetailHolder extends BaseHolder<WanderSampleStorage> {
    @BindView(R.id.iv_type)
    ImageView mIvType;
    @BindView(R.id.tv_num)
    TextView mTvNum;
    @BindView(R.id.tv_state)
    TextView mTvStatus;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_submit_time)
    TextView mSubmitTime;
    @BindView(R.id.tv_point)
    TextView mPoint;

    public WanderDetailHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(WanderSampleStorage data, int position, Context context) {
        Tags tags = DbHelpUtils.getTags(data.getTagId());

        if ("水质".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_water);
        } else if ("煤质".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_coal);
        } else if ("振动".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_shock);
        } else if ("土壤".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_soil);
        } else if ("降水".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_precipitation);
        } else if ("固废".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_solid);
        } else if ("辐射".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_radiation);
        } else if ("废气".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_gas);
        } else if ("噪声".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_noise);
        } else if ("空气".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_air);
        }

        mTvNum.setText(data.getSamplingNo());
        mTvStatus.setText(data.getStatusName());
        mTvName.setText(data.getFormName());
        if (data.getReceiveTime() == null || data.getReceiveTime().equals("")) {
            mSubmitTime.setText("收样时间：" + "暂无");
        } else {
            mSubmitTime.setText("收样时间：" + data.getReceiveTime());
        }
        mPoint.setText("点位：" + data.getAddressName());
    }
}
