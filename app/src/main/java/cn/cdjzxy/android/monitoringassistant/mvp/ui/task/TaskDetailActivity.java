package cn.cdjzxy.android.monitoringassistant.mvp.ui.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.mapapi.NetworkUtil;
import com.google.gson.Gson;


import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


import java.lang.reflect.Field;
import java.util.ArrayList;

import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.base.base.activity.MyTitleActivity;
import cn.cdjzxy.android.monitoringassistant.base.mvp.IView;
import cn.cdjzxy.android.monitoringassistant.base.mvp.Message;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.FormSelect;

import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;


import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.FormSelectAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.task.point.PointActivity;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.android.monitoringassistant.utils.TagsUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDateTool;
import cn.cdjzxy.android.monitoringassistant.widget.CustomTab;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlus;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.DialogPlusBuilder;
import cn.cdjzxy.android.monitoringassistant.widget.dialogplus.ViewHolder;
import me.jessyan.autosize.utils.Preconditions;


/**
 * 任务详情
 */
public class TaskDetailActivity extends MyTitleActivity implements IView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_task_name)
    TextView tvTaskName;
    @BindView(R.id.tv_task_time_range)
    TextView tvTaskTimeRange;
    @BindView(R.id.tv_task_num)
    TextView tvTaskNum;
    @BindView(R.id.tv_task_point)
    TextView tvTaskPoint;
    @BindView(R.id.tv_task_project_num)
    TextView tvTaskProjectNum;
    @BindView(R.id.tv_task_type)
    TextView tvTaskType;
    @BindView(R.id.tv_task_person)
    TextView tvTaskPerson;
    @BindView(R.id.tv_task_start_time)
    TextView tvTaskStartTime;
    @BindView(R.id.tv_sampling_point_count)
    TextView tvSamplingPointCount;
    @BindView(R.id.cb_all)
    ImageView cbAll;

    private TitleBarView mTitleBarView;
    private TaskDetailAdapter mTaskDetailAdapter;
    private List<Sampling> mSamplingList;
    private Project mProject;


    private boolean isSelectAll = false;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("采样任务");
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_task_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTask();//绑定视图
        initTaskFormData();
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            //请求网络数据  采样单和表单分类

        } else {
            initTabLayout();
        }
    }

    private void initTask() {
        mProject = DbHelpUtils.getDbProject(getIntent().
                getStringExtra(SamplingUtil.INTENT_PROJECT_ID));
        bindView();
    }

    /**
     * 绑定view
     */
    private void bindView() {
        if (RxDataTool.isNull(mProject)) {
            return;
        }
        mTitleBarView.setTitleMainText(mProject.getName() + "采样任务");
        tvTaskName.setText(mProject.getName());
        if (RxDataTool.isEmpty(mProject.getPlanBeginTime()) ||//任务计划开始时间
                RxDataTool.isEmpty(mProject.getPlanEndTime())) {//任务计划结束时间
            tvTaskTimeRange.setText("未设置采样计划");
        } else {
            String currentTime = RxDateTool.getDate(RxDateTool.DATE_FORMAT);
            String endTime = mProject.getPlanEndTime();
            int lastDays = RxDateTool.getLastDays(currentTime, endTime.split(" ")[0]);
            if (lastDays <= 1) {
                tvTaskTimeRange.setTextColor(getResources().getColor(R.color.text_color_ff0000));
            } else if (lastDays <= 3) {
                tvTaskTimeRange.setTextColor(getResources().getColor(R.color.text_color_ffbe00));
            } else {
                tvTaskTimeRange.setTextColor(getResources().getColor(R.color.text_color_333333));
            }
            tvTaskTimeRange.setText(mProject.getPlanBeginTime().split(" ")[0].replace("-",
                    "/") + "~" + mProject.getPlanEndTime().split(" ")[0].replace("-", "/"));
        }

        StringBuilder users = new StringBuilder();
        List<String> userIds = mProject.getSamplingUser();
        if (!RxDataTool.isEmpty(userIds)) {
            List<User> userList = DbHelpUtils.getUserList(userIds);
            if (!RxDataTool.isEmpty(userList)) {
                for (User user : userList) {
                    users.append(user.getName() + ",");
                }
            }
        }

        StringBuilder monItems = new StringBuilder();//项目
        StringBuilder points = new StringBuilder();//点位

        List<ProjectDetial> projectDetials = DbHelpUtils.getProjectDetialList(mProject.getId());
        if (!RxDataTool.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (!monItems.toString().contains(projectDetial.getMonItemName())) {
                    monItems.append(projectDetial.getMonItemName() + ",");
                }
                if (!points.toString().contains(projectDetial.getAddress())) {
                    points.append(projectDetial.getAddress() + ",");
                }
            }
        }

        if (users.lastIndexOf(",") > 0) {
            users.deleteCharAt(users.lastIndexOf(","));
        }
        if (monItems.lastIndexOf(",") > 0) {
            monItems.deleteCharAt(monItems.lastIndexOf(","));
        }
        if (points.lastIndexOf(",") > 0) {
            points.deleteCharAt(points.lastIndexOf(","));
        }

        tvTaskNum.setText("任务编号:" + mProject.getProjectNo());
        tvTaskPoint.setText("点位:" + points.toString());
        tvTaskProjectNum.setText("项目:" + monItems.toString());
        tvTaskType.setText("样品性质:" + mProject.getMonType());
        tvTaskPerson.setText("人员：" + users.toString());
        tvTaskStartTime.setText("下达:" +
                mProject.getAssignDate().split(" ")[0].replace("-", "/"));
        //采样方案个数
        if (RxDataTool.isEmpty(points.toString())) {
            tvSamplingPointCount.setText("共" + 0 + "个");
        } else if (points.toString().contains(",")) {
            tvSamplingPointCount.setText("共" + points.toString().split(",").length + "个");
        } else {
            tvSamplingPointCount.setText("共" + 1 + "个");
        }

    }

    @Override
    public void showLoading() {
        showLoadingDialog("数据提交中...", false);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }


    @Override
    public void handleMessage(@NonNull Message message) {
        Preconditions.checkNotNull(message);

    }

    /**
     * 初始化tab数据
     */
    private void initTabLayout() {
        List<Form> formList = DbHelpUtils.getFormList();
        if (!RxDataTool.isEmpty(formList)) {
            for (Form form : formList) {
                tabLayout.addTab(tabLayout.newTab().setText(form.getTagName()));
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isSelectAll) {
                    cbAll.setImageResource(R.mipmap.ic_cb_nor);
                    //updateSamplingAllStatus(false);
                    isSelectAll = false;
                }
                String mTagId = formList.get(tab.getPosition()).getTagId();
                getSampling(mTagId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        String mTagId = formList.get(0).getTagId();//默认点击第一个
        getSampling(mTagId);
        initTabLayoutView();
    }

    /**
     * 设置TabLayout宽度
     */
    private void initTabLayoutView() {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                int dp10 = getResources().getDimensionPixelSize(R.dimen.dp_12);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);

                    TextView mTextView = (TextView) mTextViewField.get(tabView);

                    tabView.setPadding(0, 0, 0, 0);

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }
                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width + getResources().getDimensionPixelSize(R.dimen.dp_8);
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * 初始化Tab数据
     */
    private void initTaskFormData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });
        if (mSamplingList == null) mSamplingList = new ArrayList<>();
        mTaskDetailAdapter = new TaskDetailAdapter(mSamplingList, mContext,
                new TaskDetailAdapter.OnSamplingListener() {
                    @Override
                    public void onSelected(View view, int position) {
                        updateSamplingStatus(position);
                    }

                    @Override
                    public void onClick(View view, int position) {
                        Sampling sampling = mSamplingList.get(position);
                        startSampleActivity(sampling.getFormPath(), sampling.getId(), false);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        showDeleteDialog(position);
                    }

                    @Override
                    public void onUpload(View view, int position) {
                        if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Upload_Num)) {
                            showNoPermissionDialog("才能进行表单上传。", UserInfoAppRight.APP_Permission_Sampling_Upload_Name);
                            return;
                        }
                        SamplingUtil.unloadSample(mSamplingList.get(position));
                    }
                });
        recyclerview.setAdapter(mTaskDetailAdapter);
    }

    /**
     * 跳转到采样单界面
     *
     * @param formPath    采样单路径
     * @param id          采样单id或者新建样品id
     * @param isNewCreate 是否新建采样单
     */
    private void startSampleActivity(String formPath, String id, boolean isNewCreate) {
        Intent intent = new Intent();
        switch (formPath) {
//            case SamplingUtil.PATH_PRECIPITATION:
//                //降水采样及样品交接记录（新都）
//                intent.setClass(mContext, PrecipitationActivity.class);
//                break;
//            case SamplingUtil.PATH_WASTE_WATER:
//                //水和废水样品采集与交接记录（新都）
//                intent.setClass(mContext, WastewaterActivity.class);
//                break;
            case SamplingUtil.PATH_INSTRUMENTAL:
                //现场监测仪器法
                intent.setClass(mContext, InstrumentalActivity.class);
                break;
//            case SamplingUtil.PATH_NOISE_FACTORY://工业企业厂界噪声监测记录
//                intent.setClass(mContext, NoiseFactoryActivity.class);
//                break;
            default:
                ArtUtils.makeText(mContext, "功能开发中");
                return;
        }

        if (isNewCreate) {
            intent.putExtra(SamplingUtil.INTENT_FORM_SELECT_ID, id);
        } else {
            intent.putExtra(SamplingUtil.INTENT_SAMPLE_ID, id);
        }
        intent.putExtra(SamplingUtil.INTENT_PROJECT_ID, mProject.getId());
        intent.putExtra(SamplingUtil.INTENT_IS_NEW_CREATE, isNewCreate);
        ArtUtils.startActivity(intent);
    }


    @OnClick({R.id.btn_sampling_point, R.id.btn_add_sampling, R.id.btn_submit, R.id.cb_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sampling_point:
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_See_Num)) {
                    Intent intent = new Intent(this, PointActivity.class);
                    intent.putExtra(SamplingUtil.INTENT_PROJECT_ID, mProject.getId());
                    startActivity(intent);
                } else {
                    showNoPermissionDialog("才能进行采样方案查看。", UserInfoAppRight.APP_Permission_Plan_See_Name);
                }
                showMessage("待开发");
                break;
            case R.id.btn_add_sampling:
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Add_Num)) {
                    TagsUtils.showAddDialog(mContext, new TagsUtils.FormSelectAdapterOnItemClick() {
                        @Override
                        public void onItemClick(FormSelect formSelect) {
                            startSampleActivity(formSelect.getPath(), formSelect.getFormId(), true);
                        }
                    });
                } else {
                    showNoPermissionDialog("才能进行表单新增。", UserInfoAppRight.APP_Permission_Sampling_Add_Name);
                }

                break;
            case R.id.btn_submit:
                showMessage("待开发");
                break;
            case R.id.cb_all:
                if (!isSelectAll) {
                    cbAll.setImageResource(R.mipmap.ic_cb_checked);
                    updateSamplingAllStatus(!isSelectAll);
                    isSelectAll = true;
                } else {
                    cbAll.setImageResource(R.mipmap.ic_cb_nor);
                    updateSamplingAllStatus(!isSelectAll);
                    isSelectAll = false;
                }
                break;
        }
    }


    /**
     * 查找采样单
     *
     * @param tagId
     */
    private void getSampling(String tagId) {
        List<Sampling> samplings = DbHelpUtils.getDbSamplesByTagId(mProject.getId(), tagId);
        mSamplingList.clear();
        if (!RxDataTool.isEmpty(samplings)) {
            mSamplingList.addAll(samplings);
        }
        if (mTaskDetailAdapter != null)
            mTaskDetailAdapter.refreshInfos(mSamplingList);
    }


    /**
     * 更新sampling选中状态
     *
     * @param position
     */
    private void updateSamplingStatus(int position) {
        if (mSamplingList.get(position).isSelected()) {
            mSamplingList.get(position).setSelected(false);
        } else {
            mSamplingList.get(position).setSelected(true);
        }
        mTaskDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 更新所有sampling选中状态
     *
     * @param isSelectAll
     */
    private void updateSamplingAllStatus(boolean isSelectAll) {
        for (Sampling sampling : mSamplingList) {
            if (isSelectAll) {
                if (sampling.getStatus() == 0 || sampling.getStatus() == 4 || sampling.getStatus() == 9) {
                    sampling.setSelected(isSelectAll);
                } else {
                    sampling.setSelected(!isSelectAll);
                }
            } else {
                sampling.setSelected(isSelectAll);
            }
        }
        mTaskDetailAdapter.notifyDataSetChanged();
    }

    @Subscriber(tag = EventBusTags.TAG_PROGRAM_MODIFY)
    private void updateData(boolean isModified) {
        mProject = DbHelpUtils.getDbProject(mProject.getId());
        bindView();
    }

    @Subscriber(tag = EventBusTags.TAG_SAMPLING_UPDATE)
    private void updateSamplingData(boolean isModified) {
        //  getSampling(mTagId);
    }


    private void showDeleteDialog(int position) {
        String title = "删除采样单";
        String msg = "您确定删除采样单？删除后数据将丢失，无法找回";
        String pBtnStr = "确定";
        String nBtnStr = "取消";
        showDialog(title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


    /**
     * 提交采样单数据冲突处理
     */
    private void commitSamplingDataConflictOperate() {
        String title = "数据选择";
        String msg = "采样单数据同步中止，服务器存在数据不一致，请选择数据标准";
        String pBtnStr = "移动端数据";
        String nBtnStr = "服务端数据";
        showDialog(title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 使用移动端数据覆盖服务端数据提示
     */
    private void appDataChooseOperate() {
        String title = "确认选择";
        String msg = "您确定使用当前移动端表单数据覆盖服务端表单数据吗？";
        String pBtnStr = "确认";
        String nBtnStr = "取消";

        showDialog(title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 使用服务端数据覆盖移动端数据提示
     */
    private void serverDataChooseOperate() {
        String title = "确认选择";
        String msg = "您确定使用服务端表单数据覆盖当前移动端表单数据吗？移动端表单数据将丢失";
        String pBtnStr = "确认";
        String nBtnStr = "取消";

        showDialog(title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //   //去服务器拉取采样单信息，覆盖到本地
            }
        });
    }


}
