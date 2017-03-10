package com.nanyi545.www.materialdemo.collapse_layout;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/7.
 *
 *
 *
 *
 * Update dynamically View sizes when layout_width or layout_height="wrap_content"

 One of the annoying flaws of Android UI is its inability to update the size of Views defined with "wrap_content", when we insert dynamically children views or when we modify their content.

 This class is a workaround that recomputes sizes and forces a new layout for those views.


 */
public class LayoutWrapContentUpdater {

    public static final String TAG = LayoutWrapContentUpdater.class.getName();


    /**
     * Does what a proper requestLayout() should do about layout_width or layout_height = "wrap_content"
     *
     * Warning: if the subTreeRoot itself has a "wrap_content" layout param, the size will be computed without boundaries maximum size.
     * 			If you do have limits, consider either passing the parent, or calling the method with the size parameters (View.MeasureSpec)
     *
     * @param subTreeRoot  root of the sub tree you want to recompute
     */
    public static final void wrapContentAgain( ViewGroup subTreeRoot )
    {
        wrapContentAgain( subTreeRoot, false, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED );
    }

    /** Same but allows re-layout of all views, not only those with "wrap_content". Necessary for "center", "right", "bottom",... */
    public static final void wrapContentAgain( ViewGroup subTreeRoot, boolean relayoutAllNodes )
    {
        wrapContentAgain( subTreeRoot, relayoutAllNodes, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED );
    }


    /**
     * Same as previous, but with given size in case subTreeRoot itself has layout_width or layout_height = "wrap_content"
     */
    public static void wrapContentAgain( ViewGroup subTreeRoot, boolean relayoutAllNodes,
                                         int subTreeRootWidthMeasureSpec, int subTreeRootHeightMeasureSpec  )
    {
        Log.d(TAG, "+++ LayoutWrapContentUpdater wrapContentAgain on subTreeRoot=["+ subTreeRoot +"], with w="
                + subTreeRootWidthMeasureSpec +" and h="+ subTreeRootHeightMeasureSpec );
        assert( "main".equals( Thread.currentThread().getName() ) );

        if (subTreeRoot == null)
            return;
        ViewGroup.LayoutParams layoutParams = subTreeRoot.getLayoutParams();

        // --- First, we force measure on the subTree
        int widthMeasureSpec 	= subTreeRootWidthMeasureSpec;
        // When LayoutParams.MATCH_PARENT and Width > 0, we apply measured width to avoid getting dimensions too big
        if ( layoutParams.width  != ViewGroup.LayoutParams.WRAP_CONTENT && subTreeRoot.getWidth() > 0 )
            widthMeasureSpec 	=  View.MeasureSpec.makeMeasureSpec( subTreeRoot.getWidth(), View.MeasureSpec.EXACTLY );

        int heightMeasureSpec 	= subTreeRootHeightMeasureSpec;

        // When LayoutParams.MATCH_PARENT and Height > 0, we apply measured height to avoid getting dimensions too big
        if ( layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT && subTreeRoot.getHeight() > 0 )
            heightMeasureSpec 	=  View.MeasureSpec.makeMeasureSpec( subTreeRoot.getHeight(), View.MeasureSpec.EXACTLY );
        // This measure recursively the whole sub-tree
        subTreeRoot.measure( widthMeasureSpec, heightMeasureSpec );

        // --- Then recurse on all children to correct the sizes
        recurseWrapContent( subTreeRoot, relayoutAllNodes );

        // --- RequestLayout to finish properly
        subTreeRoot.requestLayout();
        return;
    }


    /**
     * Internal method to recurse on view tree. Tag you View nodes in XML layouts to read the logs more easily
     */
    private static void recurseWrapContent( View nodeView, boolean relayoutAllNodes )
    {
        // Does not recurse when visibility GONE
        if ( nodeView.getVisibility() == View.GONE ) {
            // nodeView.layout( nodeView.getLeft(), nodeView.getTop(), 0, 0 );		// No need
            return;
        }

        ViewGroup.LayoutParams layoutParams = nodeView.getLayoutParams();
        boolean isWrapWidth  = ( layoutParams.width  == ViewGroup.LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;
        boolean isWrapHeight = ( layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ) || relayoutAllNodes;

        if ( isWrapWidth || isWrapHeight ) {

            boolean changed = false;
            int right  = nodeView.getRight();
            int bottom = nodeView.getBottom();

            if ( isWrapWidth  && nodeView.getMeasuredWidth() > 0 ) {
                right = nodeView.getLeft() + nodeView.getMeasuredWidth();
                changed = true;
                Log.v(TAG, "+++ LayoutWrapContentUpdater recurseWrapContent set Width to "+ nodeView.getMeasuredWidth() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }
            if ( isWrapHeight && nodeView.getMeasuredHeight() > 0 ) {
                bottom = nodeView.getTop() + nodeView.getMeasuredHeight();
                changed = true;
                Log.v(TAG, "+++ LayoutWrapContentUpdater recurseWrapContent set Height to "+ nodeView.getMeasuredHeight() +" of node Tag="+ nodeView.getTag() +" ["+ nodeView +"]");
            }

            if (changed) {
                nodeView.layout( nodeView.getLeft(), nodeView.getTop(), right, bottom );
                // FIXME: Adjust left & top position when gravity = "center" / "bottom" / "right"
            }
        }

        // --- Recurse
        if ( nodeView instanceof ViewGroup ) {
            ViewGroup nodeGroup = (ViewGroup)nodeView;
            for (int i = 0; i < nodeGroup.getChildCount(); i++) {
                recurseWrapContent( nodeGroup.getChildAt(i), relayoutAllNodes );
            }
        }
        return;
    }

    // End of class

}
