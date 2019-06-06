package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;



import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.PointHolder;


/**
 * 主页tab
 */

public class PointAdapter extends DefaultAdapter<ProjectDetial> {

    private Context mContext;
    private boolean isCanEdit;
    private ItemAdapterOnClickListener listener;

    public PointAdapter(Context context, List<ProjectDetial> infos, boolean isCanEdit,
                        ItemAdapterOnClickListener listener) {
        super(infos,context);
        this.mContext = context;
        this.isCanEdit = isCanEdit;
        this.listener = listener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point;
    }

    @Override
    public BaseHolder<ProjectDetial> getHolder(View v, int viewType) {
        return new PointHolder(mContext, v, isCanEdit,listener);
    }

    public interface ItemAdapterOnClickListener {
        void onItemOnClick(EnvirPoint point);
    }
}
