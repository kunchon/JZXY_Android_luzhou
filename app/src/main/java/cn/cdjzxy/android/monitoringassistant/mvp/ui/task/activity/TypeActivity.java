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
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.TagAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;


public class TypeActivity extends MyTitleActivity {


    @BindView(R.id.view_pager)
    RecyclerView recyclerView;
    @BindView(R.id.tab_view)
    CustomTab tabView;

    private List<Tags> mTags;
    private TagAdapter mTagAdapter;

    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            titleBar.setTitleMainText(title);
            return;
        }
        titleBar.setTitleMainText("降水类型选择");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.view_tab_recyler_view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabView.setVisibility(View.GONE);
        mTags = new ArrayList<>();
        initMethodData();
        tagId = getIntent().getStringExtra("tagId");
        List<Tags> tags = DbHelpUtils.getTagsForParentId(tagId);
        if (!RxDataTool.isEmpty(tags)) {
            for (Tags tag : tags) {
                if (tag.getLevel() == 1) {
                    mTags.add(tag);
                }
            }
        }

        mTagAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        mTagAdapter = new TagAdapter(mTags, mContext);
        mTagAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("TagId", mTags.get(position).getId());
                intent.putExtra("TagName", mTags.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(mTagAdapter);
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
