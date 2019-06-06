package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
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


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
