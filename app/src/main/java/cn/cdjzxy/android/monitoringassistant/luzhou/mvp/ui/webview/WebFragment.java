package cn.cdjzxy.monitoringassistant.mvp.ui.module.webview;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.fanneng.android.web.IWebLayout;
import com.fanneng.android.web.SuperWebX5;
import com.fanneng.android.web.client.ChromeClientCallbackManager;
import com.fanneng.android.web.client.DefaultWebClient;
import com.fanneng.android.web.client.MiddleWareWebChromeBase;
import com.fanneng.android.web.client.MiddleWareWebClientBase;
import com.fanneng.android.web.client.WebDefaultSettingsManager;
import com.fanneng.android.web.client.WebSettings;
import com.fanneng.android.web.file.DownLoadResultListener;
import com.fanneng.android.web.progress.BaseIndicatorView;
import com.fanneng.android.web.utils.PermissionInterceptor;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;

import cn.cdjzxy.monitoringassistant.R;


/**
 * webview 加载
 */
public class WebFragment extends Fragment implements FragmentKeyDown {

    protected SuperWebX5 mSuperWebX5;
    public static final String URL_KEY = "url_key";
    public static final String TAG = WebFragment.class.getSimpleName();
    private MiddleWareWebChromeBase mMiddleWareWebChrome;
    private MiddleWareWebClientBase mMiddleWareWebClient;

    public static WebFragment getInstance(Bundle bundle) {

        WebFragment mWebFragment = new WebFragment();
        if (bundle != null) {
            mWebFragment.setArguments(bundle);
        }

        return mWebFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSuperWebX5 = SuperWebX5.with(this)
                .setSuperWebParent((LinearLayout) view, new LinearLayout.LayoutParams(-1, -1))
                .setCustomIndicator(getIndicatorView())
                .setWebLayout(getWebLayout())
                .setWebSettings(getSettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setReceivedTitleCallback(mCallback)
                .setPermissionInterceptor(mPermissionInterceptor)
                .setNotifyIcon(R.mipmap.download)
                .addJavascriptInterface("Android", new WebViewJavaScriptFunction() {
                    @Override
                    public void onBack() {
                        //返回
                        WebFragment.this.onFragmentKeyDown(KeyEvent.KEYCODE_BACK, null);
                    }
                })
                .useMiddleWareWebChrome(getMiddleWareWebChrome())
                .useMiddleWareWebClient(getMiddleWareWebClient())
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownScheme()
                .openParallelDownload()
                .setSecurityType(SuperWebX5.SecurityType.strict)
                .addDownLoadResultListener(mDownLoadResultListener)
                .createSuperWeb()
                .ready()
                .go(getUrl());

        mSuperWebX5.getJsInterfaceHolder().addJavaObject("Android", new AndroidInterface(new AndroidInterface.Back() {
            @Override
            public void onBack() {
                //传递按键到web层
                boolean result = onFragmentKeyDown(KeyEvent.KEYCODE_BACK, null);
                //web层未处理返回操作，已返回到首页，则退出当前activity
                if (!result) {
                    getActivity().finish();
                }
            }
        }));

    }


    /**
     * 权限拦截器
     * 在触发某些敏感的 Action 时候会回调该方法， 比如定位触发 。
     * 例如 https//:www.baidu.com 该 Url 需要定位权限， 返回false ，如果版本大于等于23 ， 会动态申请权限 ，true 该Url对应页面请求定位失败。
     * 该方法是每次都会优先触发的 ， 开发者可以做一些敏感权限拦截
     */
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        @Override
        public boolean intercept(String url, String[] permissions, String action) {

            return false;
        }
    };

    /**
     * 文件下载结果监听
     */
    protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {

        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {

        }
    };

    /**
     * 获取WebView设置
     */
    public WebSettings getSettings() {
        return WebDefaultSettingsManager.getInstance();
    }

    /**
     * 获取url
     */
    public String getUrl() {
        String target = "";
        if (TextUtils.isEmpty(target = this.getArguments().getString(URL_KEY))) {
            target = "https://www.baidu.com";
        }
        return target;
    }

    /**
     * 获取网页title
     */
    protected ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    };


    /**
     * 获取进度条
     */
    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };

    /**
     * 采用X5客户端拦截URL
     */
    protected com.tencent.smtt.sdk.WebViewClient mWebViewClient = new com.tencent.smtt.sdk.WebViewClient() {
        private HashMap<String, Long> timer = new HashMap<>();

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }


        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?vid=XODEzMjU1MTI4&refer=&tuid=&ua=Mozilla%2F5.0%20(Linux%3B%20Android%207.0%3B%20SM-G9300%20Build%2FNRD90M%3B%20wv)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Version%2F4.0%20Chrome%2F58.0.3029.83%20Mobile%20Safari%2F537.36&source=exclusive-pageload&cookieid=14971464739049EJXvh|Z6i1re#Intent;scheme=youku;package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            if (url.startsWith("intent://")) {
                return true;
            } else return url.startsWith("youku");

        }

        int index = 1;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            Log.i(TAG, "url:" + url + " onPageStarted  url:" + getUrl());
            timer.put(url, new Long(System.currentTimeMillis()));


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (timer.get(url) != null) {
                long overTime = System.currentTimeMillis();
                Long startTime = timer.get(url);
                Log.i(TAG, "  page url:" + url + "  used time:" + (overTime - startTime));
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSuperWebX5.uploadFileResult(requestCode, resultCode, data);
    }


    protected IWebLayout getWebLayout() {
        return null;
    }

    protected BaseIndicatorView getIndicatorView() {
        return null;
    }


    @Override
    public void onResume() {
        mSuperWebX5.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mSuperWebX5.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return mSuperWebX5.handleKeyEvent(keyCode, event);
    }

    @Override
    public void onDestroyView() {
        mSuperWebX5.getWebLifeCycle().onDestroy();
        super.onDestroyView();
    }

    public MiddleWareWebChromeBase getMiddleWareWebChrome() {
        return mMiddleWareWebChrome;
    }

    public MiddleWareWebClientBase getMiddleWareWebClient() {
        return mMiddleWareWebClient;
    }
}
