package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PlaceAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;


/**
 * f分瓶信息——分析地点
 */
public class PlaceActivity extends MyTitleActivity {

    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    private List<String> placeList;
    private PlaceAdapter placeAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("分析地点选择");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabView.setVisibility(View.GONE);
        placeList = new ArrayList<>();
        initMethodData();
        placeList.add("实验室");
        placeList.add("现场");
        placeAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        placeAdapter = new PlaceAdapter(placeList, mContext);
        placeAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("place", placeList.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(placeAdapter);
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
