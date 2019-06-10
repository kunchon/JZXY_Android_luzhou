package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
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
}
