package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.PrecipitationBasic;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;

public class PrecipitationActivity extends MyBaseViewPagerActivity {


//    @BindView(R.id.tabview)
//    CustomTab tabview;
    @BindView(R.id.layout)
    LinearLayout layout;
//    @BindView(R.id.viewPager)
//    NoScrollViewPager viewPager;

    private TitleBarView mTitleBarView;


    private PrecipitationBasic mBasicFragment;
    private CollectionFragment mCollectionFragment;
    private CollectionDetailFragment mCollectionDetailFragment;

    @Override
    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        List<Fragment> mFragments=new ArrayList<>();
        mBasicFragment=new PrecipitationBasic();
        mCollectionFragment=new CollectionFragment();
        mCollectionDetailFragment=new CollectionDetailFragment();
        mFragments.add(mBasicFragment);
        mFragments.add(mCollectionFragment);
        mFragments.add(mCollectionDetailFragment);
        return mFragments;
    }

    @Override
    protected void tabViewOnTabSelect(int position) {
        openFragment(position, true);
    }

    @Override
    protected List<Tab> initTabData() {
        List<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("基本信息");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == 1) {
                tab.setTabName("样品采集");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_source);
            }
            tabs.add(tab);
        }
        return tabs;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_precipitation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("降水采样交接记录单");
        mTitleBarView.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }


    @Subscriber(tag = EventBusTags.TAG_INSTRUMENTAL_RECORD)
    private void updateCollectFragment(int position) {
        openFragment(position, true);
    }

    /**
     * 退出方法
     */
    private void onBack() {
        hideSoftInput();
//        if (viewPager.getCurrentItem() == 2) {
//            openFragment(1,false);
//            return;
//        }
        finish();
    }
}
