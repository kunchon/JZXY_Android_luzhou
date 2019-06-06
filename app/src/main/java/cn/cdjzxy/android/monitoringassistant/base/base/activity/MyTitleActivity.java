package cn.cdjzxy.android.monitoringassistant.base.base.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;


import org.simple.eventbus.Subscriber;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.IActivity;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.ITitleView;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.TitleDelegate;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.Cache;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.CacheType;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.launch.LoginActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.webview.WebActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.webview.WebFragment;
import cn.cdjzxy.android.monitoringassistant.services.TraceService;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.StatusBarUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * Activity 基类
 */
public abstract class MyTitleActivity extends AppCompatActivity implements IActivity<ApiPresenter>, ITitleView, IView {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache mCache;
    private Unbinder mUnbinder;
    protected ApiPresenter mPresenter;

    private long LastTime = 0;

    protected View mContentView;
    protected TitleDelegate mTitleDelegate;
    protected TitleBarView mTitleBar;

    private Dialog dialog;
    private TextView dialogTextView;

    protected Context mContext;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        try {
            int layoutResID = initView(savedInstanceState);
            //如果 initView 返回 0, 框架则不会调用 setContentView(), 当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                if (isTintStatusBar()) {
                    StatusBarUtil.immersive(this);
                }
                mContentView = View.inflate(this, layoutResID, null);
                setContentView(mContentView);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);

                mTitleDelegate = new TitleDelegate(mContentView, this, this.getClass());
                mTitleBar = mTitleDelegate.mTitleBar;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return mPresenter = new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null)
            mPresenter = obtainPresenter();
    }

    @Override
    public void setPresenter(@Nullable ApiPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mPresenter = null;
        this.mUnbinder = null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    /**
     * 是否使用整体沉浸式状态栏
     *
     * @return
     */
    protected boolean isTintStatusBar() {
        return true;
    }


    /**
     * 是否使用自定义布局
     *
     * @return
     */
    protected boolean isCustomContent() {
        return false;
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link } 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }


    public void showLoadingDialog() {
        showLoadingDialog("加载中");
    }

    public void showLoadingDialog(String str) {
        showLoadingDialog(str, false);
    }


    public void showLoadingDialog(String str, boolean isCanCanceled) {
        if (this.isFinishing()) {
            return;
        }

        if (dialog != null && dialog.isShowing()) {
            setLoadingDialogText(str);
            return;
        }

        View layout = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        dialogTextView = layout.findViewById(R.id.tv_content);
        RelativeLayout rlDialog = layout.findViewById(R.id.rl_dialog);
        if (RxDataTool.isEmpty(str)) {
            dialogTextView.setVisibility(View.GONE);
        } else {
            rlDialog.setBackgroundResource(R.drawable.loading_bg);
            dialogTextView.setVisibility(View.VISIBLE);
            dialogTextView.setText(str);
        }
        if (dialog == null) {
            dialog = new Dialog(this, R.style.loadingDialog);
        }
        dialog.setContentView(layout);
        dialog.setCancelable(isCanCanceled);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        dialog.show();
    }

    public void setLoadingDialogText(String str) {
        if (dialogTextView != null) {
            dialogTextView.setText(str);
        }
    }

    public void closeLoadingDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Subscriber(tag = EventBusTags.TAG_TOKEN_EXPIRE)
    private void reLogin(boolean isReLogin) {
        //每30s可激活一次
        if (LastTime != 0 && System.currentTimeMillis() - LastTime < 30000) {
            //初始值为0
            return;
        }
        UserInfoHelper.get().saveUserLoginStatee(false);
        ArtUtils.startActivity(LoginActivity.class);
        LastTime = System.currentTimeMillis();
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    /**
     * 显示一个dialog
     *
     * @param title    提示标题
     * @param msg      提示消息
     * @param listener 确定事件回调
     */
    public void showDialog(String title, String msg, DialogInterface.OnClickListener listener) {
        final Dialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    /**
     * 显示一个dialog
     *
     * @param title          提示标题
     * @param message        提示消息
     * @param pStr           右边按钮文字提示
     * @param nStr           左边按钮文字提示
     * @param pClickListener 右边点击事件回调
     * @param nClickListener 左边点击事件回调
     */
    protected void showDialog(String title, String message,
                           String pStr, String nStr,
                           DialogInterface.OnClickListener pClickListener,
                           DialogInterface.OnClickListener nClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(pStr, pClickListener);
        builder.setNegativeButton(nStr, nClickListener);
        builder.create().show();
    }

    /**
     * 显示用户没权限
     *
     * @param hint 权限提示
     * @param name 名称
     */
    public void showNoPermissionDialog(String hint, String name) {
        showDialog("提示", String.format("您需要：%s权限," + hint, name) + getString(R.string.Permission_hint_str), null);
    }


    /*********************************轨迹上传业务****************************************************************/

    /**
     * 开启轨迹服务
     */
    public void startTraceService() {
        //开启轨迹服务
        Intent intent = new Intent();
        intent.setClass(this, TraceService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    /**
     * 动态隐藏软键盘
     */
    public void hideSoftInput() {
        View view = getCurrentFocus();
        if (view == null) view = new View(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 打开web页面
     *
     * @param url   链接
     * @param title title
     */
    public void startWebActivity(String url, String title, boolean isUserWebUrl) {
        if (isUserWebUrl) {
            url = UserInfoHelper.get().getUser().getWebUrl() + url;
            startWebActivity(url, title);
        } else {
            startWebActivity(url, title);
        }
    }

    /**
     * 打开web页面
     *
     * @param url   链接
     * @param title title
     */
    public void startWebActivity(String url, String title) {
        Intent intent = new Intent();
        intent.setClass(this, WebActivity.class);
        intent.putExtra(WebFragment.URL_KEY, url);
        intent.putExtra(WebActivity.KEY_TITLE, title);
        startActivity(intent);
    }

    /**
     * 设置标题
     *
     * @param s
     */
    public void setTitle(String s) {
        if (mTitleBar != null) {
            mTitleBar.setTitleMainText(s);
        }
    }
}
