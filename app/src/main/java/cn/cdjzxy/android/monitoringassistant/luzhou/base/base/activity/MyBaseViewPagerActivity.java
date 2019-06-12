package cn.cdjzxy.android.monitoringassistant.luzhou.base.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.luzhou.R;
import cn.cdjzxy.android.monitoringassistant.luzhou.base.base.delegate.IActivity;
import cn.cdjzxy.android.monitoringassistant.luzhou.base.mvp.IPresenter;
import cn.cdjzxy.android.monitoringassistant.luzhou.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.luzhou.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.luzhou.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.android.monitoringassistant.luzhou.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.luzhou.widget.NoScrollViewPager;

/**
 * 2019年5月30日嘉泽———昆虫
 * 这个activity为了项目中左右菜单布局的界面使用 使子activity 不在做重复操作
 * 注：继承此activity必须在布局里面实现{@layout.view_tab_pager.xml}布局
 */
public abstract class MyBaseViewPagerActivity extends MyTitleActivity<ApiPresenter> {

    @BindView(R.id.tab_view)
    CustomTab tabView;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    private FragmentAdapter mFragmentAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
