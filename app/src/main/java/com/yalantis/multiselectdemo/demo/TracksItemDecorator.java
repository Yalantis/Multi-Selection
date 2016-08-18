package com.yalantis.multiselectdemo.demo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
public class TracksItemDecorator extends RecyclerView.ItemDecoration {

    private int size;

    public TracksItemDecorator(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = size;
        outRect.top = 0;
        outRect.left = 0;
        outRect.right = 0;
    }
}
