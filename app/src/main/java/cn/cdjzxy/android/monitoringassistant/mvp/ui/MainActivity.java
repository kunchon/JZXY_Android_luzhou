package cn.cdjzxy.android.monitoringassistant.mvp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.mapapi.NetworkUtil;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyBaseViewPagerActivity;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.MainTabAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.easy.EasyPusherActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.msg.MsgActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.scan.ScanCodeActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.setting.PwdModifyFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.setting.SettingFragment;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.fragment.TaskFragment;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;
import cn.cdjzxy.android.monitoringassistant.widget.ProgressBar.NumberProgressBar;
import cn.cdjzxy.android.monitoringassistant.widget.badgeview.BadgeView;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlus;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlusBuilder;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.ViewHolder;


/**
 * 程序主界面
 */
public class MainActivity extends MyTitleActivity {
    public static final int TYPE_TASK = 2222;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;

    private FragmentAdapter mFragmentAdapter;
    private MainTabAdapter mMainTabAdapter;

    private TitleBarView mTitleBarView;
    private BadgeView mBadgeView;
    private TextView mTvHintSam;//采样单提示
    private TextView mTvHint;
    private DialogPlus mDialogPlus;
    private NumberProgressBar mNumberProgressBar;
    private NumberProgressBar mNumberProgressBarSam;//采样单进度
    private int SYNC_SIZE;//进入首页同步数据的总数
    private int SYNC_SAMPLE_SIZE;//进入首页同步数据的总数
    private int SYNC_PROGRESS = 0;//任务进度
    private int SYNC_SAMPLE = 0;//采样单进度
    private boolean isEasy = false;//是否进入直播界面

