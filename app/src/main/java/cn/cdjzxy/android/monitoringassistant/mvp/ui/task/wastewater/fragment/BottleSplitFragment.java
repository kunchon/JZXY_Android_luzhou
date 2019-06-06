package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.wastewater.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;

public class BottleSplitFragment extends SampleBaseFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
