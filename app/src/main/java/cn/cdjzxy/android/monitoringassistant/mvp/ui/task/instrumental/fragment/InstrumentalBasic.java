package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.SampleMonItemActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.UserActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.onactivityresult.AvoidOnResult;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class InstrumentalBasic extends SampleBaseFragment {
    @BindView(R.id.my_layout_project)
    MyDrawableLinearLayout tvChooseProject;

    @BindView(R.id.my_layout_sampling_no)
    MyDrawableLinearLayout tvSamplingNo;

    @BindView(R.id.my_layout_project_name)
    MyDrawableLinearLayout tvProjectName;

    @BindView(R.id.my_layout_monitem_name)
    MyDrawableLinearLayout tvMonitemName;

    @BindView(R.id.my_layout_sampling_property)
    MyDrawableLinearLayout tvSamplingProperty;

    @BindView(R.id.my_layout_test_user)
    MyDrawableLinearLayout tvTestUser;

    @BindView(R.id.my_layout_start_date)
    MyDrawableLinearLayout tvTestStartDate;

    @BindView(R.id.my_layout_end_date)
    MyDrawableLinearLayout tvTestEndDate;

    @BindView(R.id.my_layout_method)
    MyDrawableLinearLayout tvTestMethod;

    @BindView(R.id.my_layout_device)
    MyDrawableLinearLayout tvTestDevice;

    @BindView(R.id.tv_comment)
    EditText tvComment;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrument_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!RxDataTool.isNull(mSampling)) {
//            tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
            tvSamplingNo.setRightTextStr(mSampling.getSamplingNo());
            tvProjectName.setRightTextStr(mSampling.getProjectName());
            tvMonitemName.setRightTextStr(mSampling.getMonitemName());
//            tvSamplingProperty.setText(InstrumentalActivity.mSampling.getTagName());
            tvSamplingProperty.setRightTextStr(mSampling.getFormTypeName());
            tvTestUser.setRightTextStr(mSampling.getSamplingUserName());
            String startDate = RxDateTool.simpleDateFormat(RxDateTool.DATE_FORMAT_DETACH,
                    mSampling.getSamplingTimeBegin(), RxDateTool.DATE_FORMAT);
            tvTestStartDate.setRightTextStr(startDate);
            String endDate = RxDateTool.simpleDateFormat(RxDateTool.DATE_FORMAT_DETACH,
                    mSampling.getSamplingTimeEnd(), RxDateTool.DATE_FORMAT);
            tvTestEndDate.setRightTextStr(endDate);
            tvTestMethod.setRightTextStr(mSampling.getMethodName());
            tvTestDevice.setRightTextStr(mSampling.getPrivateDataStringValue("DeviceText"));
            tvComment.setText(mSampling.getComment());

            tvChooseProject.setEnabled(mSampling.getIsCanEdit());
            tvTestUser.setEnabled(mSampling.getIsCanEdit());
            tvTestStartDate.setEnabled(mSampling.getIsCanEdit());
            tvTestEndDate.setEnabled(mSampling.getIsCanEdit());
            tvTestMethod.setEnabled(mSampling.getIsCanEdit());
            tvTestDevice.setEnabled(mSampling.getIsCanEdit());
            tvComment.setEnabled(mSampling.getIsCanEdit());
        }
    }

    @Override
    public void setData(@Nullable Object data) {
        if (data != null && data instanceof Sampling) {
            this.mSampling = (Sampling) data;
            // initData(null);
        }
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @OnClick({R.id.my_layout_test_user, R.id.my_layout_project, R.id.my_layout_start_date,
            R.id.my_layout_end_date, R.id.my_layout_method, R.id.my_layout_device})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_layout_project:
                selectProject();
                break;
            case R.id.my_layout_test_user:
                selectUser();
                break;
            case R.id.my_layout_start_date:
                break;
            case R.id.my_layout_end_date:
                break;
            case R.id.my_layout_method:
                break;
            case R.id.my_layout_device:
                break;

        }
    }

    /**
     * 选择人员
     *
     */
    private void selectUser() {
        Intent intent = new Intent(getContext(), UserActivity.class);
        intent.putExtra("projectId", mSampling.getProjectId());
        intent.putExtra("selectUserIds", mSampling.getSamplingUserId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!RxDataTool.isEmpty(data.getStringExtra("UserId")) &&
                            !RxDataTool.isEmpty(data.getStringExtra("UserName"))) {
                        mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                        mSampling.setSamplingUserName(data.getStringExtra("UserName"));

                        mSampling.setMonitorPerson(mSampling.getSamplingUserName());

                        tvTestUser.setRightTextStr(mSampling.getSamplingUserName());
                    }
                }
            }
        });
    }

    /**
     * 选择项目
     */
    private void selectProject() {
        Intent intent = new Intent();
        intent.setClass(getContext(), SampleMonItemActivity.class);
        intent.putExtra(SamplingUtil.INTENT_PROJECT_ID, mSampling.getProjectId());
        intent.putExtra(SampleMonItemActivity.INTENT_PATH, mSampling.getFormPath());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }

                if (RxDataTool.isEmpty(data.getStringExtra("MonitemId")) ||
                        RxDataTool.isEmpty(data.getStringExtra("MonitemName"))) {
                    return;
                }

                String monItemId = data.getStringExtra("MonitemId");
                String monItemName = data.getStringExtra("MonitemName");
                tvChooseProject.setRightTextStr(monItemName);
                if (monItemId.equals(mSampling.getMonitemId()) &&
                        monItemName.equals(mSampling.getMonitemName())) {
                    return;//跟之前选择的一样
                }

                mSampling.setMonitemId(monItemId);
                mSampling.setMonitemName(monItemName);

                //改为在添加样品时记录点位信息
//                        InstrumentalActivity.mSampling.setAddressId(data.getStringExtra("AddressId"));
//                        InstrumentalActivity.mSampling.setAddressName(data.getStringExtra("AddressName"));

                mSampling.setTagId(data.getStringExtra("TagId"));
                mSampling.setTagName(data.getStringExtra("TagName"));
                mSampling.setFormType(data.getStringExtra("TagId"));
                mSampling.setFormTypeName(data.getStringExtra("TagName"));

                mSampling.setPrivateDataStringValue("FormTypeName", mSampling.getFormTypeName());

//                        InstrumentalActivity.mSampling.setAllMonitemId(data.getStringExtra("AllMonitemId"));
//                        InstrumentalActivity.mSampling.setAllMonitemName(data.getStringExtra("AllMonitemName"));

//                        tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
//                        tvSamplingProperty.setText(InstrumentalActivity.mSampling.getTagName());
                tvSamplingProperty.setRightTextStr(mSampling.getFormTypeName());
                tvMonitemName.setRightTextStr(mSampling.getMonitemName());

                //重置监测方法
                mSampling.setMethodId("");
                mSampling.setMethodName("");
                tvTestMethod.setRightTextStr(mSampling.getMethodName());

                //重置监测仪器
                mSampling.setDeviceName("");
                mSampling.setDeviceId("");
                tvTestDevice.setRightTextStr(mSampling.getDeviceName());

                //重置检测结果,先清理数据库中的数据
                List<SamplingDetail> samplingDetails = DbHelpUtils.getSamplingDetailList(mSampling.getId());
                //遍历数据删除
                for (SamplingDetail detail : samplingDetails) {
                    if (RxDataTool.isNull(detail)) {
                        continue;
                    }

                    //从数据库中删除
                    DBHelper.get().getSamplingDetailDao().delete(detail);
                }

                //清理内存中的数据
                mSampling.getSamplingDetailYQFs().clear();
            }
        });
    }
}
