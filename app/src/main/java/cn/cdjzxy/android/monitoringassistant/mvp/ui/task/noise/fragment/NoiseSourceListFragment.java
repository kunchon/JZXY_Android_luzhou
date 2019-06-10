package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

/**
 * 噪声——主要噪声源数据列表
 */
public class NoiseSourceListFragment extends SampleBaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_back)
    LinearLayout linearBack;//返回上级的title
    @BindView(R.id.my_layout_source)
    MyDrawableLinearLayout edSource;//主要声源
    @BindView(R.id.my_layout_source_name)
    MyDrawableLinearLayout edSourceName;//声源名称
    @BindView(R.id.my_layout_source_num)
    MyDrawableLinearLayout edSourceNum;//数量
    @BindView(R.id.my_layout_source_date)
    MyDrawableLinearLayout edSourceDate;//运行时间和状况
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_edit, null);
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
