package cn.cdjzxy.android.monitoringassistant.base.base.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.delegate.IActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.NoScrollViewPager;

/**
 * 2019年5月30日嘉泽———昆虫
 * 这个activity为了项目中左右菜单布局的界面使用 使子activity 不在做重复操作
 * 注：继承此activity必须在布局里面实现{@layout.view_tab_pager.xml}布局
 */
public abstract class MyBaseViewPagerActivity extends MyTitleActivity {

    @BindView(R.id.tab_view)
    CustomTab tabView;
    //    @BindView(R.id.recycler_view)
//    RecyclerView recyclerView;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    private FragmentAdapter mFragmentAdapter;

    protected ApiPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initRcView();
        tabView.setTabs(initTabData());
        tabView.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                tabViewOnTabSelect(position);
                // openFragment(position, viewPagerSetCurrentItem());
            }
        });
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), initFragment());
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

//    private void initRcView() {
//        ArtUtils.configRecyclerView(recyclerView,
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
//                    @Override
//                    public boolean canScrollVertically() {//设置RecyclerView不可滑动
//                        return false;
//                    }
//                });
//    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return presenter = new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    /**
     * 打开fragment的方式
     *
     * @return true 用viewPager.setCurrentItem(position)打开;
     * @false 用  mFragmentAdapter.update(position)打开;
     */
    protected abstract boolean viewPagerSetCurrentItem();

    /**
     * 初始化fragment
     *
     * @return Fragment集合
     */
    protected abstract List<Fragment> initFragment();

    /**
     * tabView 点击
     *
     * @param position 点击对应的position
     */
    protected abstract void tabViewOnTabSelect(int position);

    /**
     * 初始化tab 数据
     *
     * @return Tab集合
     */
    protected abstract List<Tab> initTabData();

    /**
     * 切换Fragment页面
     *
     * @param position         那个页面
     * @param isSetCurrentItem 打开方式
     */
    public void openFragment(int position, boolean isSetCurrentItem) {
        hideSoftInput();
        if (isSetCurrentItem) {
            viewPager.setCurrentItem(position);
        } else {
            mFragmentAdapter.update(position);
        }
    }

    /**
     * 设置tabView的宽度
     *
     * @param width 宽度
     */
    public void setTabViewLayoutWidth(int width) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
        params.width = width;
        tabView.setLayoutParams(params);
    }

    /**
     * 给tabView设置背景
     *
     * @param i 背景值
     */
    public void setTabViewBg(int i) {
        tabView.setBackgroundResource(i);
    }

    /**
     * 更新tabView选中状态
     *
     * @param position
     */
    protected void updateTabStatus(int position) {
        tabView.updateTabStatus(position);
    }
}
