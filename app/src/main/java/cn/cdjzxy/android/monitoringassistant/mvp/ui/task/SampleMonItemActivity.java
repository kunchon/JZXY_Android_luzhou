package cn.cdjzxy.android.monitoringassistant.mvp.ui.task;

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
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;

import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.WasteWaterMonIteMAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;

/**
 * 获取某种采样单的检测项目
 * 需要传入任务Id,和采样单表单路径（区分是什么采样单）
 */
public class SampleMonItemActivity extends MyTitleActivity {


    @BindView(R.id.recyclerView_monite)
    RecyclerView recyclerViewMonite;

    private String projectId;
    private WasteWaterMonIteMAdapter mWasteWaterMonItemAdapter;

    private List<MonItems> mMontes = new ArrayList<>();
    public static final String INTENT_PATH = "intent_path";

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("项目选择");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_waste_water_monite;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra(SamplingUtil.INTENT_PROJECT_ID);
        String path = getIntent().getStringExtra(INTENT_PATH);
        //获取水和废水采样单
        getSamplings(path);

        //初始化水和废水样品数据
        initSamplingDetailsData();
    }

    /**
     * 获取水和废水采样单的所有样品检测项目
     *
     * @param path
     */
    private void getSamplings(String path) {
        List<Sampling> samplings = DbHelpUtils.getDbSampling(projectId, path);
        if (RxDataTool.isEmpty(samplings)) {
            return;
        }

        HashMap<String, HashMap<String, MonItems>> monItemsMap = new HashMap<String, HashMap<String, MonItems>>();

        HashMap<String, MonItems> monites = new HashMap<>();
        for (Sampling item : samplings) {
            if (TextUtils.isEmpty(item.getParentTagId())) {
                continue;
            }

            //从数据库加载项目，避免项目名称显示错误
            HashMap<String, MonItems> monItemMap = null;
            if (!monItemsMap.containsKey(item.getParentTagId())) {
                monItemMap = new HashMap<String, MonItems>();
                monItemsMap.put(item.getParentTagId(), monItemMap);

                Tags tags = DbHelpUtils.getTags(item.getParentTagId());

                List<MonItems> monItems = tags.getMMonItems();
                if (!RxDataTool.isEmpty(monItems)) {
                    for (MonItems monItem : monItems) {
                        monItemMap.put(monItem.getId(), monItem);
                    }
                }
            } else {
                monItemMap = monItemsMap.get(item.getParentTagId());
            }

            //获取样品数据
            List<SamplingContent> contentList = item.getSamplingContentResults();

            //如果为空则尝试从数据库获取
            if (RxDataTool.isEmpty(contentList)) {
                contentList = DbHelpUtils.getSamplingContentList(item.getId());
            }

            for (SamplingContent content : contentList) {
                //水和废水中，现场监测项目
                if (TextUtils.isEmpty(content.getSenceMonitemId())) {
                    continue;
                }

                String[] moniteIds = content.getSenceMonitemId().split(",");

                for (int i = 0; i < moniteIds.length; i++) {
                    String id = moniteIds[i];
                    String name = monItemMap.get(id).getName();

                    MonItems monItem = null;

                    //过滤重复项
                    if (!monites.containsKey(id)) {
                        monItem = new MonItems(id, "", name);
                        monItem.setAddressId(item.getAddressId());//点位ID
                        monItem.setAddressName(item.getAddressName());//点位名称
                        monItem.setTagId(item.getTagId());//样品性质ID
                        monItem.setTagName(item.getTagName());//样品性质

                        monites.put(id, monItem);

                        mMontes.add(monItem);
                    } else {
                        monItem = monites.get(id);
                        //记录所有采样单点位
                        if (!monItem.getAddressId().contains(item.getAddressId())) {
                            monItem.setAddressId(monItem.getAddressId() + "," + item.getAddressId());
                            monItem.setAddressName(monItem.getAddressName() + "," + item.getAddressName());
                        }

                        //记录所有样品性质
                        if (!monItem.getTagId().contains(item.getTagId())) {
                            monItem.setTagId(monItem.getTagId() + "," + item.getTagId());
                            monItem.setTagName(monItem.getTagName() + "," + item.getTagName());
                        }
                    }

//                    //组装所有现场监测项目，去重
//                    String allMonitemId = monItem.getAllMonitemId();
//                    String allMonitemName = monItem.getAllMonitemName();
//                    if (TextUtils.isEmpty(allMonitemId)) {
//                        allMonitemId = "";
//                    }
//                    if (TextUtils.isEmpty(allMonitemName)) {
//                        allMonitemName = "";
//                    }
//                    for (String mid : moniteIds) {
//                        if (allMonitemId.contains(mid)) {
//                            continue;
//                        }
//
//                        if (allMonitemId.length() > 0) {
//                            allMonitemId += ",";
//                            allMonitemName += ",";
//                        }
//                        allMonitemId += mid;
//                        allMonitemName += monItemMap.get(mid).getName();
//                    }
//
//                    monItem.setAllMonitemId(allMonitemId);
//                    monItem.setAllMonitemName(allMonitemName);
                }
            }
        }
    }

    /**
     * 初始化Tab数据
     */
    private void initSamplingDetailsData() {
        ArtUtils.configRecyclerView(recyclerViewMonite, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mWasteWaterMonItemAdapter = new WasteWaterMonIteMAdapter(mMontes, mContext);
        mWasteWaterMonItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (position < 0 || position >= mMontes.size()) {
                    return;
                }

                MonItems item = mMontes.get(position);
                if (item == null) {
                    return;
                }

                Intent intent = new Intent();

                //现场监测项目信息
                intent.putExtra("MonitemId", item.getId());
                intent.putExtra("MonitemName", item.getName());

                //所有点位集合
                intent.putExtra("AddressId", item.getAddressId());
                intent.putExtra("AddressName", item.getAddressName());

                //所有样品性质集合
                intent.putExtra("TagId", item.getTagId());
                intent.putExtra("TagName", item.getTagName());

//                //所有现场监测项目信息
//                intent.putExtra("AllMonitemId", item.getAllMonitemId());
//                intent.putExtra("AllMonitemName", item.getAllMonitemName());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewMonite.setAdapter(mWasteWaterMonItemAdapter);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}