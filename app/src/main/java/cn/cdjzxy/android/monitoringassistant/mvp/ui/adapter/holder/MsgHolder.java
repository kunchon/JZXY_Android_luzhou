package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;


/**
 * 主页tab
 */

public class MsgHolder extends BaseHolder<Msg> {

    @BindView(R.id.tv_title)
    TextView  mTvTitle;
    @BindView(R.id.tv_time)
    TextView  mTvTime;
    @BindView(R.id.tv_content)
    TextView  mTvContent;
    @BindView(R.id.iv_status)
    ImageView mIvStatus;

    public MsgHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Msg data, int position,Context context) {
        mTvTitle.setText(data.getMsgTitle());
        mTvTime.setText(data.getSendTime().replace("T", " "));
        mTvContent.setText(Html.fromHtml(data.getMsgContent()));
        if (data.getMsgStatus() == 0) {
            mIvStatus.setVisibility(View.VISIBLE);
        } else {
            mIvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRelease() {
        this.mTvTitle = null;
        this.mTvTime = null;
        this.mTvContent = null;

    }
}
