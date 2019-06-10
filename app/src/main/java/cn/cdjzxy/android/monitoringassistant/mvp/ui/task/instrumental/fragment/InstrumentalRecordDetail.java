package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.SampleBaseFragment;
import cn.cdjzxy.android.monitoringassistant.widget.MyDrawableLinearLayout;

public class InstrumentalRecordDetail extends SampleBaseFragment {
    /**
     * 样品编码
     */
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout tvSampleCode;

    /**
     * 频次
     */
    @BindView(R.id.my_layout_frequency)
    MyDrawableLinearLayout tvFrequency;

    /**
     * 点位
     */
    @BindView(R.id.my_layout_point)
    MyDrawableLinearLayout tvPoint;

    /**
     * 内控，平行/样品
     */
    @BindView(R.id.my_layout_control)
    MyDrawableLinearLayout tvControl;

    /**
     * 检测日期
     */
    @BindView(R.id.my_layout_time)
    MyDrawableLinearLayout tvTestTime;

    /**
     * 分析时间
     */
    @BindView(R.id.my_layout_analyse_time)
    MyDrawableLinearLayout tvAnalyseTime;

    /**
     * 分析结果
     */
    @BindView(R.id.my_layout_analyse_result)
    MyDrawableLinearLayout etAnalyseResult;

    /**
     * 结果单位
     */
    @BindView(R.id.my_layout_unit)
    MyDrawableLinearLayout tvTestUnit;

    /**
     * 删除按钮
     */
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;

    /**
     * 保存按钮
     */
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;

    /**
     * 实体
     */
    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_record_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
