package cn.cdjzxy.android.monitoringassistant.mvp.ui.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.android.monitoringassistant.mvp.model.greendao.UserDao;

import cn.cdjzxy.android.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.UserAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.widget.GridItemDecoration;


/**
 * 采样人员
 */

public class UserActivity extends MyTitleActivity {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;
    @BindView(R.id.text_optional)
    TextView tvOptional;
    @BindView(R.id.text_select)
    TextView tvSelect;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.linear_search)
    LinearLayout linearSearch;

    private UserAdapter mUserAdapter;

    private UserAdapter mUserSelectedAdapter;

    private String projectId;

    private List<User> mUsers = new ArrayList<>();
    private List<User> mUsersSelected = new ArrayList<>();

    private StringBuilder UserName;
    private StringBuilder UserId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样人员");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RxDataTool.isEmpty(mUsersSelected)) {
                    showMessage("请至少选择一个人员");
                    return;
                }
                getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("UserName", UserName.toString());
                intent.putExtra("UserId", UserId.toString());
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
        initUsersView();
        initUsersSelectedView();
        tvSelect.setText("已选人员");
        tvOptional.setText("可选人员");
        etSearch.setHint("请输入查找人员姓名");
        linearSearch.setVisibility(View.GONE);

        projectId = getIntent().getStringExtra("projectId");
        String selectUserIds = getIntent().getStringExtra("selectUserIds");

        Project project = DbHelpUtils.getDbProject(projectId);
        List<String> userIds = project.getSamplingUser();
        List<User> users = DbHelpUtils.getUserList(userIds);

        mUsersSelected.clear();
        mUsers.clear();
        if (!RxDataTool.isEmpty(users)) {
            for (User user : users) {
                if (!TextUtils.isEmpty(selectUserIds)) {
                    if (selectUserIds.contains(user.getId())) {
                        user.setSelected(true);
                        mUsersSelected.add(user);
                    }
                    mUsers.add(user);
                } else {
                    mUsers.addAll(users);
                    return;
                }
            }
        }


    }


    private void initUsersView() {
        ArtUtils.configRecyclerView(rvProject, new GridLayoutManager(this, 4));
        mUserAdapter = new UserAdapter(mUsers, mContext);
        mUserAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemState(position);
            }
        });
        rvProject.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProject.setAdapter(mUserAdapter);
    }


    private void initUsersSelectedView() {
        ArtUtils.configRecyclerView(rvProjectSelected, new GridLayoutManager(this, 4));
        mUserSelectedAdapter = new UserAdapter(mUsersSelected, mContext);
        mUserSelectedAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemSelectedState(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mUserSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.iv_add_monitem:
//                addMonItems();
//                break;
//            case R.id.iv_delete_monitem:
//                deleteMonItems();
//                break;
//        }
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemState(int position) {
        User user = mUsers.get(position);

        if (!user.isSelected()) {
            user.setSelected(true);
            mUsersSelected.add(user);
            mUserSelectedAdapter.notifyDataSetChanged();
            mUserAdapter.notifyDataSetChanged();
        }
//        if (mUsers.get(position).isSelected()) {
//            mUsers.get(position).setSelected(false);
//        } else {
//            mUsers.get(position).setSelected(true);
//        }

    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemSelectedState(int position) {
        User user = mUsersSelected.get(position);
        user.setSelected(false);
        mUsersSelected.remove(user);
        for (int i = 0; i < mUsers.size(); i++) {
            if (mUsers.get(i).getId().equals(user.getId())) {
                mUsers.set(i, user);
                break;
            }
        }
//        if (mUsersSelected.get(position).isSelected()) {
//            mUsersSelected.get(position).setSelected(false);
//        } else {
//            mUsersSelected.get(position).setSelected(true);
//        }
        mUserSelectedAdapter.notifyDataSetChanged();
        mUserAdapter.notifyDataSetChanged();
    }


    private void addMonItems() {
        List<User> tempList = new ArrayList<>();
        for (User user : mUsers) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsersSelected.add(user);
                tempList.add(user);
                //mUsers.remove(user);
            }
        }

        if (!RxDataTool.isEmpty(tempList)) {
            for (User user : tempList) {
                mUsers.remove(user);
            }
        }

        /*
        for (User user : mUsers) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsersSelected.add(user);
                mUsers.remove(user);
            }
        }
        */
        mUserAdapter.notifyDataSetChanged();
        mUserSelectedAdapter.notifyDataSetChanged();
    }

    private void deleteMonItems() {
        List<User> tempList = new ArrayList<>();
        for (User user : mUsersSelected) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsers.add(user);
                tempList.add(user);
            }
        }

        if (!RxDataTool.isEmpty(tempList)) {
            for (User user : tempList) {
                mUsersSelected.remove(user);
            }
        }
        /*
        for (User user : mUsersSelected) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsers.add(user);
                mUsersSelected.remove(user);
            }
        }
        */
        mUserAdapter.notifyDataSetChanged();
        mUserSelectedAdapter.notifyDataSetChanged();
    }


    private void getMonItemsData() {
        UserName = new StringBuilder();
        UserId = new StringBuilder();


        for (User user : mUsersSelected) {
            UserName.append(user.getName() + ",");
            UserId.append(user.getId() + ",");
        }

        if (UserName.lastIndexOf(",") > 0) {
            UserName.deleteCharAt(UserName.lastIndexOf(","));
        }

        if (UserId.lastIndexOf(",") > 0) {
            UserId.deleteCharAt(UserId.lastIndexOf(","));
        }
    }


    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
