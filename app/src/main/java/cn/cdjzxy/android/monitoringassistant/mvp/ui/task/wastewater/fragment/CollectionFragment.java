package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.WasteWaterCollectAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.WasteWaterActivity;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.TagsUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;

public class CollectionFragment extends SampleBaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_add_parallel)
    TextView tvAddParallel;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_add_blank)
    RelativeLayout btnAddBlank;
    @BindView(R.id.tv_print_label)
    TextView tvPrintLabel;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;
    @BindView(R.id.btn_add_new)
    RelativeLayout btn_add_new;
    @BindView(R.id.operate_layout)
    View operate_layout;
    private WasteWaterCollectAdapter mWasteWaterCollectAdapter;
    private SamplingContent selectSamplingDetail;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect, null);
    }

    @Subscriber(tag = EventBusTags.TAG_SAMPLING_UPDATE)
    private void updateSamplingData(boolean isModified) {
        if (isModified &&
                !RxDataTool.isEmpty(mWasteWaterCollectAdapter) &&
                !RxDataTool.isNull(mSampling.getSamplingContentResults())) {
            mWasteWaterCollectAdapter.refreshInfos(mSampling.getSamplingContentResults());
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!RxDataTool.isNull(mSampling) && !mSampling.getIsCanEdit()) {
            //operate_layout.setVisibility(View.INVISIBLE);
            btnAddParallel.setEnabled(false);
            btnAddBlank.setEnabled(false);
            btn_add_new.setEnabled(false);
            btnPrintLabel.setEnabled(true);
            btnAddParallel.setAlpha(0.5f);
            btnAddBlank.setAlpha(0.5f);
            btn_add_new.setAlpha(0.5f);
        } else {
            //operate_layout.setVisibility(View.VISIBLE);
            btnAddParallel.setEnabled(true);
            btnAddBlank.setEnabled(true);
            btn_add_new.setEnabled(true);
            btnPrintLabel.setEnabled(true);
            btnAddParallel.setAlpha(1f);
            btnAddBlank.setAlpha(1f);
            btn_add_new.setAlpha(1f);
        }
        initRecyclerViewData();
    }


    /**
     * 初始化RecyclerView
     */
    private void initRecyclerViewData() {
        if (RxDataTool.isEmpty(mSampling.getSamplingContentResults())) {
            mSampling.setSamplingContentResults(new ArrayList<>());
        }
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });
        /**选定状态**/
        mWasteWaterCollectAdapter = new WasteWaterCollectAdapter(mSampling.getSamplingContentResults(),
                mContext, new WasteWaterCollectAdapter.OnWasteWaterCollectListener() {
            @Override
            public void onSelected(View view, int position, boolean isSelected) {
                changSelectState(position, isSelected);
            }
        });
        mWasteWaterCollectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                strCollDetailFra(position);
            }
        });
        recyclerview.setAdapter(mWasteWaterCollectAdapter);
    }

    /**
     * 设置选中状态
     *
     * @param position
     * @param isSelected
     */
    private void changSelectState(int position, boolean isSelected) {
        List<SamplingContent> detailList = mSampling.getSamplingContentResults();
        if (!RxDataTool.isEmpty(detailList)) {
            for (SamplingContent detail : detailList) {
                detail.setSelected(false);
            }
            detailList.get(position).setSelected(isSelected);
            mWasteWaterCollectAdapter.notifyDataSetChanged();

            if (isSelected) {
                selectSamplingDetail = detailList.get(position);
            } else {
                selectSamplingDetail = null;
            }
        }
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label, R.id.btn_add_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_new://添加样品
                addNewCollection();//新增样品采集
                break;
            case R.id.btn_add_parallel://添加平行(需要选择普通样)
                addParallel();
                break;
            case R.id.btn_add_blank://添加空白(不用选择普通样品)
                addBlankSample();
                break;
            case R.id.btn_print_label:
                //  printLabel();
                break;
            default:
                break;
        }
    }

    /**
     * 新增样品采集
     */
    private void addNewCollection() {
        if (TextUtils.isEmpty(mSampling.getAddressId())) {
            ArtUtils.makeText(getContext(), "请先选择采样点位");
            return;
        }
        strCollDetailFra(-1);

    }

    /**
     * 跳转到样品采集详情/编辑界面
     *
     * @param position 样品item
     */
    private void strCollDetailFra(int position) {
        HawkUtil.putInt(EventBusTags.TAG_WASTE_WATER_COLLECTION, position);
        EventBus.getDefault().post(WasteWaterActivity.FRAGMENT_ITEM_INT_COLLECTION_DETAIL, EventBusTags.TAG_WASTE_WATER_COLLECTION);
    }


    /**
     * 添加空白样
     */
    private void addBlankSample() {
        //创建SamplingContent
        SamplingContent samplingContent = new SamplingContent();
        samplingContent.setId(UUID.randomUUID().toString());
        samplingContent.setProjectId(mSampling.getProjectId());
        samplingContent.setSamplingType(2);
        samplingContent.setSamplingId(mSampling.getId());
        //添加空白默认频次为0
        samplingContent.setSampingCode(SamplingUtil.createSamplingCode(mSampling));
        samplingContent.setFrequecyNo(0);
        samplingContent.setOrderIndex(SamplingUtil.createOrderIndex(mSampling));
        samplingContent.setMonitemId("");
        samplingContent.setMonitemName("");
        samplingContent.setSenceMonitemId("");
        samplingContent.setSenceMonitemName("");

        //创建SamplingDetail
        SamplingDetail detail = new SamplingDetail();
        detail.setProjectId(samplingContent.getProjectId());
        detail.setSamplingType(samplingContent.getSamplingType());
        detail.setId(UUID.randomUUID().toString());
        detail.setSamplingId(mSampling.getId());
        //添加空白默认频次为0
        detail.setSampingCode(samplingContent.getSampingCode());
        detail.setFrequecyNo(0);
        detail.setOrderIndex(samplingContent.getOrderIndex());
        DBHelper.get().getSamplingDetailDao().insert(detail);
        detail.setMonitemId("");
        detail.setMonitemName("");
        detail.setIsAddPreserve(false);
        detail.setPreservative("否");
        detail.setIsCompare(false);
        mSampling.getSamplingDetailResults().add(detail);

        //数据处理
        if (RxDataTool.isEmpty(mSampling.getSamplingContentResults())) {
            mSampling.setSamplingContentResults(new ArrayList<>());
        }
        mSampling.getSamplingContentResults().add(samplingContent);
        DBHelper.get().getSamplingContentDao().insert(samplingContent);
        //刷新界面
        if (mWasteWaterCollectAdapter != null) {
            mWasteWaterCollectAdapter.refreshInfos(mSampling.getSamplingContentResults());
            //mWasteWaterCollectAdapter.notifyDataSetChanged();
        } else {
            initRecyclerViewData();
        }

    }

    /**
     * 添加平行(需要选择普通样)
     */
    private void addParallel() {
        if (RxDataTool.isNull(selectSamplingDetail)) {
            ArtUtils.makeText(getContext(), "请先选择一项样品");
            return;
        }
        if (selectSamplingDetail.getSamplingType() != 0) {
            ArtUtils.makeText(getContext(), "请选择普通样品");
            return;
        }
        if (isPxSampleAdded(selectSamplingDetail)) {
            ArtUtils.makeText(getContext(), "该普通样下面已经添加平行样，请另外选择");
            return;
        }
        addTheSameSample();
    }

    /**
     * 判断某个普通样是否添加平行样:频次相同， code不同
     *
     * @param selectSamplingDetail
     * @return
     */
    private boolean isPxSampleAdded(SamplingContent selectSamplingDetail) {
        boolean flag = false;
        List<SamplingContent> samplingList = mSampling.getSamplingContentResults();
        if (!RxDataTool.isEmpty(samplingList)) {
            for (SamplingContent detail : samplingList) {
                if (selectSamplingDetail.getFrequecyNo() == detail.getFrequecyNo() && detail.getSamplingType() == 1) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 添加平行样品:频次相同，但是SamplingCode不同
     */
    private void addTheSameSample() {
        SamplingContent samplingContent = new SamplingContent();
        samplingContent.setId(UUID.randomUUID().toString());
        samplingContent.setProjectId(selectSamplingDetail.getProjectId());
        samplingContent.setSamplingId(selectSamplingDetail.getSamplingId());
        samplingContent.setSampingCode(SamplingUtil.createSamplingCode(mSampling));
        samplingContent.setFrequecyNo(selectSamplingDetail.getFrequecyNo());
        samplingContent.setDescription(selectSamplingDetail.getDescription());
        samplingContent.setSamplingType(1);
        samplingContent.setIsCompare(selectSamplingDetail.getIsCompare());
        samplingContent.setIsAddPreserve(selectSamplingDetail.getIsAddPreserve());
        samplingContent.setMonitemName(selectSamplingDetail.getMonitemName());
        samplingContent.setMonitemId(selectSamplingDetail.getMonitemId());
        samplingContent.setAddressName(selectSamplingDetail.getAddressName());
        samplingContent.setAddresssId(selectSamplingDetail.getAddresssId());
        samplingContent.setSamplingTime(selectSamplingDetail.getSamplingTime());
        samplingContent.setOrderIndex(SamplingUtil.createOrderIndex(mSampling));
        samplingContent.setSampleCollection(selectSamplingDetail.getSampleCollection());
        samplingContent.setSampleAcceptance(selectSamplingDetail.getSampleAcceptance());
        samplingContent.setPreservative(selectSamplingDetail.getPreservative());

//        //金蓉说平行数据不带现场监测项目
//        samplingContent.setSenceMonitemName(selectSamplingDetail.getSenceMonitemName());
//        samplingContent.setSenceMonitemId(selectSamplingDetail.getSenceMonitemId());

        //包括监测项目和现场监测项目
        int count = 0;
        String[] monitemIds = selectSamplingDetail.getMonitemId().split(",");
        if (!RxDataTool.isEmpty(monitemIds)) {
            count += monitemIds.length;
            for (String itemId : monitemIds) {
                SamplingDetail detail = new SamplingDetail();
                detail.setProjectId(samplingContent.getProjectId());
                detail.setId(UUID.randomUUID().toString());
                detail.setSamplingId(samplingContent.getSamplingId());
                detail.setSampingCode(samplingContent.getSampingCode());
                detail.setFrequecyNo(samplingContent.getFrequecyNo());
                detail.setDescription(samplingContent.getDescription());
                detail.setSamplingType(1);
                detail.setIsCompare(samplingContent.getIsCompare());
                detail.setIsAddPreserve(samplingContent.getIsAddPreserve());
                detail.setMonitemName(TagsUtils.getTags(itemId).getName());
                detail.setMonitemId(itemId);
                detail.setAddressName(samplingContent.getAddressName());
                detail.setAddresssId(samplingContent.getAddresssId());
                detail.setIsSenceAnalysis(false);
                detail.setSamplingTime(selectSamplingDetail.getSamplingTime());
                detail.setOrderIndex(samplingContent.getOrderIndex());
                detail.setSampleCollection(samplingContent.getSampleCollection());
                detail.setSampleAcceptance(samplingContent.getSampleAcceptance());
                detail.setPreservative(samplingContent.getPreservative());
                //计算SamplingCount为非现场监测的样品数量
                detail.setSamplingCount(samplingContent.getSamplingCount());
                DBHelper.get().getSamplingDetailDao().insert(detail);
                mSampling.getSamplingDetailResults().add(detail);
            }
        }

        //添加平行的样品数量和被选择的是一样的
        samplingContent.setSamplingCount(selectSamplingDetail.getSamplingCount());
        DBHelper.get().getSamplingContentDao().insert(samplingContent);
        mSampling.getSamplingContentResults().add(samplingContent);
        mWasteWaterCollectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
