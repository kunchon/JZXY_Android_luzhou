package cn.cdjzxy.android.monitoringassistant.mvp.ui.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;


public class MsgDetailActivity extends MyTitleActivity {


    @BindView(R.id.tv_content)
    TextView tvContent;

    TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("消息详情");
        mTitleBarView = titleBar;
    }



    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTitleBarView.setTitleMainText(getIntent().getStringExtra("title"));
        tvContent.setText(Html.fromHtml(getIntent().getStringExtra("content")));
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
