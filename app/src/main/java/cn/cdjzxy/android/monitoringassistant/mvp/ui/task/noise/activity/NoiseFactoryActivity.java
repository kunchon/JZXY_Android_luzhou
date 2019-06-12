package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment.NoiseBasic;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment.NoiseMonitorListFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment.NoiseOtherFileFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment.NoisePointListFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment.NoiseSourceListFragment;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;

public class NoiseFactoryActivity extends MyBaseViewPagerActivity {

    //噪声fragment对应值
    public static final int NOISE_FRAGMENT_INT_BASIC = 0;
    public static final int NOISE_FRAGMENT_INT_SOURCE = 1;
    public static final int NOISE_FRAGMENT_INT_SOURCE_EDIT = 2;
    public static final int NOISE_FRAGMENT_INT_POINT = 3;
    public static final int NOISE_FRAGMENT_INT_POINT_EDIT = 4;
    public static final int NOISE_FRAGMENT_INT_MONITOR = 5;
    public static final int NOISE_FRAGMENT_INT_MONITOR_EDIT = 6;
    public static final int NOISE_FRAGMENT_INT_MAP = 7;
    public static final int NOISE_FRAGMENT_INT_OTHER_FILE = 8;

    @BindView(R.id.tab_view)
    CustomTab tabView;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    NoiseBasic mBasicFragment;
    NoiseSourceListFragment sourceListFragment;
    NoisePointListFragment pointListFragment;
    NoiseMonitorListFragment monitorListFragment;
    NoiseOtherFileFragment otherFileFragment;

    boolean isNeedSave=false;

    @Override
    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        List<Fragment> mFragments=new ArrayList<>();
        mBasicFragment = new NoiseBasic();
        sourceListFragment = new NoiseSourceListFragment();
        pointListFragment = new NoisePointListFragment();
        monitorListFragment = new NoiseMonitorListFragment();
        otherFileFragment = new NoiseOtherFileFragment();
        mFragments.add(mBasicFragment);
        mFragments.add(sourceListFragment);
        mFragments.add(pointListFragment);
        mFragments.add(monitorListFragment);
        mFragments.add(otherFileFragment);
        return mFragments;
    }

    @Override
    protected void tabViewOnTabSelect(int position) {
        openFragment(position, true);
    }

    @Override
    protected List<Tab> initTabData() {
        List<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("基本信息");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == 1) {
                tab.setTabName("主要噪声源");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_source);
            } else if (i == 2) {
                tab.setTabName("监听点位");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_point);
            } else if (i == 3) {
                tab.setTabName("监测数据");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_monotor);
            } else if (i == 4) {
                tab.setTabName("测点示意图");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_sketch_map);
            } else if (i == 5) {
                tab.setTabName("附件");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_other);
            }
            tabs.add(tab);
        }
        return tabs;
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
        titleBar.setTitleMainText("工业企业厂界噪声监测记录");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back() {
        hideSoftInput();
        if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_SOURCE_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_SOURCE,true);
        } else if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_POINT_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_POINT,true);
        } else if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_MONITOR_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_MONITOR,true);
        } else if (isNeedSave) {
            showSaveDataDialog();
        } else {
            finish();
        }
    }

    private void showSaveDataDialog() {
//        if (!mSample.getIsCanEdit())return;
        final Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("有数据更改，是否本地保存？")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //保存行为
//                        saveMySample(true);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
