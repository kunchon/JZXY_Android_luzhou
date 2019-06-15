package cn.cdjzxy.android.monitoringassistant.mvp.ui.task.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.MonItemAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.BottleSplitUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.GridItemDecoration;


/**
 * 监测项目_分瓶编辑
 */

public class BottleMonItemActivity extends MyTitleActivity {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;
    @BindView(R.id.linear_search)
    LinearLayout linearSearch;

    private MonItemAdapter mMonItemAdapter;

    private MonItemAdapter mMonItemSelectedAdapter;

    private String tagId;
    private String monItemId;
    private String samplingId;

    private List<MonItems> mMonItems = new ArrayList<>();
    private List<MonItems> mMonItemsSelected = new ArrayList<>();
    //    private List<MonItems> mMonItemsDelete = new ArrayList<>();
    private StringBuilder selectMonItemNameBuilder;//选中
    private StringBuilder selectMonItemIdBuilder;
    private StringBuilder monItemNameBuilder;
    private StringBuilder monItemIdBuilder;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("selectMonItemName", selectMonItemNameBuilder.toString());
                intent.putExtra("selectMonItemId", selectMonItemIdBuilder.toString());
                intent.putExtra("MonItemName", monItemNameBuilder.toString());
                intent.putExtra("MonItemId", monItemIdBuilder.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

 
    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_project;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initMonItemsView();
        initMonItemsSelectedView();
        linearSearch.setVisibility(View.GONE);

        tagId = getIntent().getStringExtra("tagId");
        samplingId = getIntent().getStringExtra("samplingId");
        monItemId = getIntent().getStringExtra("monItemId");
        String selectItemsStr = getIntent().getStringExtra("selectItems");
//        if (!RxDataTool.isEmpty(selectItemsStr)) {
//          String[]  selectItems = selectItemsStr.split(",");
//        }
        List<String> monItemIdList = BottleSplitUtils.getAllJcMonitIds(samplingId);

        //设置未选中的item
        Tags tags =DbHelpUtils.getTags(tagId) ;
        List<MonItems> allMonItems = tags.getMMonItems();
        //List<MonItems> monItems = tags.getMMonItems();
        if (!RxDataTool.isEmpty(allMonItems)) {
            mMonItems.clear();
            for (MonItems monItems : allMonItems) {
                if (monItemIdList.contains(monItems.getId())) {
                    if (!RxDataTool.isEmpty(selectItemsStr) &&
                            !selectItemsStr.contains(monItems.getId())) {
                        monItems.setSelected(false);
                        mMonItems.add(monItems);
                    } else {
                        monItems.setSelected(true);
                        mMonItemsSelected.add(monItems);
                    }
                }
            }
        }
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();

    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getSearchView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_search, null);
        return view;
    }


    private void initMonItemsView() {
        ArtUtils.configRecyclerView(rvProject, new GridLayoutManager(this, 4));
        mMonItemAdapter = new MonItemAdapter(mMonItems,mContext);
        mMonItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemState(position);
            }
        });
        rvProject.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProject.setAdapter(mMonItemAdapter);
    }


    private void initMonItemsSelectedView() {
        ArtUtils.configRecyclerView(rvProjectSelected, new GridLayoutManager(this, 4));
        mMonItemSelectedAdapter = new MonItemAdapter(mMonItemsSelected,mContext);
        mMonItemSelectedAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemSelectedState(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mMonItemSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:
                // addMonItems();
                break;
            case R.id.iv_delete_monitem:
                //  deleteMonItems();
                break;
        }
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemState(int position) {
        MonItems monItems = mMonItems.get(position);
        monItems.setSelected(true);
        mMonItemsSelected.add(monItems);
        mMonItems.remove(position);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemSelectedState(int position) {
        if (mMonItemsSelected.size() == 1) {
            showMessage("至少保留一个监测项目");
            return;
        }

        MonItems monItems = mMonItemsSelected.get(position);
        monItems.setSelected(false);
        mMonItems.add(monItems);
        mMonItemsSelected.remove(position);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }


//    private void addMonItems() {
//        mMonItemsDelete.clear();
//        for (MonItems monItem : mMonItems) {
//            if (monItem.isSelected()) {
//                monItem.setSelected(false);
//                mMonItemsSelected.add(monItem);
//                mMonItemsDelete.add(monItem);
//            }
//        }
//        mMonItems.removeAll(mMonItemsDelete);
//        mMonItemAdapter.notifyDataSetChanged();
//        mMonItemSelectedAdapter.notifyDataSetChanged();
//    }
//
//    private void deleteMonItems() {
//        mMonItemsDelete.clear();
//        for (MonItems monItems : mMonItemsSelected) {
//            if (monItems.isSelected()) {
//                monItems.setSelected(false);
//                mMonItems.add(monItems);
//                mMonItemsDelete.add(monItems);
//            }
//        }
//        mMonItemsSelected.removeAll(mMonItemsDelete);
//        mMonItemAdapter.notifyDataSetChanged();
//        mMonItemSelectedAdapter.notifyDataSetChanged();
//    }


    private void getMonItemsData() {
        selectMonItemNameBuilder = new StringBuilder();
        selectMonItemIdBuilder = new StringBuilder();
        monItemNameBuilder = new StringBuilder();
        monItemIdBuilder = new StringBuilder();
        if (RxDataTool.isEmpty(mMonItemsSelected)) {
            return;
        }

        for (MonItems monItems : mMonItemsSelected) {
            selectMonItemNameBuilder.append(monItems.getName() + ",");
            selectMonItemIdBuilder.append(monItems.getId() + ",");
        }

        if (selectMonItemNameBuilder.lastIndexOf(",") > 0) {
            selectMonItemNameBuilder.deleteCharAt(selectMonItemNameBuilder.lastIndexOf(","));
        }

        if (selectMonItemIdBuilder.lastIndexOf(",") > 0) {
            selectMonItemIdBuilder.deleteCharAt(selectMonItemIdBuilder.lastIndexOf(","));
        }

        for (MonItems items : mMonItems) {
            monItemNameBuilder.append(items.getName() + ",");
            monItemIdBuilder.append(items.getId() + ",");
        }
        if (monItemNameBuilder.lastIndexOf(",") > 0) {
            monItemNameBuilder.deleteCharAt(monItemNameBuilder.lastIndexOf(","));
        }

        if (monItemIdBuilder.lastIndexOf(",") > 0) {
            monItemIdBuilder.deleteCharAt(monItemIdBuilder.lastIndexOf(","));
        }
    }


    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
