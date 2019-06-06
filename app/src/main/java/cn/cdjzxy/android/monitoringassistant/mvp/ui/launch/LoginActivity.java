package cn.cdjzxy.android.monitoringassistant.mvp.ui.launch;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;

import com.aries.ui.view.title.TitleBarView;
import com.tbruyelle.rxpermissions2.RxPermissions;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.MainActivity;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.di.component.AppComponent;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDeviceTool;
import cn.cdjzxy.android.monitoringassistant.widget.ClearEditText;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.ExitHelper;
import cn.cdjzxy.android.monitoringassistant.utils.PermissionUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxFileTool;
import me.jessyan.autosize.utils.Preconditions;

/**
 * 登录界面
 */
public class LoginActivity extends MyTitleActivity implements IView {

    @BindView(R.id.et_name)
    ClearEditText etName;
    @BindView(R.id.et_pwd)
    ClearEditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private ExitHelper.TwicePressHolder mExitHelper;

    private String name;
    private String pwd;

    private AppComponent mAppComponent;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "initView: " + RxDeviceTool.isTabletDevice(this));
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        requestPermission();

        String username = UserInfoHelper.get().getUserName();
        etName.setText(RxDataTool.isEmpty(username) ? "" : username);
        //        etPwd.setText("123456");
        // 双击退出
        mExitHelper = new ExitHelper.TwicePressHolder(new ExitHelper.IExitInterface() {

            @Override
            public void showExitTip() {
                showMessage("再按一次退出程序");
            }

            @Override
            public void exit() {
                ArtUtils.exitApp();
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//请求电源管理
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        getUserInfo();
    }

    private void getUserInfo() {
        if (UserInfoHelper.get().isLogin()) {
            UserInfo userInfo = UserInfoHelper.get().getUserInfo();
            name = userInfo.getName();
            pwd = userInfo.getPwd();
            //重新设置weburl
            mPresenter.resetWebUrl(null);
            initAppDataDir();
            toMain();
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mExitHelper.onKeyDown(keyCode, event);
    }


    @Override
    public void showLoading() {
        showLoadingDialog("登录中...");
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        hideLoading();
        Preconditions.checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_FAILURE:
                break;
            case Message.RESULT_OK:
                showMessage("登录成功");
                toMain();
                break;
        }
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        name = etName.getText().toString();
        pwd = etPwd.getText().toString();

        if (RxDataTool.isEmpty(name)) {
            showMessage("请输入用户名!");
            return;
        }

        if (RxDataTool.isEmpty(pwd)) {
            showMessage("请输入密码!");
            return;
        }

        initAppDataDir();


        //        startRepositoeyDownload(name, pwd);

        mPresenter.login(Message.obtain(this, new Object()), name, pwd, this);

    }

    /**
     * 初始化App数据目录
     */
    private void initAppDataDir() {
        RxFileTool.initAppDir(name, this);
        DBHelper.init(this, name);//初始化创建数据库
    }


//    /**
//     * 开启知识库后台任务
//     *
//     * @param name
//     * @param pwd
//     */
//    public void startRepositoeyDownload(String name, String pwd) {
//        Intent intent = new Intent(this, RepositoryService.class);
//        intent.putExtra("name", name);
//        intent.putExtra("pwd", pwd);
//        intent.putExtra("dir", Constant.REPOSITORY_DIR);
//        startService(intent);
//    }

    /**
     * 权限申请
     */
    private void requestPermission() {
        if (mAppComponent == null) mAppComponent = ArtUtils.obtainAppComponentFromContext(this);
        PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                             @Override
                                             public void onRequestPermissionSuccess() {

                                             }

                                             @Override
                                             public void onRequestPermissionFailure(List<String> permissions) {

                                             }

                                             @Override
                                             public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                                             }
                                         },
                new RxPermissions(this),
                mAppComponent.rxErrorHandler(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.BLUETOOTH
                , Manifest.permission.BLUETOOTH_ADMIN
                , Manifest.permission.CAMERA
                , Manifest.permission.INTERNET
                , Manifest.permission.VIBRATE
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION
        );


    }


    /**
     * 进入主界面
     */
    private void toMain() {
//        startRepositoeyDownload(name, pwd);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}