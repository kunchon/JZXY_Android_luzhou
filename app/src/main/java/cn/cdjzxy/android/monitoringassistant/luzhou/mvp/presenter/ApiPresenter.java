package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;
import com.wonders.health.lib.base.mvp.Message;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.ApiRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.api.Api;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.UploadFileResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Dic;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Enterprise;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.gps.Gps;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormFlow;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.services.TraceService;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import timber.log.Timber;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity.TYPE_TASK;

/**
 * App相关的数据操作Presenter
 */
public class ApiPresenter extends BasePresenter<ApiRepository> {

    public static final double PROGRESS = Math.round((100 / 21.0) * 10) / 10.0;
    private double SAMPLING_PROGRESS = 0.0;

    private void setSAMPLING_PROGRESS(int size) {
        SAMPLING_PROGRESS = ((double) (Math.round((100 / size) * 1)));
    }

    private AppComponent appComponent;

    public ApiPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(ApiRepository.class));
        this.appComponent = appComponent;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }


    /**
     * 登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    public void login(final Message msg, String name, String pwd, Context context) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            loginOnline(msg, name, pwd);
        } else {
            loginLocal(msg, name, pwd);
        }
    }

    /**
     * 退出登录
     *
     * @param msg
     */
    public void logout(final Message msg) {
        mModel.logout()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        UserInfoHelper.get().saveUserLoginStatee(false);
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 修改密码
     *
     * @param msg
     */
    public void modifyPwd(final Message msg, String oldPwd, String newPwd) {
        mModel.modifyPwd(oldPwd, newPwd)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * 在线登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    private void loginOnline(final Message msg, String name, final String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "0");
        params.put("loginId", name);
        params.put("pwd", pwd);
        mModel.login(params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> response) {
                        UserInfo userInfo = response.getData();
                        userInfo.setPwd(pwd);
                        DBHelper.get().getUserInfoDao().deleteAll();
                        DBHelper.get().getUserInfoDao().insert(userInfo);
                        UserInfoHelper.get().saveUserLoginStatee(true);
                        UserInfoHelper.get().saveUserInfo(userInfo);
                        HawkUtil.putBoolean("isUpdated", false);

                        //重新设置weburl
                        resetWebUrl(userInfo);

                        msg.what = Message.RESULT_OK;
                        msg.obj = response.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 本地登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    private void loginLocal(final Message msg, String name, String pwd) {
        UserInfo userInfo = UserInfoHelper.get().getUser();
        if (CheckUtil.isNull(userInfo) || CheckUtil.isEmpty(userInfo.getWorkNo())) {
            msg.getTarget().showMessage("用户信息不存在，请先联网登录更新数据");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else if (!name.equals(userInfo.getWorkNo())) {
            msg.getTarget().showMessage("输入的用户名错误,请重新输入");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else if (!pwd.equals(userInfo.getPwd())) {
            msg.getTarget().showMessage("输入的密码错误,请重新输入");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else {
            UserInfoHelper.get().saveUserLoginStatee(true);
            //重新设置weburl
            resetWebUrl(userInfo);

            msg.what = Message.RESULT_OK;
            msg.obj = userInfo;
            msg.handleMessageToTarget();
        }
    }

    /**
     * 获取二维码结果
     *
     * @param msg
     * @param qrCode
     */
    public void getQrInfo(final Message msg, String qrCode) {
        mModel.getQrInfo(qrCode)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<QrMoreInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<QrMoreInfo> qrMoreInfoBaseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = qrMoreInfoBaseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getDevices(final Message msg) {
        mModel.getDevices()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Devices>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Devices>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getDevicesDao().deleteAll();
                            DBHelper.get().getDevicesDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethods(final Message msg) {
        mModel.getMethods()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Methods>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Methods>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodsDao().deleteAll();
                            DBHelper.get().getMethodsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItems(final Message msg) {
        mModel.getMonItems()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItems>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItems>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemsDao().deleteAll();
                            DBHelper.get().getMonItemsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getTags(final Message msg) {
        mModel.getTags()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Tags>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Tags>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getTagsDao().deleteAll();
                            DBHelper.get().getTagsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItemTagRelation(final Message msg) {
        mModel.getMonItemTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemTagRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemTagRelationDao().deleteAll();
                            DBHelper.get().getMonItemTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethodTagRelation(final Message msg) {
        mModel.getMethodTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodTagRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodTagRelationDao().deleteAll();
                            DBHelper.get().getMethodTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItemMethodRelation(final Message msg) {
        mModel.getMonItemMethodRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemMethodRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemMethodRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemMethodRelationDao().deleteAll();
                            DBHelper.get().getMonItemMethodRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethodDevRelation(final Message msg) {
        mModel.getMethodDevRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodDevRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodDevRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodDevRelationDao().deleteAll();
                            DBHelper.get().getMethodDevRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getRight(final Message msg) {
        mModel.getRight()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Rights>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Rights>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getRightsDao().deleteAll();
                            DBHelper.get().getRightsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getEnvirPoint(final Message msg) {
        mModel.getEnvirPoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnvirPoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnvirPoint>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnvirPointDao().deleteAll();
                            DBHelper.get().getEnvirPointDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息：权限";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getEnterRelatePoint(final Message msg) {
        mModel.getEnterRelatePoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnterRelatePoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnterRelatePoint>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnterRelatePointDao().deleteAll();
                            DBHelper.get().getEnterRelatePointDao().insertInTx(baseResponse.getData());

                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息：点位";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getEnterprise(final Message msg) {
        mModel.getEnterprise()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Enterprise>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Enterprise>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnterpriseDao().deleteAll();
                            DBHelper.get().getEnterpriseDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息：企业";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getDic(final Message msg, int type) {
        mModel.getDic(type)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Dic>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Dic>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getDicDao().deleteAll();
                            DBHelper.get().getDicDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getWeather(final Message msg) {
        mModel.getWeather()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<String>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<String>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            Weather weather = new Weather();
                            weather.setWeathers(baseResponse.getData());
                            DBHelper.get().getWeatherDao().deleteAll();
                            DBHelper.get().getWeatherDao().insert(weather);

                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getUser(final Message msg) {
        mModel.getUser()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<User>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<User>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getUserDao().deleteAll();
                            DBHelper.get().getUserDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getUnit(final Message msg) {
        mModel.getUnit()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Unit>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Unit>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getUnitDao().deleteAll();
                            DBHelper.get().getUnitDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getMsgs(final Message msg) {
        mModel.getMsgs()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Msg>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Msg>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMsgDao().deleteAll();
                            DBHelper.get().getMsgDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步基础信息：消息";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void putReadMsg(final Message msg, List<String> messageIds) {
        mModel.putReadMsg(messageIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.str = "正在同步基础信息：消息";
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getAllTasks(final Message msg) {
        mModel.getAllTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Project>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Project>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getProjectDao().deleteAll();
                            DBHelper.get().getProjectDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * 获得与我相关的任务
     * 添加：判断时间先后保留信息
     *
     * @param msg
     */
    public void getMyTasks(final Message msg) {
        mModel.getMyTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Project>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Project>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Project> projects = baseResponse.getData();//所有与我相关的任务
                            //
                            ProjectDao dao = DBHelper.get().getProjectDao();
                            //日期转化
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            for (Project project : projects) {
                                Project old = dao.queryBuilder()
                                        .where(ProjectDao.Properties.Id.eq(project.getId()))
                                        .build()
                                        .unique();
                                /**如果没有冲突，那么直接插入**/
                                if (old == null) {
                                    List<ProjectDetial> projectDetials = project.getProjectDetials();
                                    if (!CheckUtil.isEmpty(projectDetials)) {
                                        DBHelper.get()
                                                .getProjectDetialDao()
                                                .insertInTx(projectDetials);
                                    }
                                    List<ProjectContent> projectContentList = project.getProjectContents();
                                    if (!CheckUtil.isEmpty(projectContentList)) {
                                        DBHelper.get()
                                                .getProjectContentDao()
                                                .insertInTx(projectContentList);
                                    }
                                    dao.insert(project);
                                    continue;
                                }
                                /**有冲突，比较时间先后，暂时默认相同时间使用服务器版本**/
                                try {
                                    Date remoteDate = sdf.parse(project.getUpdateTime());
                                    Date localDate = sdf.parse(old.getUpdateTime());
                                    if (remoteDate.compareTo(localDate) >= 0) {
                                        //服务端后更新,以及相等情况
                                        //更新本地的记录
                                        dao.update(project);
                                        List<ProjectDetial> projectDetials = project.getProjectDetials();
                                        if (!CheckUtil.isEmpty(projectDetials)) {
                                            DBHelper.get()
                                                    .getProjectDetialDao()
                                                    .updateInTx(projectDetials);
                                        }
                                        List<ProjectContent> projectContentList = project.getProjectContents();
                                        if (!CheckUtil.isEmpty(projectContentList)) {
                                            for (ProjectContent content : projectContentList) {
                                                ProjectContent dbContent = DBHelper.get().getProjectContentDao().
                                                        queryBuilder().where(ProjectContentDao.Properties.Id.eq(content.getId())).unique();
                                                if (dbContent != null) {
                                                    DBHelper.get()
                                                            .getProjectContentDao()
                                                            .update(content);
                                                } else {
                                                    DBHelper.get()
                                                            .getProjectContentDao()
                                                            .insert(content);
                                                }
                                            }
                                        }

                                    }
                                    //如果是客户端后更新,那么不用任何操作,因为存储的就是最新的
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

//                            DBHelper.get().getProjectDetialDao().deleteAll();
//                            DBHelper.get().getProjectDao().deleteAll();
//                            for (Project project : projects) {
//                                List<ProjectDetial> projectDetials = project.getProjectDetials();
//                                if (!CheckUtil.isEmpty(projectDetials)) {
//                                    DBHelper.get().getProjectDetialDao().insertInTx(projectDetials);
//                                }
//                            }
//                            DBHelper.get().getProjectDao().insertInTx(baseResponse.getData());
                            List<String> taskIds = new ArrayList<>();
                            setSAMPLING_PROGRESS(projects.size());
                            for (int i = 0; i < projects.size(); i++) {
                                String id = projects.get(i).getId();
                                if (!CheckUtil.isEmpty(id)) {
                                    taskIds.add(projects.get(i).getId());
                                    getSampling(Message.obtain(msg), taskIds, i, projects.size());
                                    taskIds.clear();
                                }
                            }
                        }

                        msg.str = "正在同步采样单";
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.arg1 = 0;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * 获取采样规范
     *
     * @param msg
     */
    public void getSamplingStantd(final Message msg) {
        mModel.getSamplingStantd()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<SamplingStantd>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<SamplingStantd>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getSamplingStantdDao().deleteAll();
                            DBHelper.get().getSamplingStantdDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步采样单规范";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getFormSelect(final Message msg) {
        mModel.getFormSelect()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Form>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Form>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Form> forms = baseResponse.getData();
                            DBHelper.get().getFormFlowDao().deleteAll();
                            DBHelper.get().getFormSelectDao().deleteAll();
                            DBHelper.get().getFormDao().deleteAll();
                            for (Form form : forms) {
                                List<FormSelect> formSelects = form.getFormSelectList();
                                if (!CheckUtil.isEmpty(formSelects)) {
                                    for (FormSelect formSelect : formSelects) {
                                        List<FormFlow> formFlows = formSelect.getFormFlows();
                                        if (!CheckUtil.isEmpty(formFlows)) {
                                            DBHelper.get().getFormFlowDao().insertInTx(formFlows);
                                        }
                                    }
                                    DBHelper.get().getFormSelectDao().insertInTx(formSelects);
                                }
                            }
                            DBHelper.get().getFormDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.str = "正在同步采样单分类";
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 获取所有采样单信息(支持批量)
     *
     * @param msg
     */
    public void getSampling(final Message msg, List<String> projectIds, int i, int size) {
        if (CheckUtil.isEmpty(projectIds)) return;
        mModel.getSampling(projectIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        Log.i(TAG, String.format("listSize:%d,i:%d", size, i));
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!CheckUtil.isEmpty(samplings)) {
                                //DBHelper.get().getSamplingFormStandDao().deleteAll();
                                for (Sampling sampling : samplings) {
                                    saveOneSampling(sampling);
                                }
                            }
                        }

                        Message message = Message.obtain(msg);
                        message.what = TYPE_TASK;
                        message.arg1 = size;
                        message.obj = SAMPLING_PROGRESS;
                        message.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));

    }

    /**
     * 获取所有采样单信息(支持批量)
     *
     * @param msg
     */
    public void getSampling(final Message msg, List<String> projectIds) {
        mModel.getSampling(projectIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!CheckUtil.isEmpty(samplings)) {
                                //DBHelper.get().getSamplingFormStandDao().deleteAll();
                                for (Sampling sampling : samplings) {
                                    saveOneSampling(sampling);
                                }
                            }
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 根据ID获取采样单
     *
     * @param msg
     * @param samplingId
     */
    public void getSamplinByID(final Message msg, String samplingId) {
        mModel.getSamplingById(samplingId)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse)) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!CheckUtil.isEmpty(samplings)) {
                                for (Sampling sampling : samplings) {
                                    saveOneSampling(sampling);
                                }
                            }
                        }

                        msg.what = Message.RESULT_OK;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
//                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.obj = message;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 保存一个采样单
     *
     * @param sampling
     */
    private void saveOneSampling(Sampling sampling) {
        String formName = sampling.getFormName();

        if (!CheckUtil.isNull(formName) && formName.equals(TaskDetailActivity.NAME_PRECIPITATION)) {
            sampling.setFormPath(TaskDetailActivity.PATH_PRECIPITATION);
        } else if (!CheckUtil.isNull(formName) && formName.equals(TaskDetailActivity.NAME_WASTEWATER)) {
            sampling.setFormPath(TaskDetailActivity.PATH_WASTEWATER);
        } else if (!CheckUtil.isNull(formName) && formName.equals(TaskDetailActivity.NAME_INSTRUMENTAL)) {
            sampling.setFormPath(TaskDetailActivity.PATH_INSTRUMENTAL);
        } else if (!CheckUtil.isNull(formName) && formName.equals(TaskDetailActivity.NAME_NOISE_FACTORY)) {
            sampling.setFormPath(TaskDetailActivity.PATH_NOISE_FACTORY);
        }
        //处理有相同SamplingNo不同id的情况
        List<Sampling> localSamplings = getLocalSamplingsByNo(sampling.getSamplingNo());
        if (!CheckUtil.isEmpty(localSamplings)) {
            DBHelper.get().getSamplingDao().deleteInTx(localSamplings);
        }
        //DBHelper.get().getSamplingDao().delete(sampling);

        //删除对应的SamplingDetail
        List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(sampling.getId())).build().list();
        if (!CheckUtil.isEmpty(samplingDetailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
        }
        //新增SamplingDetail
        List<SamplingDetail> samplingDetails = sampling.getSamplingDetailResults();
        if (!CheckUtil.isEmpty(samplingDetails)) {
            for (SamplingDetail samplingDetail : samplingDetails) {
                DBHelper.get().getSamplingDetailDao().delete(samplingDetail);
            }
            DBHelper.get().getSamplingDetailDao().insertInTx(samplingDetails);
        }

        List<SamplingContent> samplingContents = sampling.getSamplingContentResults();
        if (!CheckUtil.isEmpty(samplingContents)) {
            for (SamplingContent samplingContent : samplingContents) {
                List<SamplingContent> dbContentList = HelpUtil.getSamplingContent(samplingContent.getProjectId(), samplingContent.getSamplingId(), samplingContent.getSampingCode(), samplingContent.getSamplingType());
                if (!CheckUtil.isEmpty(dbContentList)) {
                    DBHelper.get().getSamplingContentDao().deleteInTx(dbContentList);
                }
                samplingContent.setId(UUID.randomUUID().toString());
            }
            DBHelper.get().getSamplingContentDao().insertInTx(samplingContents);
        }

        //文件
        List<SamplingFile> samplingFileList = sampling.getHasFile() == null ? new ArrayList<>() : sampling.getHasFile();
        if (samplingFileList != null && samplingFileList.size() > 0) {
            for (SamplingFile samplingFile : samplingFileList) {
                //从数据库查询对应的文件，Id由于是服务端提供的，所以不会变，但是SamplingId可能会变化，所以这里做一次更新
                List<SamplingFile> dbFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.Id.eq(samplingFile.getId())).list();
                //数据库中未找到，直接下载
                if (dbFiles == null || dbFiles.size() == 0) {
                    downloadSamplingFile(null, samplingFile, sampling.getId());
                } else {
                    for (SamplingFile dbFile : dbFiles) {
                        if (!new File(dbFile.getFilePath()).exists()) {
                            //本地图片不存在了，重新下载图片
                            downloadSamplingFile(dbFile, samplingFile, sampling.getId());
                        } else if ((dbFile.getSamplingId() == null &&
                                sampling.getId() != null) ||
                                (dbFile.getSamplingId() != null && !dbFile.getSamplingId().equals(sampling.getId()))) {
                            dbFile.setSamplingId(sampling.getId());
                            DBHelper.get().getSamplingFileDao().update(dbFile);
                        }
                    }
                }
            }
        }
        switch (sampling.getFormPath()) {
            case TaskDetailActivity.PATH_NOISE_FACTORY:    //噪声表在此下载测点示意图图片
                if (sampling.getPrivateData() != null) {
                    NoisePrivateData privateData = new Gson().
                            fromJson(sampling.getPrivateData(), NoisePrivateData.class);
                    if (privateData.getImageSYT() != null && !privateData.getImageSYT().equals("")) {
                        new DownLoadNoiseAsyncTask(sampling).execute(privateData);
                        return;
                    }
                }
                break;
            case TaskDetailActivity.PATH_INSTRUMENTAL:
                //同步仪器法监测结果
                List<SamplingDetail> samplingDetailYQFs = sampling.getSamplingDetailYQFs();
                if (!CheckUtil.isEmpty(samplingDetailYQFs)) {
                    for (SamplingDetail samplingDetail : samplingDetailYQFs) {
                        QueryBuilder qb = DBHelper.get().getSamplingDetailDao().queryBuilder();
                        List<SamplingDetail> dbSamplingDetailYQFs = qb.where(qb.or(SamplingDetailDao.Properties.Id.like("YQF%"), SamplingDetailDao.Properties.Id.eq(samplingDetail.getId())), SamplingDetailDao.Properties.SampingCode.eq(samplingDetail.getSampingCode())).list();
                        //匹配采样单编号一样的数据或Id一样，覆盖。
//                List<SamplingDetail> dbSamplingDetailYQFs = DBHelper.get().getSamplingDetailDao().queryBuilder().whereOr(SamplingDetailDao.Properties.Id.eq(samplingDetail.getId()), SamplingDetailDao.Properties.SampingCode.eq(samplingDetail.getSampingCode())).list();
                        if (!CheckUtil.isEmpty(dbSamplingDetailYQFs)) {
                            DBHelper.get().getSamplingDetailDao().deleteInTx(dbSamplingDetailYQFs);
                        }
                    }
                    DBHelper.get().getSamplingDetailDao().insertOrReplaceInTx(samplingDetailYQFs);
                }
                break;
            case TaskDetailActivity.PATH_WASTEWATER://水和废水
                //删除本地对应的分瓶信息
                List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                        where(SamplingFormStandDao.Properties.SamplingId.eq(sampling.getId())).
                        orderAsc(SamplingFormStandDao.Properties.Index).list();
                DBHelper.get().getSamplingFormStandDao().deleteInTx(formStantdsList);
                //新增分瓶信息
                List<SamplingFormStand> samplingFormStands = sampling.getSamplingFormStandResults();
                if (!CheckUtil.isEmpty(samplingFormStands)) {
                    DBHelper.get().getSamplingFormStandDao().insertInTx(samplingFormStands);
                }
                break;
        }
        sampling.setIsCanEdit(SamplingUtil.setSampIsCanEdit(sampling));
        sampling.setIsLocal(false);
        sampling.setIsFinish(SamplingUtil.isSamplingFinsh(sampling));
        if (DbHelpUtils.getDbSampling(sampling.getId()) != null) {
            DBHelper.get().getSamplingDao().update(sampling);
        } else {
            DBHelper.get().getSamplingDao().insert(sampling);
        }

    }

    /**
     * 下载采样单文件
     *
     * @param samplingFile
     * @param remoteFile
     * @param samplingId
     * @return
     */
    private void downloadSamplingFile(SamplingFile samplingFile, SamplingFile remoteFile, String samplingId) {
        DonwloadParams params = new DonwloadParams();
        params.remoteFile = remoteFile;
        params.samplingFile = samplingFile;
        params.samplingId = samplingId;

        new DownloadAsyncTask().execute(params);
    }

//    /**
//     * 下载采样单文件
//     *
//     * @param samplingFile
//     * @param remoteFile
//     * @param samplingId
//     * @return
//     */
//    private void donwloadSamplingFile(SamplingFile samplingFile, SamplingFile remoteFile, String samplingId) {
//        //下载并保存文件
//        String localPath = donwloadFile(remoteFile.getFilePath(), remoteFile.getFileName());
//
//        //是否成功
//        if (TextUtils.isEmpty(localPath)) {
//            return;
//        }
//
//        if (samplingFile == null) {
//            //创建文件
//            samplingFile = new SamplingFile();
//            samplingFile.setLocalId(UUID.randomUUID().toString());
//        }
//
//        samplingFile.setSamplingId(samplingId);
//        samplingFile.setFilePath(localPath);
//        samplingFile.setId(remoteFile.getId());
//        samplingFile.setFileName(remoteFile.getFileName());
//        samplingFile.setUpdateTime(remoteFile.getUpdateTime());
//
//        DBHelper.get().getSamplingFileDao().insertOrReplace(samplingFile);
//    }
//

    /**
     * 下载文件
     *
     * @param fileAddr
     * @return 保存路径
     */
    private String downloadFile(String fileAddr, String fileName) {

        try {
            int lastIndex = fileAddr.lastIndexOf("/");
            if (lastIndex >= 0) {
                String fn = fileAddr.substring(lastIndex + 1);
                //文件名手动编码
                fileAddr = fileAddr.replace(fn, java.net.URLEncoder.encode(fn, "utf-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String fileUrl = UserInfoHelper.get().getUserInfo().getWebUrl() + fileAddr;

        try {
            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            con.setReadTimeout(30000);
            con.setConnectTimeout(15000);
            InputStream is = null;
            if (((HttpURLConnection) con).getResponseCode() == 200) {
                is = con.getInputStream();//获取输入流
            }
            if (is == null) {
                return null;
            }


            String cacheDir = this.appComponent.appManager().getCurrentActivity().getCacheDir().getPath();
            File newFile = new File(cacheDir + "/" + fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);//指定文件保存路径，代码看下一步

            byte[] buf = new byte[1024 * 10];
            int ch;
            while ((ch = is.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
            }

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            is.close();

            Log.d("donwloadFile", "donwload " + fileUrl + " to " + newFile.getPath() + " success!");

            return newFile.getPath();
        } catch (Exception e) {
            Log.e("donwloadFile", "Url:" + fileUrl + " Exc:" + e);
            e.printStackTrace();
        }

        return null;
    }


    public void putSamplingFinish(final Message msg, String projectId, String comment) {
        Map<String, String> params = new HashMap<>();
        params.put("projectID", projectId);
        params.put("finishComment", comment);
        mModel.putSamplingFinish(params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void putProjectContent(final Message msg, ProjectPlan projectPlan) {
        mModel.putProjectContent(projectPlan)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        //提交采样方案成功后不提示
                        //msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void createTable(final Message msg, PreciptationSampForm preciptationSampForm) {
        mModel.createTable(preciptationSampForm)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Constants.NET_RESPONSE_CODE_259;
                        msg.obj = baseResponse.getMessage();
                        try {
                            msg.str = (String) baseResponse.getData();//服务端的采样单id
                        } catch (Exception e) {
                            Log.e(TAG, "onSuccess: " + e.toString());
                            msg.str = preciptationSampForm.getSampForm().getId();
                        }
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        if (responseCode == Constants.NET_RESPONSE_SAMPLING_DIFFER) {
                            msg.what = Constants.NET_RESPONSE_SAMPLING_DIFFER;
                        } else {
                            msg.obj = message;
                            msg.what = Message.RESULT_FAILURE;
                        }

                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void updateTable(final Message msg, PreciptationSampForm preciptationSampForm) {
        mModel.updateTable(preciptationSampForm)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void deleteTable(final Message msg, String tableId) {
        mModel.deleteTable(tableId)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void uploadFile(final Message msg, List<MultipartBody.Part> parts, Map<String, RequestBody> params) {
        mModel.uploadFile(parts, params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<Response<UploadFileResponse<List<FileInfoData>>>>() {
                    @Override
                    public void onSuccess(Response<UploadFileResponse<List<FileInfoData>>> baseResponse) {
                        if (baseResponse.code() == 200) {
                            UploadFileResponse<List<FileInfoData>> data = baseResponse.body();
                            if (data.getCode() == 1) {
                                msg.what = Message.RESULT_OK;
                                msg.obj = data.getData();
                            } else {
                                msg.obj = String.format("上传错误：%s，%d", data.getMessage(), data.getCode());
                                msg.what = Message.RESULT_FAILURE;
                            }
                        } else if (baseResponse.code() == 413) {
                            msg.obj = "文件过大！";
                            msg.what = Message.RESULT_FAILURE;
                        } else {
                            msg.obj = String.format("未知错误(%d)", baseResponse.code());
                            msg.what = Message.RESULT_FAILURE;
                        }

                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int code) {
//                        msg.getTarget().showMessage(message);
                        msg.obj = message;
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 重新设置WebUrl
     *
     * @param userInfo
     */
    public void resetWebUrl(UserInfo userInfo) {
        if (CheckUtil.isNull((userInfo))) {
            userInfo = UserInfoHelper.get().getUserInfo();
        }

        //动态设置weburl
        if (!CheckUtil.isEmpty(userInfo.getWebUrl())) {
            RetrofitUrlManager.getInstance().putDomain(Api.LOGIN_RESP_WEBURL, userInfo.getWebUrl());
        }
    }

    private List<Sampling> getLocalSamplingsByNo(String samplingNo) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.eq(samplingNo)).list();
        return samplings;
    }

    class DonwloadParams {
        public SamplingFile samplingFile;
        public SamplingFile remoteFile;
        public String samplingId;
    }

    //异步下载的任务
    class DownloadAsyncTask extends AsyncTask<DonwloadParams, Void, Void> {

        //onPreExecute用于异步处理前的操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //在doInBackground方法中进行异步任务的处理.
        @Override
        protected Void doInBackground(DonwloadParams... params) {
            //获取传进来的参数
            DonwloadParams param = params[0];

            SamplingFile samplingFile = param.samplingFile;
            SamplingFile remoteFile = param.remoteFile;
            String samplingId = param.samplingId;

            //下载并保存文件
            String localPath = "";

            String cacheDir = Constant.FILE_DIR + Constant.PNG_DIR;
            File newFile = new File(cacheDir + "/" + remoteFile.getFileName());
            if (!newFile.exists()) {
                //下载并保存文件
                localPath = downloadFile(remoteFile.getFilePath(), remoteFile.getFileName());
            } else {
                localPath = newFile.getPath();
            }

            //是否成功
            if (TextUtils.isEmpty(localPath)) {
                return null;
            }

            if (samplingFile == null) {
                //创建文件
                samplingFile = new SamplingFile();
                samplingFile.setLocalId(UUID.randomUUID().toString());
            }

            samplingFile.setSamplingId(samplingId);
            samplingFile.setFilePath(localPath);
            samplingFile.setId(remoteFile.getId());
            samplingFile.setFileName(remoteFile.getFileName());
            samplingFile.setUpdateTime(remoteFile.getUpdateTime());
            samplingFile.setIsUploaded(true);

            DBHelper.get().getSamplingFileDao().insertOrReplace(samplingFile);

            return null;
        }

        //onPostExecute用于UI的更新.此方法的参数为doInBackground方法返回的值.
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    class DownLoadNoiseAsyncTask extends AsyncTask<NoisePrivateData, Sampling, Sampling> {
        Sampling sampling;

        DownLoadNoiseAsyncTask(Sampling sampling) {
            this.sampling = sampling;
        }

        @Override
        protected Sampling doInBackground(NoisePrivateData... privateDatas) {
            String path = downloadNoiseFile(privateDatas[0].getImageSYT());
            if (path != null && !path.equals("")) {
                privateDatas[0].setImageSYT(path);
                sampling.setPrivateData(new Gson().toJson(privateDatas[0]));
            }
            sampling.setIsCanEdit(SamplingUtil.setSampIsCanEdit(sampling));
            sampling.setIsLocal(false);
            sampling.setIsFinish(SamplingUtil.isSamplingFinsh(sampling));
            if (DbHelpUtils.getDbSampling(sampling.getId()) != null) {
                DBHelper.get().getSamplingDao().update(sampling);
            } else {
                DBHelper.get().getSamplingDao().insertOrReplace(sampling);
            }
            return sampling;
        }


    }

    /**
     * 下载噪声测点示意图图片文件
     *
     * @param downloadUrl 下载路径
     * @return 保存路径
     */
    private String downloadNoiseFile(String downloadUrl) {
        String fileName = null;
        String fileUrl = UserInfoHelper.get().getUserInfo().getWebUrl() + downloadUrl;
        try {
            int lastIndex = downloadUrl.lastIndexOf("/");
            if (lastIndex >= 0) {
                String fn = downloadUrl.substring(lastIndex + 1);
                fileName = fn;
                //文件名手动编码
                downloadUrl = downloadUrl.replace(fn, java.net.URLEncoder.encode(fn, "utf-8"));
            }
            String cacheDir = Constant.FILE_DIR + Constant.PNG_DIR + "/noise/";
            File newFile = new File(cacheDir + fileName);
            //如果fileName是空的话  就不用下载了 直接返回空字符串
            if (fileName == null || fileName.equals("")) {
                Log.e("downloadNoiseFile", "Url:" + downloadUrl);
                return "";
            } else {
                //判断本地是否存在这种图片
                if (newFile.exists()) {
                    Log.d(TAG, "downloadNoiseFile:" + String.format("路径:%s 文件名：%s", cacheDir, fileName));
                    return cacheDir + fileName;
                } else {
                    FileUtils.makeDir(cacheDir);
                }
            }
            //本地不存在并且fileName不为空 去下载

            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            con.setReadTimeout(30000);
            con.setConnectTimeout(15000);
            InputStream is = null;
            if (((HttpURLConnection) con).getResponseCode() == 200) {
                is = con.getInputStream();//获取输入流
            }
            if (is == null) {
                return null;
            }

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);//指定文件保存路径，代码看下一步

            byte[] buf = new byte[1024 * 10];
            int ch;
            while ((ch = is.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
            }

            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            is.close();

            Log.d("downloadNoiseFile", "donwload " + fileUrl + " to " + newFile.getPath() + " success!");

            return newFile.getPath();
        } catch (Exception e) {
            Log.e("downloadNoiseFile", "Url:" + fileUrl + " Exc:" + e);
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取流转任务列表
     *
     * @param map
     * @param msg
     * @param isRefresh true 刷新 false  加载
     */
    public void getSampleStorageList(Map<String, String> map, Message msg, boolean isRefresh) {

        mModel.getSampleStorageList(map).compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<WanderSampleStorage>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<WanderSampleStorage>> response) {
//                        if (response.getCode() == 200) {
//                            msg.what = Message.RESULT_OK;
//                            msg.obj = response.getData();
//                        } else {
//                            msg.what = Message.RESULT_FAILURE;
//                            msg.obj = response.getMessage();
//                        }
//                        msg.what = Message.RESULT_OK;
                        if (isRefresh) {
                            msg.what = Message.RESULT_OK;
                        } else {
                            msg.what = 1001;
                        }
                        msg.obj = response.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int code) {
                        msg.what = Message.RESULT_FAILURE;
                        msg.obj = message;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 获取流转任务列表
     *
     * @param map             流转单状态（0待流转，1已流转，10自送样，20待流转已流转一起查）
     * @param msg
     * @param status@true下拉刷新 @false 上拉加载
     */
    public void getSampleStorageProject(Map<String, String> map, Message msg, boolean status) {
        mModel.getSampleStorageProject(map).compose(RxUtils.applySchedulers(this,
                msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<ProjectSampleStorage>>() {
                    @Override
                    public void onSuccess(BaseResponse<ProjectSampleStorage> response) {
                        if (status) {
                            msg.what = Message.RESULT_OK;
                            msg.obj = response.getData();
                        } else {
                            msg.what = 1001;
                            msg.obj = response.getData();
                        }
//                        if (response.getCode() == 200) {
//
//                            if (status) {
//                                msg.what = Message.RESULT_OK;
//                                msg.obj = response.getData().getData();
//                            } else {
//                                msg.what = 1001;
//                                msg.obj = response.getData().getData();
//                            }
//                        } else {
//                            msg.what = Message.RESULT_FAILURE;
//                            msg.obj = response.getMessage();
//                        }
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int code) {
                        msg.what = Message.RESULT_FAILURE;
                        msg.str = message;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param gpsList
     */
    public void postGaoDeCoordInates(List<Gps> gpsList, TraceService.TraceUploadListener listener) {
        mModel.postGaoDeCoordInates(gpsList)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        if (listener != null) {
                            listener.success(baseResponse);
                        }
                    }

                    @Override
                    public void onFailure(int Type, String message, int code) {
                        if (listener != null) {
                            listener.onFailure(message, code);
                        }
                    }
                }));
    }
}
