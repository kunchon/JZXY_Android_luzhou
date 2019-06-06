package cn.cdjzxy.android.monitoringassistant.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.TabAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

public class CustomTab extends LinearLayout {

    private Context mContext;
    private List<Tab> mTabs = new ArrayList<>();
    private TabAdapter mTabAdapter;

    private OnTabSelectListener mOnTabSelectListener;

    public CustomTab(Context context) {
        this(context, null);
    }

    public CustomTab(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = new RecyclerView(mContext);
        addView(recyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(mContext));
        mTabAdapter = new TabAdapter(mTabs,mContext);
        mTabAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateTabStatus(position);
                if (!RxDataTool.isNull(mOnTabSelectListener)) {
                    mOnTabSelectListener.onTabSelected(mTabs.get(position), position);
                }

            }
        });
        recyclerView.setAdapter(mTabAdapter);
    }


    /**
     * 更新tabView选中状态
     *
     * @param position
     */
    public void updateTabStatus(int position) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (i == position) {
                mTabs.get(i).setSelected(true);
            } else {
                mTabs.get(i).setSelected(false);
            }
        }
        mTabAdapter.notifyDataSetChanged();

    }

    /**
     * 设置数据
     *
     * @param tabs
     */
    public void setTabs(String... tabs) {
        if (RxDataTool.isEmpty(tabs)) {
            return;
        }
        mTabs.clear();
        int count = tabs.length;
        for (int i = 0; i < count; i++) {
            Tab tab = new Tab();
            tab.setTabName(tabs[i]);
            tab.setSelected(i == 0);
            mTabs.add(tab);
        }

        mTabAdapter.notifyDataSetChanged();
        if (!RxDataTool.isNull(mOnTabSelectListener)) {
            mOnTabSelectListener.onTabSelected(mTabs.get(0), 0);
        }
    }

    /**
     * 设置数据
     *
     * @param tabs
     */
    public void setTabs(List<Tab> tabs) {
        if (RxDataTool.isEmpty(tabs)) {
            return;
        }
        mTabs.clear();
        mTabs.addAll(tabs);
        mTabAdapter.notifyDataSetChanged();
        if (!RxDataTool.isNull(mOnTabSelectListener)) {
            mOnTabSelectListener.onTabSelected(mTabs.get(0), 0);
        }
    }

    /**
     * 设置 onTabSelectListener
     *
     * @param onTabSelectListener
     */
    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.mOnTabSelectListener = onTabSelectListener;
    }


    public interface OnTabSelectListener {
        void onTabSelected(Tab tab, int position);
    }


}
