package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;



/**
 * 基本信息
 */
public class PrecipitationBasic extends SampleBaseFragment {

    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout tvSamplingDate;
    @BindView(R.id.my_layout_sample_user)
    MyDrawableLinearLayout tvSamplingUser;
    @BindView(R.id.my_layout_sample_type)
    MyDrawableLinearLayout tvSamplingType;
    @BindView(R.id.my_layout_sample_point)
    MyDrawableLinearLayout tvSamplingPoint;
    @BindView(R.id.my_layout_sample_no)
    MyDrawableLinearLayout tvSamplingNo;
    @BindView(R.id.my_layout_monitor_name)
    MyDrawableLinearLayout edMonitorName;//监测项目
    @BindView(R.id.my_layout_monitor_height)
    MyDrawableLinearLayout tvSamplingHeight;
    @BindView(R.id.my_layout_monitor_area)
    MyDrawableLinearLayout etSamplingArea;
    @BindView(R.id.my_layout_monitor_method)
    MyDrawableLinearLayout tvSamplingMethod;
    @BindView(R.id.my_layout_monitor_device)
    MyDrawableLinearLayout tvSamplingDevice;
    @BindView(R.id.layout_flow_information)
    RelativeLayout layoutFlowInformation;

    @BindView(R.id.tv_comment)
    EditText tvComment;

   

    //流转信息
    @BindView(R.id.tv_arrow)
    TextView tvArrow;
    @BindView(R.id.layout_flow_information_container)
    LinearLayout layoutFlowInformationContainer;
    @BindView(R.id.my_layout_flow_method)
    MyDrawableLinearLayout tvFlowMethod;
    @BindView(R.id.my_layout_flow_date)
    MyDrawableLinearLayout tvFlowDate;
    @BindView(R.id.my_layout_receive_date)
    MyDrawableLinearLayout tv_receive_date;

    private PreciptationPrivateData mPrivateData;
    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preciptation_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPrivateData = new PreciptationPrivateData();

        if (!RxDataTool.isNull(mSampling)) {
            tvSamplingDate.setRightTextStr(RxDateTool.simpleDateFormat(mSampling.getSamplingTimeBegin(),
                    RxDateTool.DATE_FORMAT));
            tvSamplingUser.setRightTextStr(mSampling.getSamplingUserName());
            tvSamplingType.setRightTextStr(mSampling.getTagName());
            tvSamplingPoint.setRightTextStr(mSampling.getAddressName());
            tvSamplingNo.setEditTextStr(mSampling.getAddressNo());
            edMonitorName.setEditTextStr(mSampling.getMonitemName());
            if (!RxDataTool.isEmpty(mSampling.getPrivateData())) {
                mPrivateData = JSONObject.parseObject(mSampling.getPrivateData(), PreciptationPrivateData.class);
                if (!RxDataTool.isNull(mPrivateData)) {
                    tvSamplingHeight.setEditTextStr(mPrivateData.getSampHight());
                    etSamplingArea.setEditTextStr(mPrivateData.getSampArea());
                }
            }
            tvSamplingMethod.setRightTextStr(mSampling.getMethodName());
            tvSamplingDevice.setRightTextStr(mSampling.getDeviceName());
            tvFlowMethod.setRightTextStr(mSampling.getTransfer());
            tvFlowDate.setRightTextStr(RxDateTool.simpleDateFormat(mSampling.getSendSampTime(),
                    RxDateTool.DATE_FORMAT_YYYY_MM_DD_HH_MM));
            tv_receive_date.setRightTextStr(
                    RxDateTool.simpleDateFormat(mSampling.getReciveTime(),
                            RxDateTool.DATE_FORMAT_YYYY_MM_DD_HH_MM));
            tvComment.setText(mSampling.getComment());

        }

        tvSamplingHeight.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPrivateData.setSampHight(RxDataTool.isEmpty(s.toString()) ? "" : s.toString());
                mSampling.setPrivateData(JSONObject.toJSONString(mPrivateData));
            }
        });
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
      
    }
}
