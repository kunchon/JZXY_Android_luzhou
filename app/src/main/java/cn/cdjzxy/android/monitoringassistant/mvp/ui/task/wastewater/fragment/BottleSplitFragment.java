package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.WasteWaterBottleAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.WasteWaterActivity;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

public class BottleSplitFragment extends SampleBaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;
    private WasteWaterBottleAdapter mWasteWaterBottleAdapter;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnAddParallel.setVisibility(View.GONE);
        btnPrintLabel.setVisibility(View.GONE);
        tvAddBlank.setText("添加");
        tvAddBlank.setVisibility(View.GONE);
        initRecyclerViewData();
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @Subscriber(tag = EventBusTags.TAG_SAMPLING_UPDATE)
    private void updateSamplingData(boolean isModified) {
        if (isModified &&
                !RxDataTool.isEmpty(mWasteWaterBottleAdapter) &&
                !RxDataTool.isNull(mSampling.getSamplingFormStandResults())) {
            mWasteWaterBottleAdapter.refreshInfos(mSampling.getSamplingFormStandResults());
        }
    }

    private void initRecyclerViewData() {

        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<SamplingFormStand> standList = mSampling.getSamplingFormStandResults();
        if (RxDataTool.isEmpty(standList)) standList = new ArrayList<>();
        mWasteWaterBottleAdapter = new WasteWaterBottleAdapter(standList, mContext);
        mWasteWaterBottleAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                HawkUtil.putInt(EventBusTags.TAG_WASTE_WATER_COLLECTION, position);
                EventBus.getDefault().post(WasteWaterActivity.FRAGMENT_ITEM_INT_SQlLIT_DETAIL,
                        EventBusTags.TAG_WASTE_WATER_COLLECTION);
            }
        });
        recyclerview.setAdapter(mWasteWaterBottleAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
