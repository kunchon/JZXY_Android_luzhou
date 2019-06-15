package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.MethodAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;


public class MonItemMethodActivity extends MyTitleActivity {

    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    public static final String MON_ITEM_ID="monItemId";//项目id

    private List<Methods> mMethods = new ArrayList<>();
    private MethodAdapter mMethodAdapter;

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

        //获取监测项目ID
        String monItemId = getIntent().getStringExtra(MON_ITEM_ID);

        if (!RxDataTool.isEmpty((monItemId))) {
            //获取监测项目和监测方法的关系列表
            List<MonItemMethodRelation> methodRelaList =DbHelpUtils.getMonItemMethodRelationList(monItemId);

            //获取方法ID列表
            List<String> methodIdList = new ArrayList<>();
            for (MonItemMethodRelation rela : methodRelaList) {
                methodIdList.add(rela.getMethodId());
            }

            //查询方法
            List<Methods> tempList =DbHelpUtils.getMethodList(methodIdList);
            if (!RxDataTool.isEmpty(tempList)) {
                mMethods.addAll(tempList);
            }
        }

        //初始化方法列表
        initMethodData();
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
                intent.putExtra("MethodName", mMethods.get(position).getName() + "(" + mMethods.get(position).getCode() + ")");
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
