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

import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.MonItemMethodActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.SampleMonItemActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity.UserActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.device.DeviceActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.TimePickerViewUtils;
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
            String startDate = RxDateTool.simpleDateFormat(mSampling.getSamplingTimeBegin(),
                    RxDateTool.DATE_FORMAT);
            tvTestStartDate.setRightTextStr(startDate);
            String endDate = RxDateTool.simpleDateFormat(
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

//    @Override
//    public void setData(@Nullable Object data) {
//        if (data != null && data instanceof Sampling) {
//            this.mSampling = (Sampling) data;
//            // initData(null);
//        }
//    }

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
                selectDate(true);
                break;
            case R.id.my_layout_end_date:
                selectDate(false);
                break;
            case R.id.my_layout_method:
                selectMethod();
                break;
            case R.id.my_layout_device:
                selectDevice();
                break;

        }
    }

    /**
     * 选择仪器
     */
    private void selectDevice() {
        if (RxDataTool.isEmpty(mSampling.getMethodId())) {
            showMessage("请先选择方法");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, DeviceActivity.class);
        intent.putExtra(DeviceActivity.METHOD_ID, mSampling.getMethodId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String deviceId = data.getStringExtra("DeviceId");
                    String deviceName = data.getStringExtra("DeviceName");
                    String deviceCode = data.getStringExtra("DeviceCode");
                    String sourceWay = data.getStringExtra("SourceWay");
                    String expireDate = data.getStringExtra("ExpireDate");
                    String deviceText;
                    if (expireDate != null && !expireDate.equals("")) {
                        String[] s = expireDate.split(" ");
                        deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, s[0]);
                    } else {
                        deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, expireDate);
                    }


                    mSampling.setDeviceId(deviceId);
                    mSampling.setDeviceName(deviceText);
                    mSampling.setPrivateDataStringValue("SourceWay", sourceWay);
                    mSampling.setPrivateDataStringValue("SourceDate", expireDate);
                    //设备信息格式：仪器名称(仪器编号)(仪器溯源方式 仪器溯源有效期)
                    mSampling.setPrivateDataStringValue("DeviceText", deviceText);

                    tvTestDevice.setRightTextStr(deviceText);
                }
            }
        });
    }

    /**
     * 选择监测方法
     */
    private void selectMethod() {
        if (RxDataTool.isEmpty(mSampling.getMethodId())) {
            showMessage("请选择项目");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, MonItemMethodActivity.class);
        intent.putExtra(MonItemMethodActivity.MON_ITEM_ID, mSampling.getMethodId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {

            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSampling.setMethodId(data.getStringExtra("MethodId"));
                    mSampling.setMethodName(data.getStringExtra("MethodName"));
                    tvTestMethod.setRightTextStr(mSampling.getMethodName());

                    //重置监测仪器
                    mSampling.setDeviceName("");
                    mSampling.setDeviceId("");
                    tvTestDevice.setRightTextStr(mSampling.getDeviceName());
                }
            }
        });

    }

    /**
     * 选择时间
     *
     * @param b @true开始时间 false 结束时候
     */
    private void selectDate(boolean b) {
        TimePickerViewUtils.showTimePickerView(mContext,
                TimePickerViewUtils.YEAR_MONTH_DAY, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = RxDateTool.getDate(RxDateTool.DATE_FORMAT,date);
                        if (b) {
                            tvTestStartDate.setRightTextStr(time);
                            mSampling.setSamplingTimeBegin(time);
                            mSampling.setSamplingNo(SamplingUtil.createSamplingNo(time));
                            tvSamplingNo.setRightTextStr(mSampling.getSamplingNo());
                        } else {
                            tvTestEndDate.setRightTextStr(time);
                            mSampling.setSamplingTimeEnd(time);
                        }
                    }
                });
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
