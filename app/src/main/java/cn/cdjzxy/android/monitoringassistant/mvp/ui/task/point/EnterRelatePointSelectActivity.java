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
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.EnterRelatePointSelectAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;


public class EnterRelatePointSelectActivity extends MyTitleActivity {

    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;
  
    private List<EnterRelatePoint> list;
    private EnterRelatePointSelectAdapter adapter;

    private String projectId;
    private String mRcvId;
    private String tagId;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        mRcvId = getIntent().getStringExtra("rcvId");
        tagId = getIntent().getStringExtra("tagId");
        list = new ArrayList<>();
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
        adapter = new EnterRelatePointSelectAdapter(list,mContext);
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("AddressId", list.get(position).getId());
                intent.putExtra("Address", list.get(position).getName());
                intent.putExtra("AddressNo", list.get(position).getCode());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

  

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    private void getPointData(boolean isRelationPoint) {
        List<String> pointIds = new ArrayList<>();
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        //获取立项点位
        // List<ProjectContent> projectContentList= DBHelper.get().getProjectContentDao().queryBuilder().build().list();
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);

        if (!RxDataTool.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                if (content.getTagId().equals(tagId)) {
                    for (String s : content.getAddressIds().split(",")) {
                        pointIds.add(s);
                        Log.d(TAG, s);
                    }
                    break;
                }
            }
        }

        //List<EnterRelatePoint> envirPoints = null;
        List<EnterRelatePoint> envirPoints = new ArrayList<>();
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
                        List<EnterRelatePoint> tempPoints =
                                DbHelpUtils.getEnterRelatePointList(mRcvId,pointIdsTemp);
                        for (EnterRelatePoint EnterRelatePoint : tempPoints) {
                            if (!envirPoints.contains(EnterRelatePoint)) {
                                envirPoints.add(EnterRelatePoint);
                            }
                        }
                    }
                } else {
                    envirPoints =  DbHelpUtils.getEnterRelatePointList(mRcvId,pointIds);
                }
            }

        } else {
            if (RxDataTool.isEmpty(pointIds)) {
                envirPoints = DbHelpUtils.getEnterRelatePointList(mRcvId);
            } else {
                if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                    int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() / RxDataTool.DATA_PAGE_SIZE + 1 : pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                    for (int i = 0; i < pages; i++) {
                        int pageSize = RxDataTool.DATA_PAGE_SIZE;
                        if ((i + 1) == pages) {
                            pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                        }
                        List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE, i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                        List<EnterRelatePoint> tempPoints = DbHelpUtils.getEnterRelatePointListNotIds(mRcvId,pointIdsTemp);
                        for (EnterRelatePoint EnterRelatePoint : tempPoints) {
                            if (!envirPoints.contains(EnterRelatePoint)) {
                                envirPoints.add(EnterRelatePoint);
                            }
                        }
                    }
                } else {
                    List<EnterRelatePoint> tempPoints = DbHelpUtils.getEnterRelatePointListNotIds(mRcvId,pointIds);

                }
            }

        }

        list.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            list.addAll(envirPoints);
        }

        adapter.notifyDataSetChanged();
    }
}
