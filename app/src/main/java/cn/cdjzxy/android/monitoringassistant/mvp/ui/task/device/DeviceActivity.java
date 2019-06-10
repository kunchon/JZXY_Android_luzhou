package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.device;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.BaseActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;

public class DeviceActivity extends BaseActivity {


    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
