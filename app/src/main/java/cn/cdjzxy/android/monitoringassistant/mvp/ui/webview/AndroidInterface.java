package cn.cdjzxy.android.monitoringassistant.mvp.ui.webview;

import android.webkit.JavascriptInterface;

import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

/**
 * describe：公共加载fragment类
 *
 * @author ：鲁宇峰 on 2018/8/8 13：44
 * email：luyufengc@enn.cn
 */
public class AndroidInterface {
    public interface Back {
        void onBack();

        void setTitle(String s);
    }

    public Back back;

    public AndroidInterface(Back back) {
        this.back = back;
    }

    @JavascriptInterface
    public void onBack() {
        if (back != null)
            back.onBack();
    }

    @JavascriptInterface
    public void setTitle(String s) {
        if (RxDataTool.isEmpty(s)) {
            s = "嘉泽云监测";
        }
        if (back != null)
            back.setTitle(s);
    }

}
