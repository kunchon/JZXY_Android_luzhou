package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;



import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;


/**
 * 主页tab
 */

public class SearchHolder extends BaseHolder<Tab> {

    @BindView(R.id.tab_name)
    TextView mTabName;

    public SearchHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position, Context context) {
        mTabName.setText(data.getTabName());
        if (data.isSelected()) {
            mTabName.setBackgroundResource(data.getSelectedResId());
        } else {
            mTabName.setBackgroundResource(data.getResId());
        }
    }

    @Override
    protected void onRelease() {
        this.mTabName = null;
    }
}
