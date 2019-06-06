package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;


/**
 * 主页tab
 */

public class TabHolder extends BaseHolder<Tab> {


    @BindView(R.id.tab_name)
    TextView mTabName;
    @BindView(R.id.tab_icon)
    ImageView mTabIcon;

    public TabHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position,Context context) {
        mTabName.setText(data.getTabName());
        if (data.getResId() != -1 || data.getResId() != 0) {
            mTabIcon.setVisibility(View.VISIBLE);
            mTabIcon.setImageResource(data.getResId());
        }
        if (data.isSelected()) {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.bg_color_c7e4ff));
        } else {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.text_color_ffffff));
        }

    }

    @Override
    protected void onRelease() {

        this.mTabName = null;

    }
}
