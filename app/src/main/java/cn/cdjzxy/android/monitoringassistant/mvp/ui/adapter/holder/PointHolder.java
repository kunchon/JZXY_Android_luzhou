package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PointAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.PointItemAdapter;
import cn.cdjzxy.android.monitoringassistant.user.UserInfoHelper;
import cn.cdjzxy.android.monitoringassistant.utils.ArtUtils;
import cn.cdjzxy.android.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


/**
 * 主页tab
 */

public class PointHolder extends BaseHolder<ProjectDetial> {

    @BindView(R.id.tv_project_name)
    TextView mTvName;
    @BindView(R.id.tv_project_time_range)
    TextView mTvTime;
    @BindView(R.id.tv_project_members)
    TextView mTvMember;
    @BindView(R.id.tv_edit_plan)
    TextView mTvEdit;

    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerViewItem;

    private Context mContext;

    private PointItemAdapter mPointItemAdapter;

    private PointAdapter.ItemAdapterOnClickListener listener;

    private boolean isCanEdit;

    public PointHolder(Context context, View itemView, boolean isCanEdit, PointAdapter.ItemAdapterOnClickListener listener) {
        super(itemView);
        this.mContext = context;
        this.isCanEdit = isCanEdit;
        this.listener = listener;
    }

    @Override
    public void setData(ProjectDetial data, int position, Context context) {
        if (!isCanEdit) {
            mTvEdit.setVisibility(View.GONE);
        } else {
            mTvEdit.setVisibility(View.VISIBLE);
        }
        if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Modify_Num)) {
            mTvEdit.setText("查看方案");
        } else {
            mTvEdit.setText("修改方案");
        }

        mTvName.setText(data.getTagName());
        mTvTime.setText(data.getDays() + "天" + data.getPeriod() + "次");
        mTvMember.setText(data.getMonItemName());
        initPointItemData(data.getAddressId());


    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mTvTime = null;
        this.mTvMember = null;
        this.mRecyclerViewItem = null;
    }


    /**
     * 初始化Tab数据
     */
    private void initPointItemData(String pointId) {
        if (RxDataTool.isEmpty(pointId)) return;
        List<EnvirPoint> envirPoints = new ArrayList<>();

        if (pointId.contains(",")) {
            envirPoints = DbHelpUtils.getEnvirPointList(RxDataTool.strToList(pointId));
        } else {
            envirPoints.add(DbHelpUtils.getEnvirPointList(pointId));
        }

        if (!RxDataTool.isEmpty(envirPoints)) {
            ArtUtils.configRecyclerView(mRecyclerViewItem, new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {//设置RecyclerView不可滑动
                    return false;
                }
            });
            mPointItemAdapter = new PointItemAdapter(envirPoints, mContext);
            mRecyclerViewItem.setAdapter(mPointItemAdapter);
            mPointItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int viewType, Object data, int position) {
                    EnvirPoint pointSelect = (EnvirPoint) data;
                    if (listener != null) {
                        listener.onItemOnClick(pointSelect);
                    }
                }
            });
        }

    }


}
