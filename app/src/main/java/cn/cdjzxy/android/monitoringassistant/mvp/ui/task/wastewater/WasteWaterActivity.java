package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment.BottleSplitDetailFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment.BottleSplitFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment.CollectionDetailFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment.CollectionFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment.WasteWaterBasic;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;

public class WasteWaterActivity extends MyBaseViewPagerActivity {

    @BindView(R.id.tabview)
    CustomTab tabview;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;


    private String projectId;
    private String formSelectId;
    private String samplingId;
    private Sampling mSampling;

    protected static final int FRAGMENT_ITEM_INT_BASIC = 0;
    protected static final int FRAGMENT_ITEM_INT_COLLECTION = FRAGMENT_ITEM_INT_BASIC + 1;
    protected static final int FRAGMENT_ITEM_INT_SQlLIT = FRAGMENT_ITEM_INT_COLLECTION + 1;
    protected static final int FRAGMENT_ITEM_INT_COLLECTION_DETAIL = FRAGMENT_ITEM_INT_SQlLIT + 1;
    protected static final int FRAGMENT_ITEM_INT_SQlLIT_DETAIL = FRAGMENT_ITEM_INT_COLLECTION_DETAIL + 1;


    @Override

    protected boolean viewPagerSetCurrentItem() {
        return false;
    }

    @Override
    protected List<Fragment> initFragment() {
        List<Fragment> list = new ArrayList<>();
        WasteWaterBasic basic = new WasteWaterBasic();
        CollectionFragment collectionFragment = new CollectionFragment();
        BottleSplitFragment splitFragment = new BottleSplitFragment();

        CollectionDetailFragment collectionDetailFragment = new CollectionDetailFragment();
        BottleSplitDetailFragment bottleSplitDetailFragment = new BottleSplitDetailFragment();
        basic.setData(mSampling);
        list.add(basic);
        list.add(collectionFragment);
        list.add(splitFragment);
        list.add(collectionDetailFragment);
        list.add(bottleSplitDetailFragment);
        return list;
    }

    @Override
    protected void tabViewOnTabSelect(int position) {
        openFragment(position, true);
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
            } else if (i == FRAGMENT_ITEM_INT_COLLECTION) {
                tab.setTabName("样品采集");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == FRAGMENT_ITEM_INT_SQlLIT) {
                tab.setTabName("分瓶信息");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_sqilit);
            }
            tabList.add(tab);
        }
        return tabList;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_waste_water;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
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
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
