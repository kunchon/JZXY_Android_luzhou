package cn.cdjzxy.android.monitoringassistant.mvp.ui.setting;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baidu.mapapi.NetworkUtil;
import com.github.mikephil.charting.utils.FileUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.fragment.BaseFragment;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.MainActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.launch.LoginActivity;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxFileTool;

import me.jessyan.autosize.utils.Preconditions;


/**
 * 设置
 */

public class SettingFragment extends BaseFragment<ApiPresenter> implements IView {

    Unbinder unbinder;


    public SettingFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(getContext()));
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        Preconditions.checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_FAILURE:

                break;
            case Message.RESULT_OK:
                logout();
                break;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.stv_clear, R.id.stv_modify_pwd, R.id.stv_about, R.id.stv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stv_clear:
                showClearDialog();
                break;
            case R.id.stv_modify_pwd:
                EventBus.getDefault().post(((MainActivity) getActivity()).INT_FRAGMENT_PWD,
                        EventBusTags.TAG_MODIFY_PWD);
                break;
            case R.id.stv_about:
                ArtUtils.startActivity(AboutActivity.class);
                break;
            case R.id.stv_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showClearDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定清除本地缓存数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showClearDialog1();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    private void showClearDialog1() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定清除本地未提交任务数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoHelper.get().saveUserLoginStatee(false);
                        //                        DBHelper.getInstance().dropAllTables();
                        RxFileTool.clearDir(RxFileTool.USER_DATA_DIR);
                        ArtUtils.startActivity(LoginActivity.class);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    private void showLogoutDialog() {

        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定退出登录？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (NetworkUtil.isNetworkAvailable(getContext())) {
                            mPresenter.logout(Message.obtain(SettingFragment.this, new Object()));
                        } else {
                            logout();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void logout() {
        UserInfoHelper.get().saveUserLoginStatee(false);
        ArtUtils.startActivity(LoginActivity.class);
    }
}
