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

public class MainTabHolder extends BaseHolder<Tab> {

    @BindView(R.id.tab_icon)
    ImageView mTabIcon;
    @BindView(R.id.tab_name)
    TextView mTabName;

    public MainTabHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position, Context context) {
        mTabName.setText(data.getTabName());
        if (data.isSelected()) {
            mTabName.setTextColor(Color.WHITE);
            mTabIcon.setImageResource(data.getSelectedResId());
            itemView.setBackgroundColor(context.getResources().getColor(R.color.bg_color_title));
        } else {
            itemView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            mTabName.setTextColor(context.getResources().getColor(R.color.color_939FB0));
            mTabIcon.setImageResource(data.getResId());

        }
    }

    @Override
    protected void onRelease() {
        this.mTabIcon = null;
        this.mTabName = null;

    }
}
