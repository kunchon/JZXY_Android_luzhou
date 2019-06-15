package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PointSelectAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;

/**
 * 点位选择 如果采样的监测性质是环境质量{@montype=3} 则在环境质量点位里面查
 */
public class PointSelectActivity extends MyTitleActivity {


    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    private List<EnvirPoint> mEnvirPoints = new ArrayList<>();
    private PointSelectAdapter mPointSelectAdapter;

    private String projectId;
    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
        //        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        //        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ArtUtils.makeText(getApplicationContext(), "搜索");
        //            }
        //        });

    }

  

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        tagId = getIntent().getStringExtra("tagId");
        initTabData();
        initPointData();
        getPointData(true);
    }


    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabView.setTabs("方案点位", "其他点位");
        tabView.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                getPointData(position == 0);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        mPointSelectAdapter = new PointSelectAdapter(mEnvirPoints,mContext);
        mPointSelectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("AddressId", mEnvirPoints.get(position).getId());
                intent.putExtra("Address", mEnvirPoints.get(position).getName());
                intent.putExtra("AddressNo", mEnvirPoints.get(position).getCode());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(mPointSelectAdapter);
    }

    private void getPointData(boolean isRelationPoint) {
        List<String> pointIds = new ArrayList<>();
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
//        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
//        if (!RxDataTool.isEmpty(projectDetials)) {
//            for (ProjectDetial projectDetial : projectDetials) {
//                pointIds.add(projectDetial.getAddressId());
//                Log.d(TAG,projectDetial.getAddressId()+":"+projectDetial.getAddress());
//            }
//        }
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        //获取立项点位
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);
        if (!RxDataTool.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                for (String s : content.getAddressIds().split(",")) {
                    pointIds.add(s);
                    Log.d(TAG, s);
                }

            }
        }

        //List<EnvirPoint> envirPoints = null;
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (isRelationPoint) {
            if (RxDataTool.isEmpty(pointIds)) {
                envirPoints = new ArrayList<>();
            } else {
                if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                    int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() / RxDataTool.DATA_PAGE_SIZE + 1 : pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                    for (int i = 0; i < pages; i++) {
                        int pageSize = RxDataTool.DATA_PAGE_SIZE;
                        if ((i + 1) == pages) {
                            pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                        }
                        List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE, i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                        List<EnvirPoint> tempPoints = DbHelpUtils.getEnvirPointList(tagId,pointIdsTemp);

                        for (EnvirPoint envirPoint : tempPoints) {
                            if (!envirPoints.contains(envirPoint)) {
                                envirPoints.add(envirPoint);
                            }
                        }
                    }
                } else {
                    envirPoints =DbHelpUtils.getEnvirPointList(tagId,pointIds);
                }
            }

        } else {
            if (RxDataTool.isEmpty(pointIds)) {
                envirPoints =DbHelpUtils.getEnvirPointListForTagId(tagId);
            } else {
                if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                    int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() / RxDataTool.DATA_PAGE_SIZE + 1 : pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                    for (int i = 0; i < pages; i++) {
                        int pageSize = RxDataTool.DATA_PAGE_SIZE;
                        if ((i + 1) == pages) {
                            pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                        }
                        List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE, i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                        List<EnvirPoint> tempPoints =DbHelpUtils.getEnvirPointListNotInIds(tagId,pointIdsTemp);
                        for (EnvirPoint envirPoint : tempPoints) {
                            if (!envirPoints.contains(envirPoint)) {
                                envirPoints.add(envirPoint);
                            }
                        }
                    }
                } else {
                    envirPoints = DbHelpUtils.getEnvirPointListNotInIds(tagId,pointIds);

                }
            }

        }

        mEnvirPoints.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            mEnvirPoints.addAll(envirPoints);
        }

        mPointSelectAdapter.notifyDataSetChanged();
    }


    @Override
    public void handleMessage(@NonNull Message message) {
        
    }
}
