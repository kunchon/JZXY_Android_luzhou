package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;


/**
 * 方案点位Holder
 */

public class ProgramPointHolder extends BaseHolder<EnvirPoint> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public ProgramPointHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(EnvirPoint data, int position, Context context) {
        mTvName.setText(data.getName());
        if (data.isSelected()) {
            mTvName.setBackgroundResource(R.drawable.shape_project_item);
            mTvName.setTextColor(context.getResources().getColor(R.color.text_color_ffffff));
        } else {
            mTvName.setBackgroundResource(R.drawable.shape_search_condition_selected);
            mTvName.setTextColor(context.getResources().getColor(R.color.color_333333));
        }
    }

    @Override
    protected void onRelease() {

        this.mTvName = null;

    }
}
