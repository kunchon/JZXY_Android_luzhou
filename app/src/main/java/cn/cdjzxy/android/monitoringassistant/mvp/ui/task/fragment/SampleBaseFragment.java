package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.fragment.BaseFragment;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;

public abstract class SampleBaseFragment extends BaseFragment<ApiPresenter> implements IView {
    protected Sampling mSampling;

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
        if (data != null && data instanceof Sampling) {
            this.mSampling = (Sampling) data;
            //initData(null);
        }
    }

    /**
     * 给view设置选中样式
     *
     * @param isSelect 选中还是未选中
     * @param view     view
     */
    protected void setViewStyleDrawable(boolean isSelect, TextView view) {
        Drawable drawable;
        if (isSelect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_yes_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_yes_data);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_no_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_no_data);
            }
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }


    /**
     * 给view设置选中样式
     *
     * @param isSelect 选中还是未选中
     * @param view     view
     */
    protected void setViewStyle(boolean isSelect, TextView view) {
        Drawable drawable;
        if (isSelect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.ic_has_open);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.ic_has_open);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.ic_has_next);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.ic_has_next);
            }
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
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
}
