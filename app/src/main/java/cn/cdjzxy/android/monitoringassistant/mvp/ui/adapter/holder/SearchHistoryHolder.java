package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.BindView;
import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.android.monitoringassistant.utils.rx.RxDataTool;


/**
 * 主页tab
 */

public class SearchHistoryHolder extends BaseHolder<String> {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;

    private SearchHistoryAdapter.OnDeleteClickListener mOnDeleteClickListener;


    public SearchHistoryHolder(View itemView, SearchHistoryAdapter.OnDeleteClickListener onDeleteClickListener) {
        super(itemView);
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void setData(String data, final int position, Context context) {
        mTvName.setText(data);
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RxDataTool.isNull(mOnDeleteClickListener)) {
                    mOnDeleteClickListener.onDelete(position);
                }
            }
        });
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mIvDelete = null;
    }
}
