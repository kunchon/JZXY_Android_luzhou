package cn.cdjzxy.android.monitoringassistant.base.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.mapapi.NetworkUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.WanderTaskAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 2019年6月4日嘉泽———昆虫
 * 这个activity为了项目中需要刷新界面中使用的界面使用 使子activity 不在做重复操作
 * 注：继承此activity必须在布局里面实现{@R.layout.view_refresh_layout.xml}布局
 */
public abstract class MyRefreshActivity extends MyTitleActivity {
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.header)
    BezierRadarHeader mRefreshHeader;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    protected int page = 1; //当前页数
    protected DefaultAdapter adapter;
    protected boolean isRefresh;//true是否刷新 false加载


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRefresh();
    }

    /**
     * 初始化刷新控件
     */
    private void initRefresh() {
        isRefresh = true;
        mContext = this;
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            showLoading();
        }
        //mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = initAdapter();
        mRecyclerView.setAdapter(adapter);
        initLister();
        getData(true);//第一次不执行刷新动画，直接请求数据
    }

    protected abstract DefaultAdapter initAdapter();


    /**
     * 初始化监听
     */
    private void initLister() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (NetworkUtil.isNetworkAvailable(mContext)) {
                    page += 1;
                    isRefresh = false;
                    getData(false);
                } else {
                    showMessage("当前没有网络");
                    cancelRefresh();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (NetworkUtil.isNetworkAvailable(mContext)) {
                    page = 1;
                    isRefresh = true;
                    getData(false);
                } else {
                    cancelRefresh();
                    showMessage("当前没有网络");
                }
            }
        });

    }

    /**
     * 取消加载
     */
    protected void cancelRefresh() {
        if (isRefresh) {
            mRefreshLayout.finishRefresh(page);
        } else {
            mRefreshLayout.finishLoadMore(page);
        }
    }

    /**
     * 请求数据
     *
     * @param isFirst 是否第一次请求：用于拿到本地数据
     */
    protected abstract void getData(boolean isFirst);
}
