package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.MonItemActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.WasteWaterActivity;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.BottleSplitUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.TagsUtils;
import cn.cdjzxy.android.monitoringassistant.utils.TimePickerViewUtils;
import cn.cdjzxy.android.monitoringassistant.utils.onactivityresult.AvoidOnResult;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class CollectionDetailFragment extends SampleBaseFragment {
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout sample_code;//样品编码
    @BindView(R.id.my_layout_sample_time)
    MyDrawableLinearLayout sample_Time;//采样时间
    @BindView(R.id.my_layout_sample_frequency)
    MyDrawableLinearLayout sample_frequency;//频次
    @BindView(R.id.my_layout_sample_quality)
    MyDrawableLinearLayout sample_quality;//质控
    @BindView(R.id.my_layout_sample_monitor_items)
    MyDrawableLinearLayout sample_monitor_items;//检测项目
    @BindView(R.id.my_layout_sample_monitor)
    MyDrawableLinearLayout sample_monitor;//现场检测
    @BindView(R.id.text_view_sample_add_preserve)
    TextView text_view_sample_add_preserve;//是否添加保存剂
    @BindView(R.id.sample_add_preserve)
    CheckedTextView sample_add_preserve;
    @BindView(R.id.text_view_sample_compare_monitor)
    TextView text_view_sample_compare_monitor;//是否对比监测
    @BindView(R.id.sample_compare_monitor)
    CheckedTextView sample_compare_monitor;
    @BindView(R.id.sample_mark)
    TextView sample_mark;
    @BindView(R.id.operate_layout)
    View operate_layout;

    private SamplingContent samplingDetail;
    private int fsListPosition = -1;
    private String oldItemId;//对比之前的监测项目。进行比较是否需要重新分瓶

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!RxDataTool.isNull(mSampling) && !mSampling.getIsCanEdit()) {
            operate_layout.setVisibility(View.INVISIBLE);
        } else {
            operate_layout.setVisibility(View.VISIBLE);
        }

        sample_add_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_add_preserve.setChecked(!sample_add_preserve.isChecked());
                samplingDetail.setIsAddPreserve(sample_add_preserve.isChecked());
                setViewStyleDrawable(samplingDetail.isAddPreserve(), text_view_sample_add_preserve);
                if (samplingDetail.isAddPreserve()) {
                    samplingDetail.setPreservative("是");
                } else {
                    samplingDetail.setPreservative("否");
                }

            }
        });

        sample_compare_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_compare_monitor.setChecked(!sample_compare_monitor.isChecked());
                samplingDetail.setIsCompare(sample_compare_monitor.isChecked());
                setViewStyleDrawable(samplingDetail.getIsCompare(), text_view_sample_compare_monitor);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sample_code.setRightTextStr("");
            sample_Time.setRightTextStr("");
            sample_frequency.setRightTextStr("");
            sample_quality.setRightTextStr("");
            sample_monitor_items.setRightTextStr("");
            sample_monitor.setRightTextStr("");
            sample_mark.setText("");
            setSamplingDetail();
        }
    }

    private void setSamplingDetail() {
        List<SamplingContent> samplingDetailResults = mSampling.getSamplingContentResults();
        fsListPosition = HawkUtil.getInt(EventBusTags.TAG_WASTE_WATER_COLLECTION);
        if (fsListPosition == -1) {
            samplingDetail = new SamplingContent();
            samplingDetail.setId(UUID.randomUUID().toString());
            sample_code.setRightTextStr(SamplingUtil.createSamplingCode(mSampling));
            sample_frequency.setRightTextStr(SamplingUtil.createFrequency(mSampling) + "");
            sample_quality.setRightTextStr(SamplingUtil.SAMPLING_TYPE_PT);
            samplingDetail.setSamplingType(0);
            samplingDetail.setOrderIndex(SamplingUtil.createOrderIndex(mSampling));
            samplingDetail.setFrequecyNo(SamplingUtil.createFrequency(mSampling));
            //设置是否添加保存剂，是否对比监测
            samplingDetail.setPreservative("否");
            samplingDetail.setIsAddPreserve(false);
            samplingDetail.setIsCompare(false);
            setViewStyleDrawable(false, text_view_sample_add_preserve);
            setViewStyleDrawable(false, text_view_sample_compare_monitor);
            samplingDetail.setSamplingTime("");
            //新增时要将监测项目和现场监测项目带过来
            setDefaultMonitor();
        } else {
            samplingDetail = samplingDetailResults.get(fsListPosition);
            sample_code.setRightTextStr(samplingDetail.getSampingCode());
            sample_frequency.setRightTextStr(samplingDetail.getFrequecyNo() + "");
            sample_monitor_items.setRightTextStr(samplingDetail.getMonitemName());
            sample_monitor.setRightTextStr(samplingDetail.getSenceMonitemName());
            sample_Time.setRightTextStr(samplingDetail.getSamplingTime());
            if (!RxDataTool.isNull(samplingDetail.getPreservative()) &&
                    samplingDetail.getPreservative().equals("是")) {
                sample_add_preserve.setChecked(true);
                samplingDetail.setIsAddPreserve(true);
                samplingDetail.setPreservative("是");
                setViewStyleDrawable(true, text_view_sample_add_preserve);
            } else {
                sample_add_preserve.setChecked(false);
                samplingDetail.setIsAddPreserve(false);
                samplingDetail.setPreservative("否");
                setViewStyleDrawable(false, text_view_sample_add_preserve);
            }
            setViewStyleDrawable(samplingDetail.getIsCompare(), text_view_sample_compare_monitor);
            sample_compare_monitor.setChecked(samplingDetail.getIsCompare());
            sample_mark.setText(samplingDetail.getDescription());

            setMonitorSize();
            if (samplingDetail.getSamplingType() == 0) {
                sample_quality.setRightTextStr(SamplingUtil.SAMPLING_TYPE_PT);
            } else if (samplingDetail.getSamplingType() == 1) {
                sample_quality.setRightTextStr(SamplingUtil.SAMPLING_TYPE_PX);
            } else if (samplingDetail.getSamplingType() == 2) {
                sample_quality.setRightTextStr(SamplingUtil.SAMPLING_TYPE_KB);
            }
        }

        //敬蓉：平行数据或空白数据不能编辑现场监测
        if (samplingDetail.getSamplingType() == 2 || samplingDetail.getSamplingType() == 1) {
            sample_monitor.setEnabled(false);
        } else {
            sample_monitor.setEnabled(true);
        }
    }

    /**
     * 先拿到立项时候的监测项目，在通过tagId（用户选择的样品性质）拿到采样规范的的数据
     * 如果 立项的监测项目在采样规范里面 并且采样规范里面的字段（IsSenceAnalysis）为true是现场监测
     * 否则为监测项目
     * 设置默认监测项目
     */
    private void setDefaultMonitor() {
        //拿到立项的监测项目
        List<ProjectDetial> detailList = DbHelpUtils.getProjectDetialList(mSampling.getProjectId(),
                mSampling.getTagId());
        List<String> ietmIdList = new ArrayList<>();
        List<String> ietmNameList = new ArrayList<>();
        if (RxDataTool.isEmpty(detailList)) {
            for (ProjectDetial detial : detailList) {
                if (!ietmIdList.contains(detial.getMonItemId())) {
                    ietmIdList.add(detial.getMonItemId());
                    ietmNameList.add(detial.getMonItemName());
                }
            }
        }
        //获取采样规范
        List<SamplingStantd> stantdList = DbHelpUtils.getSamplingStantdList(mSampling.getTagId());
        //现场监测
        List<String> xcNameList = new ArrayList<>();
        List<String> xcIdList = new ArrayList<>();
        //监测项目
        List<String> nameList = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        for (int i = 0; i < ietmIdList.size(); i++) {
            String itemName = ietmNameList.get(i);
            boolean isAdd = false;
            for (SamplingStantd stantd : stantdList) {
                if (stantd.getMonItems().contains(itemName) && stantd.getIsSenceAnalysis()) {
                    xcNameList.add(itemName);
                    xcIdList.add(ietmIdList.get(i));
                    isAdd = true;
                    continue;
                } else if (stantd.getMonItems().contains(itemName)) {
                    nameList.add(itemName);
                    idList.add(ietmIdList.get(i));
                    isAdd = true;
                    continue;
                }
            }
            //没有找到的项目  放在监测项目里面
            if (!isAdd) {
                nameList.add(itemName);
                idList.add(ietmIdList.get(i));
            }
        }
        samplingDetail.setMonitemName(RxDataTool.listToStr(nameList));
        samplingDetail.setMonitemId(RxDataTool.listToStr(idList));
        samplingDetail.setSenceMonitemName(RxDataTool.listToStr(xcNameList));
        samplingDetail.setSenceMonitemId(RxDataTool.listToStr(xcIdList));

        sample_monitor_items.setRightTextStr(samplingDetail.getMonitemName());
        sample_monitor.setRightTextStr(samplingDetail.getSenceMonitemName());

        setMonitorSize();
    }

    /**
     * 设置项目显示数量
     */
    private void setMonitorSize() {
        //名称项数量
        if (samplingDetail.getMonitemId() == null || samplingDetail.getMonitemId().equals("")) {
            sample_monitor_items.setLeftTextViewStr("监测项目(" + 0 + ")");
        } else {
            sample_monitor_items.setLeftTextViewStr("监测项目(" + samplingDetail.
                    getMonitemId().split(",").length + ")");
        }
        if (samplingDetail.getSenceMonitemId() == null || samplingDetail.getSenceMonitemId().equals("")) {
            sample_monitor.setLeftTextViewStr("现场监测(" + 0 + ")");
        } else {
            sample_monitor.setLeftTextViewStr("现场监测(" + samplingDetail.getSenceMonitemId().
                    split(",").length + ")");
        }
    }

    @OnClick({R.id.btn_back, R.id.my_layout_sample_monitor_items, R.id.my_layout_sample_monitor,
            R.id.btn_delete, R.id.btn_save, R.id.my_layout_sample_time})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.btn_back:
                onBack();
                break;
            case R.id.my_layout_sample_monitor_items:
                selectMonitorItems();
                break;
            case R.id.my_layout_sample_monitor:
                selectAddressItems();
                break;
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.my_layout_sample_time:
                selectSampleTime();
                break;
            default:
                break;
        }
    }

    /**
     * 采样时间
     */
    private void selectSampleTime() {
        TimePickerViewUtils.showTimePickerView(mContext,
                TimePickerViewUtils.HOURS_MIN, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String strDate = RxDateTool.getDate(RxDateTool.DATE_FORMAT_HH_MM, date);
                        samplingDetail.setSamplingTime(strDate);
                        sample_Time.setRightTextStr(strDate);
                    }
                });
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
                saveCollDetail();
            }
        }).start();
    }

    /**
     * 保存样品采集记录
     */
    private void saveCollDetail() {
        samplingDetail.setProjectId(mSampling.getProjectId());
        samplingDetail.setSamplingId(mSampling.getId());
        samplingDetail.setSampingCode(sample_code.getRightTextViewStr());
        samplingDetail.setFrequecyNo(Integer.parseInt(sample_frequency.getRightTextViewStr()));
        samplingDetail.setDescription(sample_mark.getText().toString());
        // samplingDetail.setSamplingTime(DateUtils.getWholeDate());
        //设置样品采集和样品验收
        samplingDetail.setSampleCollection("");
        samplingDetail.setSampleAcceptance("");
        //生成分瓶信息
        generateBottleSplit();
        //根据分瓶等到的样品数量
        int fxcCount = BottleSplitUtils.setSamplingCount(samplingDetail, mSampling);
        samplingDetail.setSamplingCount(fxcCount);
        //新增样品采集到采样单中
        if (fsListPosition == -1) {
            if (mSampling.getSamplingContentResults() == null) {
                mSampling.setSamplingContentResults(new ArrayList<SamplingContent>());
            }
            List<SamplingContent> samplingDetailResults = mSampling.getSamplingContentResults();
            if (!samplingDetailResults.contains(samplingDetail)) {
                samplingDetailResults.add(samplingDetail);
            }
            mSampling.setSamplingContentResults(samplingDetailResults);
        }

        //设置采样单完结状态以及更新采样单
        mSampling.setIsFinish(SamplingUtil.isSamplingFinsh(mSampling));
        mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
        Sampling sampling = DbHelpUtils.getDbSampling(mSampling.getId());
        if (RxDataTool.isNull(sampling)) {
            //mSampling.setId( UUID.randomUUID().toString());
            DBHelper.get().getSamplingDao().insert(mSampling);
        } else {
            DBHelper.get().getSamplingDao().update(mSampling);
        }


        //删除之前生成的SamplingDetail
        List<SamplingDetail> samplingDetailList = DbHelpUtils.getSamplingDetailList(samplingDetail.getSamplingId()
                , samplingDetail.getSampingCode(), samplingDetail.getSamplingType());
        if (!RxDataTool.isEmpty(samplingDetailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
            mSampling.getSamplingDetailResults().removeAll(samplingDetailList);
        }
        //生成新的SamplingDetail
        generateSamplingDetails(fxcCount);

        if (fsListPosition == -1) {
            //samplingDetail.setId(UUID.randomUUID().toString());
            //samplingDetail.setSamplingType(0);
            //DBHelper.get().getSamplingContentDao().insert(samplingDetail);
            DBHelper.get().getSamplingContentDao().insertOrReplace(samplingDetail);
        } else {
            DBHelper.get().getSamplingContentDao().update(samplingDetail);
        }

        //设置信息
        setBottleAndContent();
        strCollFragment(true);
    }


    private void setBottleAndContent() {
        List<SamplingFormStand> formStantdsList = DbHelpUtils.getSamplingFormStandList(mSampling.getId());
        if (!RxDataTool.isEmpty(formStantdsList)) {
            mSampling.setSamplingFormStandResults(formStantdsList);
        } else {
            mSampling.setSamplingFormStandResults(new ArrayList<>());
        }

        List<SamplingContent> samplingDetails = DbHelpUtils.getSamplingContentList(mSampling.getId());
        if (!RxDataTool.isEmpty(samplingDetails)) {
            mSampling.setSamplingContentResults(samplingDetails);
        } else {
            mSampling.setSamplingContentResults(new ArrayList<>());
        }

    }

    /**
     * 生成分瓶信息:只有监测项目才生成分瓶信息
     *
     * @return
     */
    private void generateBottleSplit() {
        String currentMonItemIds = samplingDetail.getMonitemId();
        if (oldItemId == null) oldItemId = "";
        //相同 不用生成分瓶信息  不同则需要
        if (!oldItemId.equals(currentMonItemIds)) {
            List<String> monItemIds = Arrays.asList(currentMonItemIds.split(","));
            List<String> oldItemIdList = Arrays.asList(oldItemId.split(","));
            if (oldItemIdList != null && oldItemIdList.size() > 0) {
                for (String oldId : oldItemIdList) {
                    BottleSplitUtils.deleteBottleByItemId(oldId, mSampling);
                }
            }

            if (monItemIds != null && monItemIds.size() > 0) {
                for (String id : monItemIds)
                    BottleSplitUtils.createAndUpdateBottle(id, mSampling);
            }
        }
    }

    /**
     * 判断是否可以保存
     *
     * @return
     */
    private boolean isCanSave() {
        if (TextUtils.isEmpty(sample_monitor_items.getRightTextViewStr())) {
            showMessage("请选择监测项目");
            return false;
        }
        return true;
    }

    /**
     * 删除采样信息
     */
    private void delete() {
        if (RxDataTool.isEmpty(mSampling.getSamplingContentResults()) || fsListPosition == -1) {
            showMessage("请先添加采样数据");
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
                        deleteCollDetail();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                    }
                });
    }

    /**
     * 删除样品采集信息
     */
    private void deleteCollDetail() {
        //删除SamplingContent
        SamplingContent currentDetail = mSampling.getSamplingContentResults().get(fsListPosition);
        SamplingContent samplingDetail = DbHelpUtils.getSamplingContent(currentDetail.getId());

        if (!RxDataTool.isNull(samplingDetail)) {
            DBHelper.get().getSamplingContentDao().delete(samplingDetail);
        }
        //删除平行样
        if (!RxDataTool.isNull(samplingDetail) && samplingDetail.getSamplingType() == 0) {//删除普通样对应的平行样
            SamplingContent parallelSamContent = DbHelpUtils.getSamplingContent(samplingDetail.getProjectId(),
                    samplingDetail.getId(), samplingDetail.getFrequecyNo());

            if (!RxDataTool.isNull(parallelSamContent)) {
                DBHelper.get().getSamplingContentDao().delete(parallelSamContent);
                mSampling.getSamplingContentResults().remove(parallelSamContent);
            }
        }
        //删除分瓶信息
        deleteRelateBottle();

        if ((fsListPosition >= 0) && (fsListPosition < mSampling.getSamplingContentResults().size())) {
            mSampling.getSamplingContentResults().remove(fsListPosition);
        }

        //删除对应的SamplingDetail
        List<SamplingDetail> samplingDetailList = DbHelpUtils.getSamplingDetailList(currentDetail.getSamplingId(),
                currentDetail.getSampingCode(), currentDetail.getSamplingType());

        if (!RxDataTool.isEmpty(samplingDetailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
            //WastewaterActivity.mSampling.setSamplingDetailResults(new ArrayList<>());
            mSampling.getSamplingDetailResults().removeAll(samplingDetailList);
        }


        //下面这个地方有问题，没清除数据库之前的数据
        List<SamplingFormStand> formStantdsList = DbHelpUtils.getSamplingFormStandList(mSampling.getId());

        if (!RxDataTool.isEmpty(formStantdsList)) {
            mSampling.setSamplingFormStandResults(formStantdsList);
        } else {
            mSampling.setSamplingFormStandResults(new ArrayList<>());
        }

        mSampling.setIsFinish(SamplingUtil.isSamplingFinsh(mSampling));
        mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
        Sampling sampling = DbHelpUtils.getDbSampling(mSampling.getId());
        if (RxDataTool.isNull(sampling)) {
            DBHelper.get().getSamplingDao().insert(mSampling);
        } else {
            DBHelper.get().getSamplingDao().update(mSampling);
        }

        showMessage("删除成功");
        strCollFragment(true);
    }

    /**
     * 删除分瓶信息
     */
    private void deleteRelateBottle() {
        String currentMonitemIds = samplingDetail.getMonitemId();
        String[] monItemIds = currentMonitemIds.split(",");
        if (monItemIds.length > 0) {
            for (String itemId : monItemIds) {
                BottleSplitUtils.deleteBottleByItemId(itemId, mSampling);
            }
        }
    }

    /**
     * 选择现场监测项目
     */
    private void selectAddressItems() {
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSampling.getParentTagId());
        if (!RxDataTool.isEmpty(samplingDetail.getSenceMonitemId())) {
            intent.putExtra("selectItems", samplingDetail.getSenceMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    saveAndShowItem(data, true);
                }
            }
        });
    }

    /**
     * 选择监测项目
     */
    private void selectMonitorItems() {
        if (fsListPosition != -1) {
            oldItemId = samplingDetail.getMonitemId();
        }
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSampling.getParentTagId());
        if (!RxDataTool.isEmpty(samplingDetail.getMonitemId())) {
            intent.putExtra("selectItems", samplingDetail.getMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    saveAndShowItem(data, false);
                }
            }
        });
    }

    /**
     * 根据@isScenel来区分保存和显示intent传递的项目值是“现场检测”还是“检测项目”
     *
     * @param data                          传递的项目名称和id
     * @param isScene@true现场检测”@false“检测项目”
     */
    public void saveAndShowItem(Intent data, boolean isScene) {
        String id = data.getStringExtra("MonItemId");
        String name = data.getStringExtra("MonItemName");
        String str = "";
        if (isScene) {//现场检测
            samplingDetail.setSenceMonitemName(name);
            samplingDetail.setSenceMonitemId(id);
            sample_monitor.setRightTextStr(name);
            //name去重
            str = RxDataTool.getDifference(samplingDetail.getMonitemName(), samplingDetail.getSenceMonitemName());
            sample_monitor_items.setRightTextStr(str);
            samplingDetail.setMonitemName(str);
            //id去重
            samplingDetail.setMonitemId(RxDataTool.getDifference(samplingDetail.getMonitemId(),
                    samplingDetail.getSenceMonitemId()));
        } else {//检测项目
            samplingDetail.setMonitemName(name);
            samplingDetail.setMonitemId(id);
            sample_monitor_items.setRightTextStr(name);
            //name去重
            str = RxDataTool.getDifference(samplingDetail.getSenceMonitemName(), samplingDetail.getMonitemName());
            sample_monitor.setRightTextStr(str);
            samplingDetail.setSenceMonitemName(str);
            //id去重
            samplingDetail.setSenceMonitemId(RxDataTool.getDifference(samplingDetail.getSenceMonitemId(),
                    samplingDetail.getMonitemId()));
        }
        //名称项数量
        setMonitorSize();
    }

    /**
     * 产生SamplingDetails
     */
    private void generateSamplingDetails(int fxcCount) {
        samplingDetail.setSamplingCount(fxcCount);
        //包括监测项目和现场监测项目
        //非现场监测的样品数量
        if (!RxDataTool.isEmpty(samplingDetail.getMonitemId())) {
            String[] monitemIds = samplingDetail.getMonitemId().split(",");
            if (!RxDataTool.isEmpty(monitemIds)) {
                for (String itemId : monitemIds) {
                    SamplingDetail detail = new SamplingDetail();
                    detail.setProjectId(samplingDetail.getProjectId());
                    detail.setId(UUID.randomUUID().toString());
                    detail.setSamplingId(samplingDetail.getSamplingId());
                    detail.setSampingCode(samplingDetail.getSampingCode());
                    detail.setFrequecyNo(samplingDetail.getFrequecyNo());
                    detail.setDescription(samplingDetail.getDescription());

                    detail.setSamplingType(samplingDetail.getSamplingType());
                    detail.setIsCompare(samplingDetail.getIsCompare());
                    detail.setIsAddPreserve(samplingDetail.getIsAddPreserve());
                    detail.setMonitemName(TagsUtils.getMonItemNameById(itemId, mSampling));
                    detail.setMonitemId(itemId);
                    detail.setAddressName(samplingDetail.getAddressName());
                    detail.setAddresssId(samplingDetail.getAddresssId());
                    detail.setIsSenceAnalysis(false);
                    detail.setSamplingTime(samplingDetail.getSamplingTime());
                    detail.setOrderIndex(samplingDetail.getOrderIndex());
                    //设置剩余信息
                    detail.setSampleCollection(samplingDetail.getSampleCollection());
                    detail.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                    detail.setPreservative(samplingDetail.getPreservative());
                    //计算SamplingCount为非现场监测的样品数量
                    detail.setSamplingCount(fxcCount);
                    DBHelper.get().getSamplingDetailDao().insert(detail);
                    mSampling.getSamplingDetailResults().add(detail);
                }
            }
        }
        if (!RxDataTool.isEmpty(samplingDetail.getSenceMonitemId())) {
            String[] sendMonitemIds = samplingDetail.getSenceMonitemId().split(",");
            if (!RxDataTool.isEmpty(sendMonitemIds)) {
                for (String itemId : sendMonitemIds) {
                    SamplingDetail detail = new SamplingDetail();
                    detail.setProjectId(samplingDetail.getProjectId());
                    detail.setId(UUID.randomUUID().toString());
                    detail.setSamplingId(samplingDetail.getSamplingId());
                    detail.setSampingCode(samplingDetail.getSampingCode());
                    detail.setFrequecyNo(samplingDetail.getFrequecyNo());
                    detail.setDescription(samplingDetail.getDescription());
                    detail.setSamplingType(samplingDetail.getSamplingType());
                    detail.setIsCompare(samplingDetail.getIsCompare());
                    detail.setIsAddPreserve(samplingDetail.getIsAddPreserve());
                    detail.setMonitemName(TagsUtils.getMonItemNameById(itemId, mSampling));
                    detail.setMonitemId(itemId);
                    detail.setAddressName(samplingDetail.getAddressName());
                    detail.setAddresssId(samplingDetail.getAddresssId());
                    detail.setIsSenceAnalysis(true);
                    detail.setSamplingTime(samplingDetail.getSamplingTime());
                    detail.setOrderIndex(samplingDetail.getOrderIndex());
                    //设置剩余信息
                    detail.setSampleCollection(samplingDetail.getSampleCollection());
                    detail.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                    detail.setPreservative(samplingDetail.getPreservative());
                    //现场监测样品数量为0
                    detail.setSamplingCount(0);
                    DBHelper.get().getSamplingDetailDao().insert(detail);
                    mSampling.getSamplingDetailResults().add(detail);
                }
            }
        }
    }

    /**
     * 退出当前页面
     */
    private void onBack() {
        strCollFragment(false);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 跳转到样品采集列表界面
     */
    private void strCollFragment(boolean isUpdate) {
        closeLoadingDialog();
        EventBus.getDefault().post(isUpdate, EventBusTags.TAG_SAMPLING_UPDATE);
        EventBus.getDefault().post(WasteWaterActivity.FRAGMENT_ITEM_INT_COLLECTION,
                EventBusTags.TAG_WASTE_WATER_COLLECTION);
    }
}
