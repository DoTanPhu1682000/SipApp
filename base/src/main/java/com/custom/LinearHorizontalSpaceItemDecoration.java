package com.custom;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.recyclerview.widget.RecyclerView;

public class LinearHorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public LinearHorizontalSpaceItemDecoration(@IntRange(from = 0) int margin) {
        this.margin = margin;
    }

    /**
     * Set different margins for the items inside the recyclerView: no top margin for the first row
     * and no left margin for the first column.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //set bottom margin to all
        outRect.right = margin;

    }
}
