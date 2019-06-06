package cn.cdjzxy.android.monitoringassistant.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.gps.Gps;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.gps.GpsBean;

import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.launch.LoginActivity;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.NotificationUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDeviceTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxNetTool;


public class TraceService extends Service {
    private static final String TAG = "TraceService+嘉泽轨迹";


    private LocationClient mLocationClient;
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    private static final int SERVER_ID = 1001;
    private BDAbstractLocationListener listener;
    private static final int Scan_Span = 60;//定位时间间隔60秒
    private Object objLock = new Object();
    private ApiPresenter apiPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate:");
        initNotification();
        initListener();
        init();

    }


    private void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder2 = new Notification.Builder(getApplicationContext(),
                    RxDeviceTool.getAppPackageName(this))
                    .setContentTitle("嘉泽小助手")
                    .setContentText("正在后台同步数据")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            notification = builder2.build();
            startForeground(SERVER_ID, notification);
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(this);
            Intent nfIntent = new Intent(this, LoginActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("嘉泽小助手") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            notification = builder.build(); // 获取构建好的Notification

        }
    }

    private void init() {
        apiPresenter = new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(CoordType.GCJ02.name());//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(Scan_Span * 1000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationDescribe(false);//可选，设置是否需要地址描述
        option.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        option.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(listener);
        mLocationClient.enableLocInForeground(SERVER_ID, notification);
        start();
    }

    private void initListener() {
        listener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
//                if (null != bdLocation
//                        && bdLocation.getLocType() != BDLocation.TypeServerError//server定位失败，没有对应的位置信息
//                        && bdLocation.getLocType() != BDLocation.TypeOffLineLocationFail//离线定位失败结
//                        && bdLocation.getLocType() != BDLocation.TypeNone
//                        && bdLocation.getLocType()
//                        ) {
                if (null != null
                        && (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation//网络定位成功
                        || bdLocation.getLocType() == BDLocation.TypeOffLineLocation//离线定位成功
                        || bdLocation.getLocType() == BDLocation.TypeOffLineLocationNetworkFail//离线定位成功（已取消）
                )) {
                    try {
                        GpsBean gpsBean = new GpsBean();
                        Log.i(TAG, "定位成功: " + bdLocation.toString());
                        gpsBean.setCollectionTime(RxDateTool.getCurTimeString());//sdk返回的时间有重复
                        gpsBean.setLatitude(bdLocation.getLatitude());
                        gpsBean.setLongitude(bdLocation.getLongitude());
                        gpsBean.setCoordType("gcj02");
                        gpsBean.setLocType(bdLocation.getLocType() + "");
                        gpsBean.setId(UUID.randomUUID().toString());
                        uploadTrace(gpsBean);

                    } catch (Exception e) {
                        Log.e(TAG, "onReceiveLocation: " + e.toString());
                    }
                }
            }

            @Override
            public void onLocDiagnosticMessage(int i, int i1, String s) {
                Log.e(TAG, "onLocDiagnosticMessage: " + String.format("定位异常：定位类型：%d, 诊断类型：%d,问题解释：%s", i, i1, s));
            }
        };
    }

    /**
     * 上传轨迹服务
     *
     * @param gpsBean
     */
    private void uploadTrace(GpsBean gpsBean) {
        if (DBHelper.get() == null || DBHelper.get().getGpsBeanDao() == null) {
            DBHelper.init(this, UserInfoHelper.get().getUserName());//初始化创建数据库
        }
        DBHelper.get().getGpsBeanDao().insert(gpsBean);
        List<GpsBean> list = DBHelper.get().getGpsBeanDao().loadAll();
        if (RxNetTool.isNetworkAvailable(getApplicationContext())) {//有网络  上传
            if (apiPresenter != null) {

                List<Gps> gpsList = new ArrayList<>();
                for (GpsBean bean : list) {
                    Gps gps = new Gps(bean.getLatitude(), bean.getLongitude(), bean.getCollectionTime());
                    gpsList.add(gps);
                }
                if (RxDataTool.isEmpty(gpsList)) return;
                apiPresenter.postGaoDeCoordInates(gpsList, new TraceService.TraceUploadListener() {
                    @Override
                    public void success(BaseResponse response) {
                        DBHelper.get().getGpsBeanDao().deleteAll();
                    }

                    @Override
                    public void onFailure(String message, int code) {
                        Log.e(TAG, String.format("onFailure:   message:%s   code:%d", message, code));
                    }
                });
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        mLocationClient.unRegisterLocationListener(listener);
        mLocationClient.disableAssistantLocation();
        stop();
        mLocationClient = null;
    }

    public interface TraceUploadListener {
        void success(BaseResponse response);

        void onFailure(String message, int code);
    }

    public void start() {
        synchronized (objLock) {
            if (mLocationClient != null && !mLocationClient.isStarted()) {
                mLocationClient.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        }
    }

    public boolean isStart() {
        return mLocationClient.isStarted();
    }

    public boolean requestHotSpotState() {
        return mLocationClient.requestHotSpotState();
    }
}
