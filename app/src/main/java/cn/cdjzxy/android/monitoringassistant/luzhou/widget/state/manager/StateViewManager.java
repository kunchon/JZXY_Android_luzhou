package cn.cdjzxy.android.monitoringassistant.luzhou.base.widget.state.manager;


import android.view.View;
import android.view.ViewGroup;

/**
 * 视图状态管理
 */
public interface StateViewManager {


    ViewGroup getOverallView();


    /**
     * 设置核心布局
     *
     * @param resId
     * @return
     */
    View setContentView(int resId);

    View setContentView(View view);


    View getContentView();


}
