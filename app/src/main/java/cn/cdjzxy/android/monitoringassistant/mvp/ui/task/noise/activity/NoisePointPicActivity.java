package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.noise.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;

/**
 * 测试点示意图： 添加图片界面
 * 缺少SketchView无法显示，具体适配方法问问昆虫
 */
public class NoisePointPicActivity extends MyTitleActivity {

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
//    @BindView(R.id.sketch_view)
//    SketchView sketchView;
    @BindView(R.id.linear_add)
    LinearLayout layoutAdd;
    @BindView(R.id.linear_clean)
    LinearLayout layoutClean;
    @BindView(R.id.linear_noise)
    LinearLayout linearNoise;
    @BindView(R.id.text_noise_source)
    TextView tvNoiseSource;
    @BindView(R.id.text_noise_point)
    TextView tvNoisePoint;
    @BindView(R.id.text_noise_monitor)
    TextView tvNoiseMonitor;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_noise_point_pic;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
