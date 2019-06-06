package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;


import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyRefreshActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;

import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental.fragment.InstrumentalBasic;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;

/**
 * 现场监测仪器法
 */
public class InstrumentalActivity extends MyBaseViewPagerActivity {


    private String projectId;
    private String formSelectId;
    private String samplingId;
    private Sampling mSampling;

    protected static final int FRAGMENT_ITEM_INT_BASIC = 0;//基础信息
    protected static final int FRAGMENT_ITEM_INT_RECORD = FRAGMENT_ITEM_INT_BASIC + 1;
    protected static final int FRAGMENT_ITEM_INT_RECORD_DETAIL = FRAGMENT_ITEM_INT_RECORD + 1;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("仪器法原始记录");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_tab_viewpager;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        layout.scrollTo(0, StatusBarUtil.getStatusBarHeight(this));
        projectId = getIntent().getStringExtra(SamplingUtil.INTENT_PROJECT_ID);
        formSelectId = getIntent().getStringExtra(SamplingUtil.INTENT_FORM_SELECT_ID);
        samplingId = getIntent().getStringExtra(SamplingUtil.INTENT_SAMPLE_ID);
        boolean isNewCreate = getIntent().getBooleanExtra(SamplingUtil.INTENT_IS_NEW_CREATE, false);

        if (isNewCreate) {
            //是否是新建
            mSampling = SamplingUtil.createInstrumentalSampling(projectId, formSelectId);
        } else {
            //从数据库加载
            mSampling = DbHelpUtils.getDbSampling(samplingId);

            //加载详细信息
            List<SamplingDetail> samplingDetails = DbHelpUtils.getSamplingDetailList(samplingId);
            mSampling.setSamplingDetailYQFs(samplingDetails);
        }

        if (mSampling.getIsCanEdit()) {
            //可编辑

        }

    }

    @Override
    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new InstrumentalBasic());

        return list;
    }

    @Override
    protected void tabViewOnTabSelect(int position) {

    }

    @Override
    protected List<Tab> initTabData() {
        List<Tab> tabList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Tab tab = new Tab();
            if (i == FRAGMENT_ITEM_INT_BASIC) {
                tab.setTabName("基础信息");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == FRAGMENT_ITEM_INT_RECORD) {
                tab.setTabName("监测结果");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            }
            tabList.add(tab);
        }
        return tabList;
    }


    @Subscriber(tag = EventBusTags.TAG_INSTRUMENTAL_RECORD)
    private void updateCollectFragment(int position) {
openFragment(position,true);
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
