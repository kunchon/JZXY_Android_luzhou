package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;


public class WeatherHolder extends BaseHolder<String> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public WeatherHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(String data, int position, Context context) {
        mTvName.setText(data);
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
