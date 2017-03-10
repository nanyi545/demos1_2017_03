package com.webcon.sus.utils;

import android.view.View;

/**
 * RecyclerView列表项点击监听接口
 * @author m
 */
public interface OnRVItemClickListener {
    /**
     * 点击事件
     * @param view 组件
     * @param position 列表项位置
     */
    void onItemClick(View view , int position);

    /**
     * 长点击事件
     * @param view 组件
     * @param position 列表项位置
     */
    void onItemLongClick(View view, int position);
}
