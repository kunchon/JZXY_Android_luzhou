package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

/**
 * 噪声监测数据——详情或者编辑页面
 */
public class NoiseMonitorEditFragment extends SampleBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_back)
    LinearLayout linearBack;
    @BindView(R.id.my_layout_monitor_address)
    MyDrawableLinearLayout tvAddress;//测点位置
    @BindView(R.id.btn_is_open)
    ImageButton btnIsOpen;
    @BindView(R.id.relate_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.linear_open)
    LinearLayout linearOpen;
    @BindView(R.id.my_layout_monitor_date)
    MyDrawableLinearLayout edMonitorDate;//监测时段
    @BindView(R.id.my_layout_monitor_time_first_start)
    MyDrawableLinearLayout tvMonitorTimeFirstStart;//第一个监测时间
    @BindView(R.id.my_layout_monitor_time_first_end)
    MyDrawableLinearLayout tvMonitorTimeFirstEnd;
    @BindView(R.id.my_layout_monitor_data)
    MyDrawableLinearLayout edMonitorData;//测量值
    @BindView(R.id.my_layout_monitor_time_start)
    MyDrawableLinearLayout tvMonitorTimeStart;//监测时间
    @BindView(R.id.my_layout_monitor_time_end)
    MyDrawableLinearLayout tvMonitorTimeEnd;
    @BindView(R.id.my_layout_bg_data)
    MyDrawableLinearLayout edMonitorBgData;//背景值
    @BindView(R.id.my_layout_monitor_edit_data)
    MyDrawableLinearLayout edMonitorEditData;//修正值
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_monitor_edit, null);
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
