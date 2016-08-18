package com.yalantis.multiselection.lib.adapter

import android.support.v7.widget.RecyclerView

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */

abstract class BaseRightAdapter<I, VH : RecyclerView.ViewHolder> : BaseAdapter<I, VH>() {

    var items: MutableList<I> = mutableListOf()

    override fun getItemCount(): Int = items.count()

    override fun getItemAt(index: Int): I = items[index]

    override fun indexOf(item: I): Int = items.indexOf(item)

    override fun removeItemAt(position: Int): I {
        return items.removeAt(position).apply {
            notifyItemRemoved(position)
        }
    }

    override fun add(item: I, hide: Boolean): Int {
        if (hide) {
            hiddenItems += item
        }
        items.add(item)
        val index = items.count() - 1
        notifyItemInserted(index)
        return index
    }


    override fun addAll(items: List<I>) {
        this.items.addAll(items)
    }
}