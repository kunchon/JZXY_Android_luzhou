package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;


public class NoiseBasic extends SampleBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.my_layout_number)
    MyDrawableLinearLayout tvSampleNo;//采样点编号
    @BindView(R.id.my_layout_name)
    MyDrawableLinearLayout tvSampleName;//项目名称
    @BindView(R.id.my_layout_nature)
    MyDrawableLinearLayout tvNature;//监测性质
    @BindView(R.id.my_layout_date)
    MyDrawableLinearLayout tvDate;//监测日期
    @BindView(R.id.my_layout_factory_name)
    MyDrawableLinearLayout edFactoryName;//企业名称
    //    @BindView(R.id.text_view_factory_name)
//    TextView tvFactoryName;//企业名称 用于展示（不可编辑）
    @BindView(R.id.my_layout_factory_address)
    MyDrawableLinearLayout edFactoryAddress;//企业地址
    //    @BindView(R.id.text_view_factory_address)
//    TextView tvFactoryAddress;//企业地址 用于展示（不可编辑）
    @BindView(R.id.my_layout_factory_info)
    MyDrawableLinearLayout edFactoryInfo;//企业生产工况
    @BindView(R.id.my_layout_weather_state)
    MyDrawableLinearLayout tvWeatherState;//天气情况
    @BindView(R.id.my_layout_wind_speed)
    MyDrawableLinearLayout edWingSpeed;//风速
    @BindView(R.id.my_layout_calibration_front)
    MyDrawableLinearLayout edCalibrationFront;//测前校准值
    @BindView(R.id.my_layout_calibration_after)
    MyDrawableLinearLayout edCalibrationAfter;//测后校准值
    @BindView(R.id.my_layout_device_number)
    MyDrawableLinearLayout tvDeviceNumber;//风速仪型号及编号
    @BindView(R.id.my_layout_monitor_from)
    MyDrawableLinearLayout tvMonitorForm;//监测方法及来源
    @BindView(R.id.my_layout_device_name)
    MyDrawableLinearLayout tvMonitorDeviceName;//监测仪器名称
    @BindView(R.id.my_layout_calibration_method)
    MyDrawableLinearLayout edCalibrationMethod;//校准方法
    @BindView(R.id.my_layout_calibration_number)
    MyDrawableLinearLayout tvCalibrationNumber;//校准仪器名称
    @BindView(R.id.edit_remarks)
    EditText edeRemarks;//备注
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_basic, null);
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
