package cn.cdjzxy.monitoringassistant.mvp.ui.module.launch;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.Button;

import com.aries.ui.view.title.TitleBarView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.PermissionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.BroadcastReceiver.NetworkChangeReceiver;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskActivity;
import cn.cdjzxy.monitoringassistant.services.RepositoryService;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.ExitHelper;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import cn.cdjzxy.monitoringassistant.widgets.ClearEditText;
import cn.cdjzxy.monitoringassistant.trajectory.TrajectoryServer;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class LoginActivity extends BaseTitileActivity<ApiPresenter> implements IView {

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

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        mAppComponent = ArtUtils.obtainAppComponentFromContext(this);
        return new ApiPresenter(mAppComponent);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        requestPermission();

        String username = UserInfoHelper.get().getUserName();
        etName.setText(CheckUtil.isEmpty(username) ? "" : username);
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
        checkNotNull(message);
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

        if (CheckUtil.isEmpty(name)) {
            showMessage("请输入用户名!");
            return;
        }

        if (CheckUtil.isEmpty(pwd)) {
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
        Constant.USER_DATA_DIR = getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/" + name;//初始化APP根目录
        Constant.LOG_DIR = Constant.USER_DATA_DIR + "/Logs";//初始化日志目录
        Constant.REPOSITORY_DIR = Constant.USER_DATA_DIR + "/Repository";//初始化知识库文件目录
        Constant.FILE_DIR = Constant.USER_DATA_DIR + "/Files";//初始化文件目录
        Constant.DATABASE_DIR = Constant.USER_DATA_DIR + "/Database";//初始化数据库

        FileUtils.makeDir(Constant.USER_DATA_DIR);//创建APP根目录
        FileUtils.makeDir(Constant.LOG_DIR);//创建日志目录
        FileUtils.makeDir(Constant.REPOSITORY_DIR);//创建知识库文件目录
        FileUtils.makeDir(Constant.FILE_DIR);//创建文件目录
        FileUtils.makeDir(Constant.DATABASE_DIR);//创建数据库
        DBHelper.init(this, name);//初始化创建数据库
    }


    /**
     * 开启知识库后台任务
     *
     * @param name
     * @param pwd
     */
    public void startRepositoeyDownload(String name, String pwd) {
        Intent intent = new Intent(this, RepositoryService.class);
        intent.putExtra("name", name);
        intent.putExtra("pwd", pwd);
        intent.putExtra("dir", Constant.REPOSITORY_DIR);
        startService(intent);
    }

    /**
     * 权限申请
     */
    private void requestPermission() {
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
        startRepositoeyDownload(name, pwd);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}