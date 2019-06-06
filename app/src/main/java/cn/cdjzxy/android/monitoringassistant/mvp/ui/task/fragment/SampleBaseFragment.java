package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import cn.cdjzxy.android.monitoringassistant.base.base.fragment.BaseFragment;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;

public abstract class SampleBaseFragment extends BaseFragment<ApiPresenter> implements IView {
    protected Sampling sampling;

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(getContext()));
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(mContext, message);
    }

    @Override
    public void setData(@Nullable Object data) {
        if (data != null && data instanceof Message) {
            switch (((Message) data).what) {
                case 0:
                    this.sampling = (Sampling) ((Message) data).obj;
                    break;
                case 1:

                    break;
                default:
                    //do something
                    break;
            }
        }
    }
}
