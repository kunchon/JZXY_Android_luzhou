package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.BottleMonItemActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.PlaceActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.WasteWaterActivity;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.BottleSplitUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.onactivityresult.AvoidOnResult;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class BottleSplitDetailFragment extends SampleBaseFragment {

    @BindView(R.id.my_layout_sample_project)
    MyDrawableLinearLayout sample_project;//监测项目
    @BindView(R.id.my_layout_sample_quantity)
    MyDrawableLinearLayout sample_quantity;//采样量
    @BindView(R.id.my_layout_sample_vessel)
    MyDrawableLinearLayout sample_vessel;//容器
    @BindView(R.id.my_layout_sample_number)
    MyDrawableLinearLayout sample_number;//数量
    @BindView(R.id.my_layout_sample_method)
    MyDrawableLinearLayout sample_method;//保存方法
    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout sample_date;//保存时间
    @BindView(R.id.my_layout_sample_place)
    MyDrawableLinearLayout sample_place;//分析地点
    @BindView(R.id.operate_layout)
    View operate_layout;
    private SamplingFormStand bottleSplit;
    private String monItemId;//未选中的itemId
    private int bottleListPosition = -1;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!RxDataTool.isNull(mSampling) && !mSampling.getIsCanEdit()) {
            operate_layout.setVisibility(View.INVISIBLE);
        } else {
            operate_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sample_project.setRightTextStr("");
            sample_quantity.setEditTextStr("");
            sample_vessel.setEditTextStr("");
            sample_number.setEditTextStr("");
            sample_method.setEditTextStr("");
            sample_place.setRightTextStr("");
            setBottleSplitDetail();
        }
    }

    private void setBottleSplitDetail() {
        List<SamplingFormStand> samplingFormStandResults = mSampling.getSamplingFormStandResults();
        bottleListPosition = HawkUtil.getInt(EventBusTags.TAG_WASTE_WATER_COLLECTION);
        bottleSplit = samplingFormStandResults.get(bottleListPosition);
        sample_project.setRightTextStr(RxDataTool.isEmpty(bottleSplit.getMonitemName()) ? "" : bottleSplit.getMonitemName());
        sample_quantity.setEditTextStr(RxDataTool.isEmpty(bottleSplit.getSamplingAmount()) ? "" : bottleSplit.getSamplingAmount());
        sample_vessel.setEditTextStr(RxDataTool.isEmpty(bottleSplit.getContainer()) ? "" : bottleSplit.getContainer());
        sample_number.setEditTextStr(bottleSplit.getCount() + "");
        sample_method.setEditTextStr(RxDataTool.isEmpty(bottleSplit.getSaveMehtod()) ? "" : bottleSplit.getSaveMehtod());
        sample_date.setEditTextStr(RxDataTool.isEmpty(bottleSplit.getSaveTimes()) ? "" : bottleSplit.getSaveTimes());
        sample_place.setRightTextStr(RxDataTool.isEmpty(bottleSplit.getAnalysisSite()) ? "" : bottleSplit.getAnalysisSite());

    }

    @OnClick({R.id.btn_back, R.id.my_layout_sample_place, R.id.my_layout_sample_project, R.id.btn_save, R.id.btn_delete})
    public void onClick(View view) {
        hideSoftInput();
        if (view.getId() == R.id.btn_back) {
            EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
            return;
        }
        switch (view.getId()) {
            case R.id.btn_back:
                back();
                break;
            case R.id.my_layout_sample_project:
                selectMonitorItems();
                break;
            case R.id.my_layout_sample_place:
                showAnalysisPlace();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_save:
                save();
                break;
            default:
                break;

        }
    }

    /**
     * 保存
     */
    private void save() {
        if (!isCanSave()) {
            return;
        }
        showLoadingDialog("请等待...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveSplitDetail();
            }
        }).start();
    }

    private void saveSplitDetail() {
        bottleSplit.setSamplingId(mSampling.getId());
        bottleSplit.setContainer(sample_vessel.getEditTextStr());
        bottleSplit.setCount(Integer.parseInt(sample_number.getEditTextStr()));
        bottleSplit.setSaveMehtod(sample_method.getEditTextStr());
        bottleSplit.setSaveTimes(sample_date.getEditTextStr());
        bottleSplit.setAnalysisSite(sample_place.getRightTextViewStr());
        bottleSplit.setSamplingAmount(sample_quantity.getEditTextStr());
        bottleSplit.setStandNo(BottleSplitUtils.generateNewBottleIndex(mSampling.getId()));

        if (monItemId == null || monItemId.equals("")) {
            if (DbHelpUtils.getSamplingFormStandDaoForId(bottleSplit.getId()) != null) {
                DBHelper.get().getSamplingFormStandDao().update(bottleSplit);
            }
            mSampling.getSamplingFormStandResults().set(bottleListPosition, bottleSplit);
        } else {
            //生成分瓶信息
            List<SamplingFormStand> list = DbHelpUtils.getSamplingFormStandListForSampId(mSampling.getId());
            if (!RxDataTool.isEmpty(list)) {
                DBHelper.get().getSamplingFormStandDao().deleteInTx(list);
            }
            if (mSampling.getSamplingFormStandResults() != null) {
                mSampling.getSamplingFormStandResults().clear();
            }
            if (monItemId != null && !monItemId.equals("")) {
                String[] monItemIds = monItemId.split(",");
                for (String s : monItemIds) {
                    BottleSplitUtils.createAndUpdateBottle(s, mSampling);
                }
            }
            bottleSplit.setIndex(BottleSplitUtils.generateNewBottleIndex(mSampling.getId()));
            DBHelper.get().getSamplingFormStandDao().insert(bottleSplit);
            //这里要从新生成样品数量
            List<SamplingContent> samplingContentList = DbHelpUtils.getSamplingContentList(mSampling.getId(),
                    mSampling.getProjectId());
            if (mSampling.getSamplingContentResults() == null || mSampling.getSamplingContentResults().size() == 0) {
                mSampling.setSamplingContentResults(samplingContentList);
            }
            if (!RxDataTool.isEmpty(samplingContentList))
                DBHelper.get().getSamplingContentDao().deleteInTx(samplingContentList);
            List<SamplingContent> contentList = BottleSplitUtils.setSamplingCountList(mSampling);
            mSampling.setSamplingContentResults(contentList);
            if (!RxDataTool.isEmpty(contentList))
                DBHelper.get().getSamplingContentDao().insertInTx(contentList);
        }
        mSampling.setSamplingFormStandResults(DbHelpUtils.getSamplingFormStandListForSampId(mSampling.getId()));
        mSampling.setIsFinish(SamplingUtil.isSamplingFinsh(mSampling));
        mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
        Sampling sampling = DbHelpUtils.getDbSampling(mSampling.getId());
        if (RxDataTool.isNull(sampling)) {
            mSampling.setId(UUID.randomUUID().toString());
            DBHelper.get().getSamplingDao().insert(mSampling);
        } else {
            DBHelper.get().getSamplingDao().update(mSampling);
        }
        showMessage("保存成功");
        startBottleSplitFra(true);
    }

    /**
     * 判断状态是否可保存
     *
     * @return
     */
    private boolean isCanSave() {
        if (TextUtils.isEmpty(bottleSplit.getMonitemIds())) {
            showMessage("请选择监测项目");
            return false;
        }

        return true;
    }

    /**
     * 删除
     */
    private void delete() {
        if (RxDataTool.isEmpty(mSampling.getSamplingFormStandResults())) {
            showMessage("请先添加分瓶数据");
            return;
        }
        showDeleteDialog();
    }

    /**
     * 弹出删除对话框
     */
    private void showDeleteDialog() {
        showDialog("提示", "是否删除采集数据", "确定", "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoadingDialog("请等待....");
                        deleteBottleDetail();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    /**
     * 删除分瓶信息
     */
    private void deleteBottleDetail() {

        SamplingFormStand bottleSplit = mSampling.getSamplingFormStandResults().get(bottleListPosition);
        SamplingFormStand samplingBottleSplit = DbHelpUtils.getSamplingFormStandDaoForId(bottleSplit.getId());

        if (!RxDataTool.isNull(samplingBottleSplit)) {
            DBHelper.get().getSamplingFormStandDao().delete(samplingBottleSplit);
        }

        mSampling.getSamplingFormStandResults().remove(bottleListPosition);

        mSampling.setIsFinish(SamplingUtil.isSamplingFinsh(mSampling));
        mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
        Sampling sampling = DbHelpUtils.getDbSampling(mSampling.getId());
        if (RxDataTool.isNull(sampling)) {
            DBHelper.get().getSamplingDao().insert(mSampling);
        } else {
            DBHelper.get().getSamplingDao().update(mSampling);
        }

        //更新index
        BottleSplitUtils.updateAllBottleIndex(mSampling);
        showMessage("删除成功");
        startBottleSplitFra(true);
    }

    /**
     * 跳转到分瓶列表界面
     *
     * @param b
     */
    private void startBottleSplitFra(boolean b) {
        closeLoadingDialog();
        EventBus.getDefault().post(b, EventBusTags.TAG_SAMPLING_UPDATE);
        EventBus.getDefault().post(WasteWaterActivity.FRAGMENT_ITEM_INT_SQlLIT,
                EventBusTags.TAG_WASTE_WATER_COLLECTION);
    }

    /**
     * 分析地点选择
     */
    private void showAnalysisPlace() {
        Intent intent = new Intent(getContext(), PlaceActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!RxDataTool.isEmpty(data.getStringExtra("place"))) {
                        bottleSplit.setAnalysisSite(data.getStringExtra("place"));
                        sample_place.setRightTextStr(bottleSplit.getAnalysisSite());
                    }
                }
            }
        });
    }

    /**
     * 选择监测项目
     */
    private void selectMonitorItems() {
        Intent intent = new Intent(getContext(), BottleMonItemActivity.class);
        intent.putExtra("tagId", mSampling.getParentTagId());
        intent.putExtra("samplingId", mSampling.getId());
        if (!RxDataTool.isEmpty(bottleSplit.getMonitemIds())) {
            intent.putExtra("selectItems", bottleSplit.getMonitemIds());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    bottleSplit.setMonitemName(data.getStringExtra("selectMonItemName"));
                    bottleSplit.setMonitemIds(data.getStringExtra("selectMonItemId"));
                    sample_project.setRightTextStr(bottleSplit.getMonitemName());
                    monItemId = data.getStringExtra("MonItemId");//用户没选择的监测项目
                }
            }
        });
    }

    /**
     * 退出当前页面
     */
    private void back() {
        startBottleSplitFra(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
