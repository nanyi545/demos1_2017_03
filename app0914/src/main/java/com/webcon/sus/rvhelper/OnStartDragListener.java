package com.webcon.sus.rvhelper;


import android.support.v7.widget.RecyclerView;

/**
 *  * Created by Administrator on 15-11-3.
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}

