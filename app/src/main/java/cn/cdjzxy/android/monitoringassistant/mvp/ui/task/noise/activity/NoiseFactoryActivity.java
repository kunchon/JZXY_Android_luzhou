package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;

public class NoiseFactoryActivity extends MyBaseViewPagerActivity {

    @BindView(R.id.tab_view)
    CustomTab tabView;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    @Override
    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        return null;
    }

    @Override
    protected void tabViewOnTabSelect(int position) {

    }

    @Override
    protected List<Tab> initTabData() {
        return null;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_noise_factory;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
