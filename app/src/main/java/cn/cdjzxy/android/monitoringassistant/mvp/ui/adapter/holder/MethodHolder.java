package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;


/**
 * 主页tab
 */

public class MethodHolder extends BaseHolder<Methods> {

    @BindView(R.id.tv_name)
    TextView mTvName;

    public MethodHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Methods data, int position, Context context) {
        mTvName.setText(data.getCode() == null ? data.getName() : data.getName() + "(" + data.getCode() + ")");

    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }


}
