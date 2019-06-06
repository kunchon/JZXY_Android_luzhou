package cn.cdjzxy.android.monitoringassistant.base.base.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.IActivity;
import cn.cdjzxy.android.monitoringassistant.base.base.fragment.BaseFragment;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.Cache;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.CacheType;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.StatusBarUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * Activity 基类
 *
 * @param <P>
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IActivity<P> {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;
    protected P mPresenter;

    protected View mContentView;


    private Dialog dialog;


    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(this).cacheFactory().
                    build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //8.0透明bug
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            boolean result = fixOrientation();
        }
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            //如果 initView 返回 0, 框架则不会调用 setContentView(), 当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                if (isTintStatusBar()) {
                    StatusBarUtil.immersive(this);
                }

                mContentView = View.inflate(this, layoutResID, null);
                //解决StatusLayoutManager与SmartRefreshLayout冲突
                if (mContentView.getClass() == SmartRefreshLayout.class) {
                    FrameLayout frameLayout = new FrameLayout(this);
                    if (mContentView.getLayoutParams() != null) {
                        frameLayout.setLayoutParams(mContentView.getLayoutParams());
                    }
                    frameLayout.addView(mContentView);
                    mContentView = frameLayout;
                }
                setContentView(mContentView);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initData(savedInstanceState);

    }


    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    @Override
    public void setPresenter(@Nullable P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null)
            mPresenter = obtainPresenter();
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
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
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
        if (!this.isFinishing()) {
            View layout = getLayoutInflater().inflate(R.layout.dialog_loading, null);
            TextView tvContent = layout.findViewById(R.id.tv_content);
            RelativeLayout rlDialog = layout.findViewById(R.id.rl_dialog);
            if (RxDataTool.isEmpty(str)) {
                tvContent.setVisibility(View.GONE);
            } else {
                rlDialog.setBackgroundResource(R.drawable.loading_bg);
                tvContent.setVisibility(View.VISIBLE);
                tvContent.setText(str);
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
    }


    public void closeLoadingDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
