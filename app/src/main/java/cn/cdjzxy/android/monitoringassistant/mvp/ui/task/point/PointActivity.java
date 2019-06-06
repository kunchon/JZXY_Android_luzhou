package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.point;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;

import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;


import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PointAdapter;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


/**
 * 采样点位
 */

public class PointActivity extends MyTitleActivity implements IView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.linear_add)
    LinearLayout linearAdd;
    private String projectId;

    private PointAdapter mPointAdapter;

    private Project mProject;
    private List<ProjectDetial> mProjectDetials;
    private Map<String, ProjectDetial> mStringProjectDetialMap;


    private static final int authBaseRequestCode = 1;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private String mSDCardPath = null;

    private LocationClient locationClient;
    public BDLocation bdLocation;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样点位");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        mProject = DbHelpUtils.getDbProject(projectId);
        if (mProject.getCanSamplingEidt()) {
            linearAdd.setVisibility(View.VISIBLE);
        } else {
            linearAdd.setVisibility(View.GONE);
        }

        getData();
        initPointData();


        //初始化导航
        if (initDirs()) {
            initNavi();
        }
        //初始化定位
        initLocation();
    }

    /**
     * 初始化Tab数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));
        mPointAdapter = new PointAdapter(this, mProjectDetials, mProject.getCanSamplingEidt(),
                new PointAdapter.ItemAdapterOnClickListener() {
                    @Override
                    public void onItemOnClick(EnvirPoint point) {
                        routePlanToNavi(point);
                    }
                });
        mPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (mProject.getCanSamplingEidt()) {
                    Intent intent = new Intent(PointActivity.this, ProgramModifyActivity.class);
                    intent.putExtra("isAdd", false);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("projectDetail", mProjectDetials.get(position));
                    ArtUtils.startActivity(intent);
                }

            }
        });
        recyclerview.setAdapter(mPointAdapter);
    }

    @OnClick({R.id.linear_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_add:
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Add_Num)) {
                    Intent intent = new Intent();
                    intent.setClass(PointActivity.this, ProgramModifyActivity.class);
                    intent.putExtra("projectId", projectId);
                    intent.putExtra("isAdd", true);
                    ArtUtils.startActivity(intent);
                } else {
                    showNoPermissionDialog("才能进行采样方案添加。", UserInfoAppRight.APP_Permission_Plan_See_Name);
                }
                break;
        }
    }

    @Subscriber(tag = EventBusTags.TAG_PROGRAM_MODIFY)
    private void updateData(boolean isModified) {
        getData();
    }

    private void getData() {
        if (mProjectDetials == null) mProjectDetials = new ArrayList<>();
        else mProjectDetials.clear();
        if (mStringProjectDetialMap == null) mStringProjectDetialMap = new HashMap<>();
        else mStringProjectDetialMap.clear();
        List<ProjectDetial> projectDetials =DbHelpUtils.getProjectDetialList(projectId);
        if (!RxDataTool.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (RxDataTool.isNull(mStringProjectDetialMap.get(projectDetial.getProjectContentId()))) {
                    mStringProjectDetialMap.put(projectDetial.getProjectContentId(), newAsameProjectDetial(projectDetial));
                } else {
                    ProjectDetial projectDetial1 = mStringProjectDetialMap.get(projectDetial.getProjectContentId());
                    if (!projectDetial1.getAddressId().contains(projectDetial.getAddressId())) {
                        projectDetial1.setAddressId(projectDetial1.getAddressId() + "," + projectDetial.getAddressId());
                        projectDetial1.setAddress(projectDetial1.getAddress() + "," + projectDetial.getAddress());
                    }
                    if (!projectDetial1.getMonItemId().contains(projectDetial.getMonItemId())) {
                        projectDetial1.setMonItemId(projectDetial1.getMonItemId() + "," + projectDetial.getMonItemId());
                        projectDetial1.setMonItemName(projectDetial1.getMonItemName() + "," + projectDetial.getMonItemName());
                    }
                    mStringProjectDetialMap.put(projectDetial1.getProjectContentId(), projectDetial1);
                }
            }

            for (String key : mStringProjectDetialMap.keySet()) {
                mProjectDetials.add(mStringProjectDetialMap.get(key));
            }

        }
        if (mPointAdapter != null) {
            mPointAdapter.refreshInfos(mProjectDetials);
        }

        //mPointAdapter.notifyDataSetChanged();
    }

    /**
     * copy a new ProjectDetial
     *
     * @param projectDetials
     * @return
     */
    private ProjectDetial newAsameProjectDetial(ProjectDetial projectDetials) {
        ProjectDetial newProjectDetial = new ProjectDetial();
        newProjectDetial.setUpdateTime(projectDetials.getUpdateTime());
        newProjectDetial.setTagParentName(projectDetials.getTagParentName());
        newProjectDetial.setTagParentId(projectDetials.getTagParentId());
        newProjectDetial.setTagName(projectDetials.getTagName());
        newProjectDetial.setTagId(projectDetials.getTagId());
        newProjectDetial.setProjectId(projectDetials.getProjectId());
        newProjectDetial.setProjectContentId(projectDetials.getProjectContentId());
        newProjectDetial.setPeriod(projectDetials.getPeriod());
        newProjectDetial.setMonItemId(projectDetials.getMonItemId());
        newProjectDetial.setMonItemName(projectDetials.getMonItemName());
        newProjectDetial.setMethodName(projectDetials.getMethodName());
        newProjectDetial.setMethodId(projectDetials.getMethodId());
        newProjectDetial.setDays(projectDetials.getDays());
        newProjectDetial.setComment(projectDetials.getComment());
        newProjectDetial.setId(projectDetials.getId());
        newProjectDetial.setAddressId(projectDetials.getAddressId());
        newProjectDetial.setAddress(projectDetials.getAddress());
        return newProjectDetial;
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

//        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
//                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {
//
//                    @Override
//                    public void onAuthResult(int status, String msg) {
//                        String result;
//                        if (0 == status) {
//                            result = "key校验成功!";
//                        } else {
//                            result = "key校验失败, " + msg;
//                        }
////                        ArtUtils.makeText(PointActivity.this, result);
//                        Log.e(TAG, "onAuthResult: " + result);
//                        Log.i(TAG, "onAuthResult: " + result);
//                    }
//
//                    @Override
//                    public void initStart() {
////                        ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化开始");
//                        System.out.print("百度导航引擎初始化开始");
//                        Log.i(TAG, "initStart: 百度导航引擎初始化开始");
//                    }
//
//                    @Override
//                    public void initSuccess() {
//                        System.out.print("百度导航引擎初始化成功");
//                        Log.i(TAG, "initStart: 百度导航引擎初始化成功");
//                        // ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化成功");
//                        //hasInitSuccess = true;
//                    }
//
//                    @Override
//                    public void initFailed() {
//                        System.out.print("百度导航引擎初始化失败");
//                        Log.e(TAG, "initStart: 百度导航引擎初始化失败");
//                        ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化失败");
//                    }
//                });

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    ArtUtils.makeText(this, "缺少导航基本的权限!");
                    return;
                }
            }
            initNavi();
        }
    }

    private void initLocation() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        //设置option
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        //option.setCoorType("GCJ02");
        option.setScanSpan(3000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(true);
        locationClient.setLocOption(option);
        locationClient.start();

    }


    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            PointActivity.this.bdLocation = location;
        }
    }

    /**
     * 导航前往目标点位
     *
     * @param pointSelect
     */
    private void routePlanToNavi(EnvirPoint pointSelect) {
        if (bdLocation == null) {
//            ArtUtils.makeText(mContext, "未定位到当前位置，请重试");
            showMessage("未定位到当前位置，请重试");
            return;
        }

        showLoading("正在初始化导航，请稍后...");
//        ArtUtils.makeText(mContext, "正在计算前往"+bdLocation.getLongitude() + "，" + bdLocation.getLatitude()+"线路");
        Log.i(TAG, "routeplanToNavi: +导航定位：\n经度" + bdLocation.getLongitude() + "\n纬度" + bdLocation.getLatitude());

//        BNRoutePlanNode sNode = new BNRoutePlanNode(bdLocation.getLongitude(), bdLocation.getLatitude(), "",
//                "", BNRoutePlanNode.CoordinateType.BD09LL);
//        BNRoutePlanNode eNode = new BNRoutePlanNode(pointSelect.getLongtitude(),
//                pointSelect.getLatitude(), pointSelect.getName(), pointSelect.getName(),
//                BNRoutePlanNode.CoordinateType.BD09LL);
//
//        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//        list.add(sNode);
//        list.add(eNode);
//
//        final BNRoutePlanNode mStartNode = sNode;
//
//        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
//                list,
//                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
//                null,
//                new Handler(Looper.getMainLooper()) {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        switch (msg.what) {
//                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
////                                Toast.makeText(mContext, "算路开始", Toast.LENGTH_SHORT)
////                                        .show();
//                                showMessage("算路开始");
//                                break;
//                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
//                                showMessage("算路成功");
////                                Toast.makeText(mContext, "算路成功", Toast.LENGTH_SHORT)
////                                        .show();
//                                break;
//                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
////                                Toast.makeText(mContext, "算路失败", Toast.LENGTH_SHORT)
////                                        .show();
//                                showMessage("算路失败");
//                                break;
//                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
////                                Toast.makeText(mContext, "算路成功准备进入导航", Toast.LENGTH_SHORT)
////                                        .show();
//                                showMessage("算路成功准备进入导航");
//                                Intent intent = new Intent(mContext,
//                                        NavigationActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("endPoint", mStartNode);
//                                intent.putExtras(bundle);
//                                ArtUtils.startActivity(intent);
//                                break;
//                            default:
//                                hideLoading();
//                                // nothing
//                                break;
//                        }
//                    }
//                });
    }

}
