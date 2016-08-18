package com.yalantis.multiselection.lib.adapter

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
abstract class BaseAdapter<I, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    val hiddenItems = mutableSetOf<I>()

    abstract fun add(item: I, hide: Boolean = false): Int

    abstract fun removeItemAt(position: Int): I

    abstract fun indexOf(item: I): Int

    abstract fun getItemAt(index: Int): I

    abstract fun addAll(items: List<I>)

    fun showItem(item: I) {
        if (hiddenItems.remove(item)) {
            notifyItemChanged(indexOf(item))
        }
        notifyDataSetChanged()
    }

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.visibility = when {
            hiddenItems.contains(getItemAt(position)) -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

}