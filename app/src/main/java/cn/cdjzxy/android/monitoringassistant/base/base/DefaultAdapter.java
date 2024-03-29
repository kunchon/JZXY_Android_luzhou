/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.cdjzxy.android.monitoringassistant.base.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ================================================
 * 基类 {@link RecyclerView.Adapter} ,如果需要实现非常复杂的 {@link RecyclerView} ,请尽量使用其他优秀的三方库
 * <p>
 * Created by jess on 2015/11/27.
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    protected List<T> mInfos;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private BaseHolder<T> mHolder;
    public Context mContext;

    public DefaultAdapter(List<T> infos, Context context) {
        super();
        this.mInfos = infos;
        this.mContext = context;
    }
    public DefaultAdapter(Context context){
        this.mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    /**
     * 创建 {@link BaseHolder}
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        mHolder = getHolder(view, viewType);
        mHolder.setOnItemClickListener(new BaseHolder.OnViewClickListener() {//设置Item点击事件
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, viewType, mInfos.get(position), position);
                }
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        holder.setData(mInfos.get(position), position,mContext);
    }


    /**
     * 返回数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mInfos.size();
    }


    /**
     * 获取全部数据
     *
     * @return
     */
    public List<T> getInfos() {
        return mInfos;
    }

    /**
     * 获得某个 {@code position} 上的 {@code item} 的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mInfos == null ? null : mInfos.get(position);
    }

    /**
     * 刷新数据源
     *
     * @param newInfos
     */
    public void refreshInfos(List<T> newInfos) {
        mInfos = newInfos;
        notifyDataSetChanged();
    }

    /**
     * 刷新指定位置数据源
     *
     * @param newInfos
     * @param size
     */
    public void refreshInfos(List<T> newInfos, int size) {
        if (size == 0) {
            mInfos = newInfos;
            notifyDataSetChanged();
        } else {
            int oldSize = getItemCount();
            mInfos = newInfos;
            notifyItemRangeInserted(oldSize, newInfos.size());
        }
    }

    /**
     * 删除指定数据
     *
     * @param t
     * @return
     */
    public boolean remove(T t) {
        int pos = mInfos.indexOf(t);
        if (pos >= 0) {
            mInfos.remove(t);
            notifyItemRemoved(pos);
            return true;
        }
        return false;
    }

    /**
     * 添加指定数据
     *
     * @param t
     */
    public void add(T t) {
        int size = mInfos.size();
        mInfos.add(t);
        notifyItemInserted(size - 1);
    }

    /**
     * 添加所有数据
     *
     * @param newInfos
     */
    public void addAll(List<T> newInfos) {
        int size = mInfos.size();
        mInfos.addAll(newInfos);
        notifyItemRangeInserted(size - 1, newInfos.size());
    }

    /**
     * 添加数据到指定位置
     *
     * @param pos
     * @param item
     */
    public void add(int pos, T item) {
        mInfos.add(pos, item);
        notifyItemInserted(pos);
    }

    /**
     * 清空指定位置数据
     *
     * @param pos
     */
    public void delete(int pos) {
        mInfos.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * 清空数据源
     */
    public void deleteAll() {
        mInfos.clear();
        notifyDataSetChanged();
    }


    /**
     * 让子类实现用以提供 {@link BaseHolder}
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseHolder<T> getHolder(View v, int viewType);

    /**
     * 提供用于 {@code item} 布局的 {@code layoutId}
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);


    /**
     * 遍历所有 {@link BaseHolder}, 释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null)
            return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).onRelease();
            }
        }
    }


    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, int viewType, T data, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
