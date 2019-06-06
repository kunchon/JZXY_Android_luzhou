package cn.cdjzxy.android.monitoringassistant.mvp.ui.task;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.mapapi.NetworkUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyRefreshActivity;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.scan.ScanCodeActivity;
import cn.cdjzxy.android.monitoringassistant.services.TraceService;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxLocationTool;
import me.jessyan.autosize.utils.Preconditions;


/**
 * 任务列表
 */

public class TaskActivity extends MyRefreshActivity {

    public static final int TASK_REQUEST_CODE = 10;
    public static final int TASK_RESULT_CODE = 11;


    @BindView(R.id.check_box)
    CheckBox checkBox;

    private TaskAdapter mTaskAdapter;
    private List<Project> list;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_task;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样任务");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(ScanCodeActivity.class);
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTraceCheckBox();
    }

    /**
     * 初始化导航控件
     */
    private void initTraceCheckBox() {
        //权限检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }

        checkBox.setChecked(UserInfoHelper.get().getUserTraceOpen());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    showDialog("开启轨迹提示", "轨迹开启后，“嘉泽云小助手”将在后台记录的轨迹信息。", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ((RxLocationTool.hasGPSDevice(TaskActivity.this) ||       //设备支持gps
                                    RxLocationTool.isGpsEnabled(TaskActivity.this)) ||    //设备已经开启gps
                                    NetworkUtil.isNetworkAvailable(TaskActivity.this)    //设备有网络
                                    ) {
                                startTraceService();
                                //startUploadTrace(true);
                                UserInfoHelper.get().saveUserTraceOpen(true);
                            } else {
                                showDialog("轨迹开启失败", "请保持设备有网或者设备支持GPS定位，才能开启轨迹", null);
                                checkBox.setChecked(false);
                                UserInfoHelper.get().saveUserTraceOpen(false);
                            }
                        }
                    });
                } else {
                    stopTraceService();
                    UserInfoHelper.get().saveUserTraceOpen(false);
                }
            }
        });
    }


    /**
     * 关闭轨迹
     */
    private void stopTraceService() {
        //开启轨迹服务
        Intent intent = new Intent();
        intent.setClass(this, TraceService.class);
        stopService(intent);
        ArtUtils.makeText(this, "轨迹导航已关闭");
    }

    /**
     * 获取数据
     *
     * @param isFirst 是否第一次请求：用于拿到本地数据
     */
    @Override
    protected void getData(boolean isFirst) {
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            //请求网络数据
            getMyNetData();
        } else if (isFirst) {
            initTaskData();
        }
    }

    /**
     * 请求网络数据
     */
    private void getMyNetData() {
        //暂时展示本地数据
        initTaskData();
    }

    /**
     * 获取本地数据
     */
    private void initTaskData() {
        if (list == null) list = new ArrayList<>();
        else list.clear();
        list = DbHelpUtils.getProjectListDesc();
        if (!RxDataTool.isEmpty(mTaskAdapter)) {
            mTaskAdapter.refreshInfos(list);
        }
    }

    @Override
    protected DefaultAdapter initAdapter() {
        if (list == null)
            list = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(list, mContext);
        mTaskAdapter.setOnItemClickListener((view, viewType, data, position) -> {
            if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_See_Num)) {
                Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                intent.putExtra(SamplingUtil.INTENT_PROJECT_ID, list.get(position).getId());
                startActivityForResult(intent, TASK_REQUEST_CODE);
            } else {
                showNoPermissionDialog("才能进行表单查看。", UserInfoAppRight.APP_Permission_Sampling_See_Name);
            }
        });
        return mTaskAdapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_REQUEST_CODE && resultCode == TASK_RESULT_CODE) {
            final List<Project> projects = DbHelpUtils.getProjectListDesc();
            mTaskAdapter.refreshInfos(projects);
        }
    }


    @Override
    public void handleMessage(@NonNull Message message) {
        Preconditions.checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_OK:

                break;
            case Message.RESULT_FAILURE:
                cancelRefresh();
                break;
        }
    }
}


