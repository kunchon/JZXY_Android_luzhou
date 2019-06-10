package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;



/**
 * 基本信息
 */
public class PrecipitationBasic extends SampleBaseFragment {

    Unbinder unbinder;
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

    @BindView(R.id.iv_add_photo)
    ImageView ivAddPhoto;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

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

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preciptation_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
