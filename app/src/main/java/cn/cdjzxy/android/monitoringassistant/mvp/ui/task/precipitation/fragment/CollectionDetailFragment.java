package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.precipitation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;


/**
 * 采集样品详情
 */
public class CollectionDetailFragment extends SampleBaseFragment {


    Unbinder unbinder;
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout tvSampleCode;
    @BindView(R.id.my_layout_frequency)
    MyDrawableLinearLayout tvFrequency;
    @BindView(R.id.my_layout_start_time)
    MyDrawableLinearLayout tvStartTime;
    @BindView(R.id.my_layout_end_time)
    MyDrawableLinearLayout tvEndTime;
    @BindView(R.id.my_layout_precipitation)
    MyDrawableLinearLayout etPrecipitation;
    @BindView(R.id.my_layout_rainwater_volume)
    MyDrawableLinearLayout etRainwaterVolume;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect_detail, null);
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
