package com.yalantis.multiselection.lib

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.yalantis.multiselection.R
import com.yalantis.multiselection.lib.adapter.BaseLeftAdapter
import com.yalantis.multiselection.lib.adapter.BaseRightAdapter

/**
 * Created by Artem Kholodnyi on 9/17/16.
 */
class MultiSelectBuilder<I : Comparable<I>>(val clazz: Class<I>) {

    private lateinit var context: Context
    private lateinit var mountPoint: ViewGroup
    private lateinit var multiSelectView: MultiSelectImpl<I>
    private lateinit var leftAdapter: BaseLeftAdapter<I, out RecyclerView.ViewHolder>
    private lateinit var rightAdapter: BaseRightAdapter<I, out RecyclerView.ViewHolder>
    private var sidebarWidth: Float = 0f

    fun withContext(context: Context): MultiSelectBuilder<I> {
        this.context = context
        return this
    }

    fun mountOn(mountPoint: ViewGroup): MultiSelectBuilder<I> {
        this.mountPoint = mountPoint
        return this
    }


    fun withSidebarWidth(sidebarWidthDp: Float): MultiSelectBuilder<I> {
        this.sidebarWidth = sidebarWidthDp
        return this
    }

    fun withLeftAdapter(adapter: BaseLeftAdapter<I, out RecyclerView.ViewHolder>): MultiSelectBuilder<I> {
        this.leftAdapter = adapter
        return this
    }


    fun withRightAdapter(adapter: BaseRightAdapter<I, out RecyclerView.ViewHolder>): MultiSelectBuilder<I> {
        this.rightAdapter = adapter
        return this
    }

    fun build(): MultiSelect<I> {
        this.multiSelectView = MultiSelectImpl<I>(context, mountPoint)
        this.multiSelectView.id = R.id.yal_ms_multiselect
        this.multiSelectView.setSidebarWidthDp(this.sidebarWidth)
        this.multiSelectView.leftAdapter = this.leftAdapter
        this.multiSelectView.rightAdapter = this.rightAdapter
        this.mountPoint.addView(this.multiSelectView)
        return this.multiSelectView
    }

}