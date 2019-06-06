package cn.cdjzxy.android.monitoringassistant.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.launch.LoginActivity;
import cn.cdjzxy.android.monitoringassistant.utils.NotificationUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDeviceTool;

/**
 * 用于首页同步任务
 */
public class SyncIntentServer extends IntentService implements IView {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SyncIntentServer(String name) {
        super(name);
    }

    private ApiPresenter apiPresenter;
    private Notification notification;
    private static final int SERVER_ID = 1002;
    private NotificationUtils notificationUtils;

    public SyncIntentServer(String name, ApiPresenter apiPresenter) {
        super(name);
        this.apiPresenter = apiPresenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || apiPresenter == null) return;
        apiPresenter.getDevices(Message.obtain(this, new Object()));//获取设备信息 GET
        apiPresenter.getMethods(Message.obtain(this, new Object()));//获取方法信息 GET
        apiPresenter.getMonItems(Message.obtain(this, new Object()));//获取监测项目 GET
        apiPresenter.getTags(Message.obtain(this, new Object()));//获取要素分类 GET
        apiPresenter.getMonItemTagRelation(Message.obtain(this, new Object()));// 获取项目要素关系 GET
        apiPresenter.getMethodTagRelation(Message.obtain(this, new Object()));//获取方法要素关系 GET
        apiPresenter.getMonItemMethodRelation(Message.obtain(this, new Object()));//获取项目方法关系 GET
        apiPresenter.getMethodDevRelation(Message.obtain(this, new Object()));//获取方法设备关系 GET
        apiPresenter.getRight(Message.obtain(this, new Object()));//获取权限 GET
        apiPresenter.getEnvirPoint(Message.obtain(this, new Object()));//获取环境质量点位 GET
        apiPresenter.getEnterRelatePoint(Message.obtain(this, new Object()));//获取企业点位 GET
        apiPresenter.getEnterprise(Message.obtain(this, new Object()));//获取企业 GET
        apiPresenter.getDic(Message.obtain(this, new Object()), 7);//获取字典  GET
        apiPresenter.getWeather(Message.obtain(this, new Object()));//获取天气  GET
        apiPresenter.getUser(Message.obtain(this, new Object()));//获取采样用户  GET
        apiPresenter.getUnit(Message.obtain(this, new Object()));//获取结果单位  GET
        apiPresenter.getMsgs(Message.obtain(this, new Object()));//获取全部消息
        apiPresenter.getFormSelect(Message.obtain(this, new Object()));//获取表单分类 GET
        apiPresenter.getSamplingStantd(Message.obtain(this, new Object()));//获取采样规范 GET
        apiPresenter.getMyTasks(Message.obtain(this, new Object()));//获取跟我相关待采样任务 GET
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    private void initNotification() {
        NotificationUtils.ChannelBuilder channelBuilder = new NotificationUtils.ChannelBuilder(
                "1号频道组", "1号频道", "一号频道名字", NotificationManager.IMPORTANCE_DEFAULT)
                .setChannelName("一号频道名字").setByPassDnd(true).setLightColor(Color.GREEN)
                .setShowBadge(false).setEnableLight(true).setEnableSound(true).setEnableVibrate(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationUtils = new NotificationUtils(this, channelBuilder);
        //    NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(this);
            Intent nfIntent = new Intent(this, LoginActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("嘉泽小助手") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台同步数据") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            notification = builder.build(); // 获取构建好的Notification
        }
    }
}
