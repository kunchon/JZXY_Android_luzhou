package cn.cdjzxy.android.monitoringassistant.mvp.ui.webview;

import android.webkit.JavascriptInterface;

public interface WebViewJavaScriptFunction {
    @JavascriptInterface
    void onBack();
}