    //标题栏  更换时更换这里即可
    private final int INT_PROJECT = 0;//任务或者采样
    private final int INT_SCAN = INT_PROJECT + 1;//扫描
    //    private final int INT_POINT_MAP = INT_PROJECT + 2;//点位地图
    private final int INT_EASY_PUSH = INT_PROJECT + 2;//直播
    private final int INT_SET = INT_PROJECT + 3;//设置
    //fragment 更换时更换这里即可
    private final int INT_FRAGMENT_PROJECT = 0;//任务或者采样
    public final int INT_FRAGMENT_SET = INT_FRAGMENT_PROJECT + 1;//设置
    public final int INT_FRAGMENT_PWD = INT_FRAGMENT_PROJECT + 2;
    private List<Tab> mTabs;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        updateDataFromNetwork();
        // setTabViewLayoutWidth(200);
        initTabData();
        initFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Msg> msgList = DbHelpUtils.getMsgListForState(0);
        if (!RxDataTool.isEmpty(msgList)) {
            mBadgeView.setVisibility(View.VISIBLE);
        } else {
            mBadgeView.setVisibility(View.GONE);
        }
        if (isEasy) {
            updateTabStatus(0);
            openFragment(0);
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.mTitleBarView = titleBar;
        mTitleBarView.setLeftVisible(false);
        mTitleBarView.setTitleMainText("首页");
        mTitleBarView.addLeftAction(titleBar.new ViewAction(getTitleLeftView()));
        mTitleBarView.addRightAction(titleBar.new ViewAction(getTitleRightView()
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(MsgActivity.class);
            }
        }));
    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getTitleRightView() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.view_main_title_right, null);
        mBadgeView = view.findViewById(R.id.red_hot);
        return view;
    }

    /**
     * 获取标题左边View
     *
     * @return
     */
    private View getTitleLeftView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_main_title_left, null);
        TextView mTvUserName = view.findViewById(R.id.tv_username);
        TextView mTvDate = view.findViewById(R.id.tv_date);
        String userName = UserInfoHelper.get().getUserName();
        mTvUserName.setText(RxDataTool.isEmpty(userName) ? "测试数据" : userName + ", 欢迎你");
        mTvDate.setText(RxDateTool.getDate() + " " + RxDateTool.getWeek());
        return view;
    }


    @Override
    public void handleMessage(@NonNull Message message) {
        if (RxDataTool.isEmpty(message)) return;
        switch (message.what) {
            case Message.RESULT_FAILURE:
                if (mDialogPlus != null) {
                    mDialogPlus.dismiss();
                }
                break;
            case Message.RESULT_OK:
                try {
                    updateProgress(message.str);
                    List<String> stringList = new ArrayList<>();
                    if (!RxDataTool.isEmpty(message.obj)) {
                        stringList = (List<String>) message.obj;
                        SYNC_SAMPLE_SIZE = stringList.size();
                        SYNC_SAMPLE = 0;
                        updateProgressSam();
                        mPresenter.getSampling(Message.obtain(this, new Object()), stringList.get(0));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "handleMessage: " + e.toString());
                }

                break;
            case TYPE_TASK:
                SYNC_SAMPLE++;
                updateProgressSam();
                //mPresenter.getSampling(Message.obtain(this, new Object()), (List<String>) message.obj);//获取所有采样单信息(支持批量)
                break;
        }
    }

    /**
     * 更新进度
     */
    private void updateProgressSam() {
        if (!mDialogPlus.isShowing()) {
            mDialogPlus.show();
        }

        mTvHintSam.setVisibility(View.VISIBLE);
        mNumberProgressBarSam.setVisibility(View.VISIBLE);
        mTvHintSam.setText("正在同步采样单");
        mNumberProgressBarSam.setMax(SYNC_SAMPLE_SIZE);

        if (SYNC_SAMPLE > SYNC_SAMPLE_SIZE) {
            mNumberProgressBarSam.setProgress(SYNC_SAMPLE_SIZE);
            mTvHintSam.setText("采样单同步完成");
        } else {
            mNumberProgressBarSam.setProgress(SYNC_SAMPLE);
        }
    }

    /**
     * 更新进度
     */
    private void updateProgress(String str) {
        mNumberProgressBar.setMax(SYNC_SIZE);
        if (!mDialogPlus.isShowing()) {
            return;
        }
        SYNC_PROGRESS++;
        mTvHint.setText(str);
        if (SYNC_PROGRESS >= SYNC_SIZE) {
            mNumberProgressBar.setProgress(SYNC_SIZE);
        } else {
            mNumberProgressBar.setProgress(SYNC_PROGRESS);
        }
        if (SYNC_PROGRESS >= SYNC_SIZE) {
            mDialogPlus.dismiss();
        }
    }

    /**
     * 初始化更新数据的进度条
     */
    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_download, null);
        mNumberProgressBar = view.findViewById(R.id.progressbar);
        mNumberProgressBarSam = view.findViewById(R.id.progressbar_1);
        mTvHint = view.findViewById(R.id.tv_hint);
        mTvHintSam = view.findViewById(R.id.tv_hint_1);
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
        dialogPlusBuilder.setContentHolder(new ViewHolder(view));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setCancelable(false);
        dialogPlusBuilder.setContentWidth(700);
        mDialogPlus = dialogPlusBuilder.create();
        mDialogPlus.show();
    }

    /**
     * 从服务端更新数据
     */
    private void updateDataFromNetwork() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            SYNC_SIZE = 0;
            showDialog();
            mPresenter.getDevices(Message.obtain(this, new Object()));//获取设备信息 GET
            mPresenter.getMethods(Message.obtain(this, new Object()));//获取方法信息 GET
            mPresenter.getMonItems(Message.obtain(this, new Object()));//获取监测项目 GET
            mPresenter.getTags(Message.obtain(this, new Object()));//获取要素分类 GET
            mPresenter.getMonItemTagRelation(Message.obtain(this, new Object()));// 获取项目要素关系 GET
            mPresenter.getMethodTagRelation(Message.obtain(this, new Object()));//获取方法要素关系 GET
            mPresenter.getMonItemMethodRelation(Message.obtain(this, new Object()));//获取项目方法关系 GET
            mPresenter.getMethodDevRelation(Message.obtain(this, new Object()));//获取方法设备关系 GET
            mPresenter.getRight(Message.obtain(this, new Object()));//获取权限 GET
            mPresenter.getEnvirPoint(Message.obtain(this, new Object()));//获取环境质量点位 GET
            mPresenter.getEnterRelatePoint(Message.obtain(this, new Object()));//获取企业点位 GET
            mPresenter.getEnterprise(Message.obtain(this, new Object()));//获取企业 GET
            mPresenter.getDic(Message.obtain(this, new Object()), 7);//获取字典  GET
            mPresenter.getWeather(Message.obtain(this, new Object()));//获取天气  GET
            mPresenter.getUser(Message.obtain(this, new Object()));//获取采样用户  GET
            mPresenter.getUnit(Message.obtain(this, new Object()));//获取结果单位  GET
            mPresenter.getMsgs(Message.obtain(this, new Object()));//获取全部消息
            mPresenter.getFormSelect(Message.obtain(this, new Object()));//获取表单分类 GET
            mPresenter.getSamplingStantd(Message.obtain(this, new Object()));//获取采样规范 GET
            mPresenter.getMyTasks(Message.obtain(this, new Object()));//获取跟我相关待采样任务 GET
            //HawkUtil.putBoolean("isUpdated", true);
            HawkUtil.putBoolean("isUpdated", false);
            SYNC_SIZE = 20;
        }
    }


    protected void initFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new TaskFragment());//任务
