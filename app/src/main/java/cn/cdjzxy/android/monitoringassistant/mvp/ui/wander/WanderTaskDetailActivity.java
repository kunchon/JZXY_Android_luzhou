package cn.cdjzxy.android.monitoringassistant.mvp.ui.wander;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aries.ui.view.title.TitleBarView;


import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;


/**
 * 流转任务详情
 */
public class WanderTaskDetailActivity extends MyTitleActivity implements IView {


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }



    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
