
package cn.cdjzxy.android.monitoringassistant.base.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.title.TitleBarView;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.IFragment;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.ITitleView;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.TitleDelegate;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.Cache;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.CacheType;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;


/**
 * 可以使用标题的Fragment
 */
public abstract class BaseTitleFragment<P extends IPresenter> extends Fragment implements IFragment<P>, ITitleView {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    protected P mPresenter;

    protected TitleDelegate mTitleDelegate;
    protected TitleBarView mTitleBar;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        mTitleDelegate = new TitleDelegate(view, this, this.getClass());
        mTitleBar = mTitleDelegate.mTitleBar;
        return view;
    }

    @Override
    public void setPresenter(@Nullable P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = obtainPresenter();
        }
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

}
