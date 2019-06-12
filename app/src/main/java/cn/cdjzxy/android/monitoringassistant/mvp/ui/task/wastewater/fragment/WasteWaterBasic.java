package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class WasteWaterBasic extends SampleBaseFragment {

    @BindView(R.id.my_layout_number)
    MyDrawableLinearLayout base_sample_no;//采样单编号
    @BindView(R.id.my_layout_sample_md)
    MyDrawableLinearLayout base_sample_md;//采样单目的
    @BindView(R.id.my_layout_sample_xz)
    MyDrawableLinearLayout base_sample_xz;//采样性质
    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout base_sample_date;//采样日期
    @BindView(R.id.my_layout_sample_user)
    MyDrawableLinearLayout base_sample_user;//采样人

    @BindView(R.id.my_layout_sample_property)
    MyDrawableLinearLayout base_sample_property;//样品性质
    @BindView(R.id.my_layout_sample_point)
    MyDrawableLinearLayout base_sample_point;//采样点位
    @BindView(R.id.my_layout_sample_method)
    MyDrawableLinearLayout base_sample_method;//采样方法
    @BindView(R.id.base_sample_handle)
    CheckedTextView base_sample_handle;
    @BindView(R.id.text_view_sample_handle)
    TextView textViewSampleHandler;
    @BindView(R.id.text_view_weather_arrow)
    TextView text_view_weather_arrow;
    @BindView(R.id.weather_arrow)
    TextView weather_arrow;
    @BindView(R.id.layout_weather_information_container)
    View layout_weather_information_container;
    @BindView(R.id.my_layout_weather_state)
    MyDrawableLinearLayout weather_state;
    @BindView(R.id.my_layout_weather_temp)
    MyDrawableLinearLayout weather_temp;
    @BindView(R.id.my_layout_weather_pressure)
    MyDrawableLinearLayout weather_pressure;
    //更多信息
    @BindView(R.id.text_view_more_arrow)
    TextView text_view_more_arrow;
    @BindView(R.id.more_arrow)
    TextView more_arrow;
    @BindView(R.id.layout_more_information)
    View layout_more_information;
    @BindView(R.id.my_layout_more_name)
    MyDrawableLinearLayout more_name;//企业名称
    @BindView(R.id.my_layout_more_address)
    MyDrawableLinearLayout more_address;//企业地址
    @BindView(R.id.my_layout_more_device)
    MyDrawableLinearLayout more_device;//处理设施
    @BindView(R.id.my_layout_more_waterbody)
    MyDrawableLinearLayout more_waterbody;//受纳水体
    @BindView(R.id.my_layout_more_build_date)
    MyDrawableLinearLayout more_build_date;//建设时间
    @BindView(R.id.text_view_more_gw)
    TextView text_view_more_gw;
    @BindView(R.id.more_gw)
    CheckedTextView more_gw;//是否进入管网
    //流转信息
    @BindView(R.id.text_view_tv_arrow)
    TextView text_view_tv_arrow;
    @BindView(R.id.tv_arrow)
    TextView tv_arrow;
    @BindView(R.id.layout_flow_information_container)
    View layout_flow_information_container;
    @BindView(R.id.my_layout_flow_method)
    MyDrawableLinearLayout tv_flow_method;//运输方式
    @BindView(R.id.my_layout_flow_date)
    MyDrawableLinearLayout tv_flow_date;//送样时间
    @BindView(R.id.my_layout_receive_date)
    MyDrawableLinearLayout tv_receive_date;//收样时间

    @BindView(R.id.base_sample_comment)
    TextView base_sample_comment;
    @BindView(R.id.iv_add_photo)
    ImageView iv_add_photo;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public View initView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_basic_info, null);
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
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
