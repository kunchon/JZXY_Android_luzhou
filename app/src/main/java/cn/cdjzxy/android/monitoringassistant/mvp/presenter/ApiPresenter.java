package cn.cdjzxy.android.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;


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

import cn.cdjzxy.android.monitoringassistant.mvp.ui.MainActivity;
import cn.cdjzxy.android.monitoringassistant.base.di.component.AppComponent;
import cn.cdjzxy.android.monitoringassistant.base.mvp.BasePresenter;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.ApiRepository;
import cn.cdjzxy.android.monitoringassistant.mvp.model.api.Api;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.UploadFileResponse;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Dic;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Enterprise;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.gps.Gps;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormFlow;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.android.monitoringassistant.services.TraceService;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxFileTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxNetTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxObserver;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxUtils;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import timber.log.Timber;

import static cn.cdjzxy.android.monitoringassistant.mvp.ui.MainActivity.TYPE_TASK;


/**
 * App相关的数据操作Presenter
 */
public class ApiPresenter extends BasePresenter<ApiRepository> {


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
        if (RxNetTool.isNetworkAvailable(context)) {
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
                        HawkUtil.putBoolean(HawkUtil.IS_UPDATE, false);

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
        if (RxDataTool.isNull(userInfo) || RxDataTool.isEmpty(userInfo.getWorkNo())) {
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
     * 获取设备信息 GET
     *
     * @param msg
     */
    public void getDevices(final Message msg) {
        mModel.getDevices()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Devices>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Devices>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getDevicesDao().deleteAll();
                            DBHelper.get().getDevicesDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
                        if (!RxDataTool.isNull(baseResponse) && !RxDataTool.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodsDao().deleteAll();
                            DBHelper.get().getMethodsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取监测项目
     *
     * @param msg
     */
    public void getMonItems(final Message msg) {
        mModel.getMonItems()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItems>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItems>> baseResponse) {
                        if (
                                !RxDataTool.isNull(baseResponse) &&
                                        !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMonItemsDao().deleteAll();
                            DBHelper.get().getMonItemsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取要素分类
     *
     * @param msg
     */
    public void getTags(final Message msg) {
        mModel.getTags()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Tags>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Tags>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getTagsDao().deleteAll();
                            DBHelper.get().getTagsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取项目要素关系
     *
     * @param msg
     */
    public void getMonItemTagRelation(final Message msg) {
        mModel.getMonItemTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemTagRelation>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMonItemTagRelationDao().deleteAll();
                            DBHelper.get().getMonItemTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * /获取方法要素关系 GE
     *
     * @param msg
     */
    public void getMethodTagRelation(final Message msg) {
        mModel.getMethodTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodTagRelation>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMethodTagRelationDao().deleteAll();
                            DBHelper.get().getMethodTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取项目方法关系 GET
     *
     * @param msg
     */
    public void getMonItemMethodRelation(final Message msg) {
        mModel.getMonItemMethodRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemMethodRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemMethodRelation>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMonItemMethodRelationDao().deleteAll();
                            DBHelper.get().getMonItemMethodRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取方法设备关系
     *
     * @param msg
     */
    public void getMethodDevRelation(final Message msg) {
        mModel.getMethodDevRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodDevRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodDevRelation>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMethodDevRelationDao().deleteAll();
                            DBHelper.get().getMethodDevRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getRightsDao().deleteAll();
                            DBHelper.get().getRightsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取环境质量点位
     *
     * @param msg
     */
    public void getEnvirPoint(final Message msg) {
        mModel.getEnvirPoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnvirPoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnvirPoint>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getEnvirPointDao().deleteAll();
                            DBHelper.get().getEnvirPointDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取企业点位
     *
     * @param msg
     */
    public void getEnterRelatePoint(final Message msg) {
        mModel.getEnterRelatePoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnterRelatePoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnterRelatePoint>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getEnterRelatePointDao().deleteAll();
                            DBHelper.get().getEnterRelatePointDao().insertInTx(baseResponse.getData());

                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取企业
     *
     * @param msg
     */
    public void getEnterprise(final Message msg) {
        mModel.getEnterprise()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Enterprise>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Enterprise>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getEnterpriseDao().deleteAll();
                            DBHelper.get().getEnterpriseDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取字典
     *
     * @param msg
     */
    public void getDic(final Message msg, int type) {
        mModel.getDic(type)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Dic>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Dic>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getDicDao().deleteAll();
                            DBHelper.get().getDicDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取天气
     *
     * @param msg
     */
    public void getWeather(final Message msg) {
        mModel.getWeather()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<String>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<String>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            Weather weather = new Weather();
                            weather.setWeathers(baseResponse.getData());
                            DBHelper.get().getWeatherDao().deleteAll();
                            DBHelper.get().getWeatherDao().insert(weather);

                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取采样用户
     *
     * @param msg
     */
    public void getUser(final Message msg) {
        mModel.getUser()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<User>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<User>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getUserDao().deleteAll();
                            DBHelper.get().getUserDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取结果单位
     *
     * @param msg
     */
    public void getUnit(final Message msg) {
        mModel.getUnit()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Unit>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Unit>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse)
                                && !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getUnitDao().deleteAll();
                            DBHelper.get().getUnitDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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
     * 获取所有消息
     */
    public void getMsgs(final Message msg) {
        mModel.getMsgs()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Msg>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Msg>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse)
                                && !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getMsgDao().deleteAll();
                            DBHelper.get().getMsgDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
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

    /**
     * 批量阅读消息
     *
     * @return
     */
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
     * 获取所有任务 GET
     *
     * @param msg
     */
    public void getAllTasks(final Message msg) {
        mModel.getAllTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Project>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Project>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
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
                        if (!RxDataTool.isNull(baseResponse)) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!RxDataTool.isEmpty(samplings)) {
                                SamplingUtil.saveSample(samplings);
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
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            DBHelper.get().getSamplingStantdDao().deleteAll();
                            DBHelper.get().getSamplingStantdDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;

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
     * 获取表单分类 GET
     *
     * @param msg
     */
    public void getFormSelect(final Message msg) {
        mModel.getFormSelect()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Form>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Form>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            List<Form> forms = baseResponse.getData();
                            DBHelper.get().getFormFlowDao().deleteAll();
                            DBHelper.get().getFormSelectDao().deleteAll();
                            DBHelper.get().getFormDao().deleteAll();
                            for (Form form : forms) {
                                List<FormSelect> formSelects = form.getFormSelectList();
                                if (!RxDataTool.isEmpty(formSelects)) {
                                    for (FormSelect formSelect : formSelects) {
                                        List<FormFlow> formFlows = formSelect.getFormFlows();
                                        if (!RxDataTool.isEmpty(formFlows)) {
                                            DBHelper.get().getFormFlowDao().insertInTx(formFlows);
                                        }
                                    }
                                    DBHelper.get().getFormSelectDao().insertInTx(formSelects);
                                }
                            }
                            DBHelper.get().getFormDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;

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
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            List<Project> projects = baseResponse.getData();//所有与我相关的任务
                            for (Project project : projects) {
                                getSampling(Message.obtain(msg), project.getId());
                                if (RxDataTool.isEmpty(DbHelpUtils.getDbProject(project.getId()))) {
                                    //数据库没有就直接插入
                                    DBHelper.get().getProjectDao().insert(project);
                                } else {
                                    //有得话就直接更新
                                    DBHelper.get().getProjectDao().update(project);
                                }
                            }
                            msg.obj = projects.size();
                        }
                        msg.str = "正在同步采样单";
                        msg.what = Message.RESULT_OK;
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
     * 获取任务列表下的所有采样单信息(支持批量)
     *
     * @param msg
     */
    public void getSamplings(final Message msg, List<String> projectIds) {
        if (RxDataTool.isEmpty(projectIds)) return;
        mModel.getSamplings(projectIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) && !RxDataTool.isEmpty(baseResponse.getData())) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!RxDataTool.isEmpty(samplings)) {
                                //DBHelper.get().getSamplingFormStandDao().deleteAll();
                                SamplingUtil.saveSample(samplings);
                            }
                        }

                        Message message = Message.obtain(msg);
                        message.what = Message.RESULT_OK;
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
     * 获取所有采样单信息
     *
     * @param msg
     */
    public void getSampling(final Message msg, String projectId) {
        mModel.getSampling(projectId)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        if (!RxDataTool.isNull(baseResponse) &&
                                !RxDataTool.isEmpty(baseResponse.getData())
                                ) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!RxDataTool.isEmpty(samplings)) {
                                SamplingUtil.saveSample(samplings);
                            }
                        }
                        Message message = Message.obtain(msg);
                        message.what = TYPE_TASK;
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
     * /**
     * 采样任务完结操作
     *
     * @param msg
     * @param projectId
     * @param comment
     */
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

    /**
     * 修改任务方案
     *
     * @param msg
     * @param projectPlan
     */
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

    /**
     * 创建采样单
     *
     * @param msg
     * @param preciptationSampForm
     */
    public void createTable(final Message msg, PreciptationSampForm preciptationSampForm) {
        mModel.createTable(preciptationSampForm)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = SamplingUtil.NET_RESPONSE_CODE_259;
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
                        if (responseCode == SamplingUtil.NET_RESPONSE_SAMPLING_DIFFER) {
                            msg.what = SamplingUtil.NET_RESPONSE_SAMPLING_DIFFER;
                        } else {
                            msg.obj = message;
                            msg.what = Message.RESULT_FAILURE;
                        }

                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 更新采样单
     *
     * @param msg
     * @param preciptationSampForm
     */
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

    /**
     * 删除采样单
     *
     * @param msg
     * @param tableId
     */
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

    /**
     * 上传文件
     *
     * @param msg
     * @param parts
     * @param params
     */
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
        if (RxDataTool.isNull((userInfo))) {
            userInfo = UserInfoHelper.get().getUserInfo();
        }

        //动态设置weburl
        if (!RxDataTool.isEmpty(userInfo.getWebUrl())) {
            RetrofitUrlManager.getInstance().putDomain(Api.LOGIN_RESP_WEBURL, userInfo.getWebUrl());
        }
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
     * 上传gps轨迹
     *
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
