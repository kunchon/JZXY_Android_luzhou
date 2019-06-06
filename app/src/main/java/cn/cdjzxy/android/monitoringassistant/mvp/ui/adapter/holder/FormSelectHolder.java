package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;


/**
 * 主页tab
 */

public class FormSelectHolder extends BaseHolder<FormSelect> {

    @BindView(R.id.tv_name)
    TextView mTvName;

    public FormSelectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(FormSelect data, int position, Context context) {
        mTvName.setText(data.getFormName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
