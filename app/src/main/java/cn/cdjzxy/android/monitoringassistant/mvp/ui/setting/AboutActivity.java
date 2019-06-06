package cn.cdjzxy.android.monitoringassistant.mvp.ui.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.aries.ui.view.title.TitleBarView;


import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.webview.WebFragment;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.StatusBarUtil;

public class AboutActivity extends MyTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("关于我们");
    }



    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        Bundle mBundle = new Bundle();
        mBundle.putString(WebFragment.URL_KEY, "http://www.cdjzxy.cn/");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, WebFragment.getInstance(mBundle), WebFragment.class.getName());
        ft.commit();

    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
