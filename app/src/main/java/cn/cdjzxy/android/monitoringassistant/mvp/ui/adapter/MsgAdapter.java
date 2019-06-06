package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.MsgHolder;


/**
 * 主页tab
 */

public class MsgAdapter extends DefaultAdapter<Msg> {


    public MsgAdapter(List<Msg> infos, Context context) {
        super(infos, context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_msg;
    }

    @Override
    public BaseHolder<Msg> getHolder(View v, int viewType) {
        return new MsgHolder(v);
    }
}
