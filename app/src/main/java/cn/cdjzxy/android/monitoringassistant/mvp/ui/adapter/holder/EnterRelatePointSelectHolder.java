package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;


public class EnterRelatePointSelectHolder extends BaseHolder<EnterRelatePoint> {
    @BindView(R.id.tv_name)
    TextView mTvName;

    public EnterRelatePointSelectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(EnterRelatePoint data, int position, Context context) {
        mTvName.setText(data.getName());
    }


    @Override
    protected void onRelease() {
        this.mTvName = null;
    }

}
