package cn.cdjzxy.android.monitoringassistant.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.FormSelectAdapter;
import cn.cdjzxy.android.monitoringassistant.user.db.DBHelper;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlus;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlusBuilder;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.ViewHolder;

public class TagsUtils {
    /**
     * 把tag数据更新到数据库  因为是基础数据  所以直接删除后插入
     *
     * @param tagsList
     */
    public static void insertDb(List<Tags> tagsList) {
        if (!RxDataTool.isEmpty(tagsList)) {
            DBHelper.get().getTagsDao().deleteAll();
            DBHelper.get().getTagsDao().insertInTx(tagsList);
        }
    }

    /**
     * 获取数据库中所有的tags表
     *
     * @return
     */
    public static List<Tags> getAllDbTags() {
//        return DbHelpUtils.getTags();
        return DBHelper.get().getTagsDao().loadAll();
    }

    /**
     * 通过{TagsDao.Properties.Id}查找数据库所有的TagsDao表
     * TagsDao 要素分类表
     *
     * @param tagId 要素分类表
     * @return Tags 要素
     */
    public static Tags getTags(String tagId) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
    }

    /**
     * 获取
     *
     * @return
     */
    public static List<Tags> getTagsLevel0() {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Level.eq(0)).list();
    }


    /**
     * 显示要素选择框
     */
    public static void showAddDialog(Context context,FormSelectAdapterOnItemClick onItemClick) {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_tag, null);
        dialogPlusBuilder.setContentHolder(new ViewHolder(view));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setContentHeight(800);
        DialogPlus mDialogPlus = dialogPlusBuilder.create();
        initDialogView(view, context, mDialogPlus,onItemClick);
        mDialogPlus.show();


    }

    private static void initDialogView(View view, Context context,
                                       DialogPlus dialogPlus,FormSelectAdapterOnItemClick onItemClick) {
        CustomTab mCustomTab = view.findViewById(R.id.tabview);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        List<Tab> tabList = new ArrayList<>();
        List<Form> formList = DbHelpUtils.getFormList();
        if (!RxDataTool.isEmpty(formList)) {
            for (Form form : formList) {
                Tab tab = new Tab();
                tab.setTabName(form.getTagName());
                tab.setSelected(false);
                tabList.add(tab);
            }
        }
        if (!RxDataTool.isEmpty(tabList)) {
            tabList.get(0).setSelected(true);
            mCustomTab.setTabs(tabList);
        }
        FormSelectAdapter mFormSelectAdapter = new
                FormSelectAdapter(getFormSelectList(formList.get(0).getTagId()), context);
        mCustomTab.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                mFormSelectAdapter.refreshInfos(getFormSelectList(formList.get(position).getTagId()));
            }
        });

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(context));
        mFormSelectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (onItemClick!=null){
                    onItemClick.onItemClick(mFormSelectAdapter.getItem(position));
                }
                dialogPlus.dismiss();
            }
        });
        mRecyclerView.setAdapter(mFormSelectAdapter);
    }

    /**
     * 根据tagParentId 获取 List<FormSelect>
     *
     * @param tagParentId 表单分类的tagId
     * @return
     */
    public static List<FormSelect> getFormSelectList(String tagParentId) {
        return DbHelpUtils.getDbFormSelectForTagParentId(tagParentId);
    }

    public static interface FormSelectAdapterOnItemClick {
        void onItemClick(FormSelect formSelect);
    }
}