//        list.add(new ScanCodeFragment());//扫描
        //list.add(new RepositoryFragment());知识库没有了
        list.add(new SettingFragment());//设置
        list.add(new PwdModifyFragment());//修改密码

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);

    }


    protected void tabViewOnTabSelect(int position) {
        switch (position) {
            case INT_PROJECT://任务
                openFragment(INT_FRAGMENT_PROJECT);
                break;
            case INT_SCAN://扫描
                startActivity(new Intent(this, ScanCodeActivity.class));
                break;
//            case INT_POINT_MAP://点位地图
//                startWebActivity("https://www.amap.com/", "点位地图");
//                break;
            case INT_EASY_PUSH:
                startActivity(new Intent(this, EasyPusherActivity.class));
                break;
            case INT_SET:
                openFragment(INT_FRAGMENT_SET);
                break;
        }
    }


    protected void initTabData() {
        mTabs = new ArrayList<>();
        for (int i = 0; i <= INT_SET; i++) {
            Tab tab = new Tab();
            if (i == INT_PROJECT) {
                tab.setTabName("任务");
                tab.setResId(R.mipmap.icon_task_nor);
                tab.setSelectedResId(R.mipmap.icon_task_hov);
                tab.setSelected(true);
            } else if (i == INT_SCAN) {
                tab.setTabName("扫码");
                tab.setResId(R.mipmap.icon_scan_nor);
                tab.setSelectedResId(R.mipmap.icon_scan_hov);
                tab.setSelected(false);
            }
           /* else if (i == INT_POINT_MAP) {
                tab.setTabName("点位地图");
                tab.setResId(R.mipmap.icon_point_nor);
                tab.setSelectedResId(R.mipmap.icon_point_hov);
                tab.setSelected(false);
            } */
            else if (i == INT_EASY_PUSH) {
                tab.setTabName("视频直播");
                tab.setResId(R.mipmap.icon_live_nor);
                tab.setSelectedResId(R.mipmap.icon_live_hov);
                tab.setSelected(false);
            } else if (i == INT_SET) {
                tab.setTabName("设置");
                tab.setResId(R.mipmap.icon_setting_nor);
                tab.setSelectedResId(R.mipmap.icon_setting_hov);
                tab.setSelected(false);
            }
            mTabs.add(tab);
        }

        ArtUtils.configRecyclerView(recyclerView,
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                    @Override
                    public boolean canScrollVertically() {//设置RecyclerView不可滑动
                        return false;
                    }
                });
        mMainTabAdapter = new MainTabAdapter(mTabs, mContext);
        mMainTabAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateTitle(position);
                tabViewOnTabSelect(position);
                openFragment(position);
            }
        });
        recyclerView.setAdapter(mMainTabAdapter);

    }

    /**
     * 更新标题
     *
     * @param position
     */
    private void updateTitle(int position) {
        if (position == 0) {
            mTitleBarView.setTitleMainText("嘉泽云监测");
        } else {
            mTitleBarView.setTitleMainText(mTabs.get(position).getTabName());
        }
    }

    /**
     * EventBus 通知
     *
     * @param position
     */
    @Subscriber(tag = EventBusTags.TAG_MODIFY_PWD)
    private void updateSettingFragment(int position) {
        openFragment(position);
    }


    /**
     * 更新tabView选中状态
     *
     * @param position
     */
    private void updateTabStatus(int position) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (i == position) {
                mTabs.get(i).setSelected(true);
            } else {
                mTabs.get(i).setSelected(false);
            }
        }
        mMainTabAdapter.notifyDataSetChanged();
    }

    /**
     * 切换Fragment页面
     *
     * @param position 那个页面
     */
    public void openFragment(int position) {
        hideSoftInput();
        viewPager.setCurrentItem(position);
    }
}
