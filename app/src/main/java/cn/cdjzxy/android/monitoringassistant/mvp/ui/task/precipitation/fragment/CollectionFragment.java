package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PrecipitationCollectAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

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
    private List<SamplingDetail> detailList;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnAddParallel.setVisibility(View.GONE);
        btn_add_new.setVisibility(View.GONE);
        tvAddBlank.setText("添加");
        getData();
        initRecyclerViewData();
    }

    private void initRecyclerViewData() {
        detailList = new ArrayList<>();
        if (!mSampling.getIsCanEdit()) {
            btnAddParallel.setVisibility(View.GONE);
            //btnAddBlank.setVisibility(View.GONE);
            //btnPrintLabel.setVisibility(View.GONE);
            btnAddBlank.setEnabled(false);
            btnPrintLabel.setEnabled(true);
            btnAddBlank.setAlpha(0.5f);
        } else {
            btnAddBlank.setEnabled(true);
            btnPrintLabel.setEnabled(true);
            btnAddBlank.setAlpha(1f);
        }

        if (mSampling.getSamplingDetailResults() == null) {
            return;
        }

        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        PrecipitationCollectAdapter mPrecipitationCollectAdapter = new PrecipitationCollectAdapter(detailList, mContext);
        mPrecipitationCollectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                EventBus.getDefault().post(2, EventBusTags.TAG_PRECIPITATION_COLLECTION);

            }
        });

        recyclerview.setAdapter(mPrecipitationCollectAdapter);
    }

    private void getData() {
        if (detailList == null) detailList = new ArrayList<>();
        else detailList.clear();
        for (SamplingDetail detail : mSampling.getSamplingDetailResults()) {
            if (detail != null && detail.getMonitemName() != null &&
                    detail.getMonitemName().equals("降水量")) {//毛杨说的这个表单  这个只有降水量
                detailList.add(detail);
            }
        }
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.btn_add_parallel:
                break;
            case R.id.btn_add_blank:
                addBlank();//添加空白
                break;
            case R.id.btn_print_label:
               printLabel();
                break;
        }
    }

    /**
     * 打印标签
     */
    private void printLabel() {
//        Gson gson = new Gson();
//        //构建标签数据
//        String labelStr = gson.toJson(buildPrintLabelList(mSampling));
//        //构建封条数据
//        String sealStr = gson.toJson(buildSealInfo(PrecipitationActivity.mProject));
//
//        Intent intent = new Intent(getContext(), LabelPrintActivity.class);
//        intent.putExtra(LabelPrintActivity.LABEL_JSON_DATA, labelStr);
//        intent.putExtra(LabelPrintActivity.SEAL_JSON_DATA, sealStr);
//        ArtUtils.startActivity(intent);
    }

    /**
     * 添加空白
     */
    private void addBlank() {
        if (RxDataTool.isEmpty(mSampling.getSamplingTimeBegin())) {
            ArtUtils.makeText(getContext(), "请先选择采样日期");
            return;
        }
        //添加空白
        if (TextUtils.isEmpty(mSampling.getAddressName())) {
            ArtUtils.makeText(getContext(), "请先选择采样点位");
            return;
        }
        EventBus.getDefault().post(2, EventBusTags.TAG_PRECIPITATION_COLLECTION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
