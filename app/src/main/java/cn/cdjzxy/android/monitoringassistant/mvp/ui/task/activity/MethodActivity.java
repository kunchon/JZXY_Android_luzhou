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
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.MethodAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;

public class MethodActivity extends MyTitleActivity {

    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    private List<Methods> mMethods = new ArrayList<>();
    private MethodAdapter mMethodAdapter;

    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("方法");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabView.setVisibility(View.GONE);
        initMethodData();
        tagId = getIntent().getStringExtra("tagId");
        Tags tags =DbHelpUtils.getTags(tagId);
        if (!RxDataTool.isNull(tags)) {
            List<Methods> methods = tags.getMMethods();
            if (!RxDataTool.isEmpty(methods)) {
                mMethods.clear();
                for (Methods method : methods) {
                    if (method.getLabelType() == 1) {
                        mMethods.add(method);
                    }
                }
            }
        }
        mMethodAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        mMethodAdapter = new MethodAdapter(mMethods,mContext);
        mMethodAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("MethodId", mMethods.get(position).getId());
                intent.putExtra("MethodName", mMethods.get(position).getName()
                        + "("+mMethods.get(position).getCode()+")");

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(mMethodAdapter);
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
