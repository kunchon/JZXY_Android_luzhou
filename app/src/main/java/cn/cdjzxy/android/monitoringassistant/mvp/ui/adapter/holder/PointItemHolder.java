package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;


/**
 * 点位列表
 */

public class PointItemHolder extends BaseHolder<EnvirPoint> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public PointItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(EnvirPoint data, int position, Context context) {
        mTvName.setText(data.getName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
