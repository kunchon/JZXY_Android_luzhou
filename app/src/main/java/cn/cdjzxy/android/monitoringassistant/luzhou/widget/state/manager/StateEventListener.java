package cn.cdjzxy.android.monitoringassistant.luzhou.base.widget.state.manager;

import android.view.View;

/**
 * 异常界面和数据为空界面按钮点击事件监听
 */
public interface StateEventListener {
    void onEventListener(String state, View view);

}