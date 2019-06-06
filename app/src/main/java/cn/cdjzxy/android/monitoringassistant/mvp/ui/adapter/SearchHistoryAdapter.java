package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.SearchHistoryHolder;


/**
 * 主页tab
 */

public class SearchHistoryAdapter extends DefaultAdapter<String> {

    private OnDeleteClickListener mOnDeleteClickListener;

    public SearchHistoryAdapter(List<String> infos, Context context, OnDeleteClickListener onDeleteClickListener) {
        super(infos,context);
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_search_history;
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new SearchHistoryHolder(v, mOnDeleteClickListener);
    }


    public interface OnDeleteClickListener {
        void onDelete(int position);
    }
}
