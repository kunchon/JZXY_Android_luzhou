/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.cdjzxy.android.monitoringassistant.base.base.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.IFragment;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.Cache;
import cn.cdjzxy.android.monitoringassistant.base.integration.cache.CacheType;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


public abstract class MyBaseFragment<P extends IPresenter> extends Fragment implements IFragment<P> {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    protected P                     mPresenter;

    private Dialog dialog;

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
        return initView(inflater, container, savedInstanceState);
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

    public void showLoadingDialog() {
        showLoadingDialog("加载中");
    }

    public void showLoadingDialog(String str) {
        showLoadingDialog(str, true);
    }


    public void showLoadingDialog(String str, boolean isCanCanceled) {

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
            dialog = new Dialog(getContext(), R.style.loadingDialog);
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


    public void closeLoadingDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 动态隐藏软键盘
     *
     */
    public void hideSoftInput() {
        View view = getActivity().getCurrentFocus();
        if (view == null) view = new View(getActivity());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
