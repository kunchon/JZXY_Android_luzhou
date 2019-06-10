package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class CollectionDetailFragment extends SampleBaseFragment {

    Unbinder unbinder;
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

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect_detail, null);
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
