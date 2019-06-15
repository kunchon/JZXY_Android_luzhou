package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment.PrecipitationBasic;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

public class PrecipitationActivity extends MyBaseViewPagerActivity {

    private TitleBarView mTitleBarView;

    private PrecipitationBasic mBasicFragment;
    private CollectionFragment mCollectionFragment;
    private CollectionDetailFragment mCollectionDetailFragment;
    public Project mProject;
    private Sampling mSampling;

    @Override
    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        List<Fragment> mFragments = new ArrayList<>();
        mBasicFragment = new PrecipitationBasic();
        mCollectionFragment = new CollectionFragment();
        mCollectionDetailFragment = new CollectionDetailFragment();
        mBasicFragment.setData(mSampling);
        mCollectionFragment.setData(mSampling);
        mCollectionDetailFragment.setData(mSampling);
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
                tab.setResId(R.mipmap.icon_samp_coll);
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
        String projectId = getIntent().getStringExtra(SamplingUtil.INTENT_PROJECT_ID);
        String formSelectId = getIntent().getStringExtra(SamplingUtil.INTENT_FORM_SELECT_ID);
        String samplingId = getIntent().getStringExtra(SamplingUtil.INTENT_SAMPLE_ID);
        boolean isNewCreate = getIntent().getBooleanExtra(SamplingUtil.INTENT_IS_NEW_CREATE, false);
        mProject = DbHelpUtils.getDbProject(projectId);

        Project mProject = DbHelpUtils.getDbProject(projectId);
        if (isNewCreate) {
            mSampling = SamplingUtil.createNewSampling(projectId, formSelectId);
        } else {
            mSampling = DbHelpUtils.getDbSampling(samplingId);
            getJsData();
        }

    }

    /**
     * 获取降水数据
     * 因为服务器返回很多非降水的数据在，app暂时用不着 所以直接删除
     * 积累后就会很多  app只用了降水的数据
     */
    private void getJsData() {
        try {
            List<SamplingDetail> samplingDetails = DbHelpUtils.getSamplingDetailList(mSampling.getId());

            List<SamplingDetail> detailList = new ArrayList<>();
            List<SamplingDetail> deleteList = new ArrayList<>();
            if (!RxDataTool.isEmpty(samplingDetails)) {
                for (SamplingDetail detail : samplingDetails) {
                    if (detail != null && detail.getMonitemName() != null && detail.getMonitemName().equals("降水量")) {
                        detailList.add(detail);
                    } else {
                        deleteList.add(detail);
                    }
                }
            }
            mSampling.setSamplingDetailResults(detailList);
            DBHelper.get().getSamplingDetailDao().deleteInTx(deleteList);
        } catch (Exception e) {
            Log.e(TAG, "getJsData: " + e.toString());
        }
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
