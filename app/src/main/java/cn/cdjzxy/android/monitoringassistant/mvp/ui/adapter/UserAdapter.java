package cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

;

import java.util.List;

import cn.cdjzxy.android.monitoringassistant.R;
import cn.cdjzxy.android.monitoringassistant.base.base.BaseHolder;
import cn.cdjzxy.android.monitoringassistant.base.base.DefaultAdapter;
import cn.cdjzxy.android.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.android.monitoringassistant.mvp.ui.adapter.holder.UserHolder;


/**
 * 主页tab
 */

public class UserAdapter extends DefaultAdapter<User> {


    public UserAdapter(List<User> infos, Context context) {
        super(infos,context);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_project;
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserHolder(v);
    }
}
