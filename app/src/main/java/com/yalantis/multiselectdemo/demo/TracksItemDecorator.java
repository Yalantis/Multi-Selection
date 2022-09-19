package com.yalantis.multiselectdemo.demo;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
public class TracksItemDecorator extends RecyclerView.ItemDecoration {

    private int size;

    public TracksItemDecorator(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = size;
        outRect.top = 0;
        outRect.left = 0;
        outRect.right = 0;
    }
}
