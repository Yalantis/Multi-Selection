package com.yalantis.multiselection.lib.adapter

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import com.yalantis.multiselection.lib.callbacks.SortedListCallback

/**
 * Created by Artem Kholodnyi on 9/3/16.
 */
abstract class BaseLeftAdapter<I : Comparable<I>, VH : RecyclerView.ViewHolder>
: BaseAdapter<I, VH> {

    lateinit var items: SortedList<I>

    private constructor() : super()

    constructor(klass: Class<I>) : super() {
        items = SortedList(klass, object: SortedListCallback<I>() {})
    }

    override fun getItemCount(): Int = items.size()

    override fun indexOf(item: I): Int = items.indexOf(item)

    override fun removeItemAt(position: Int): I = items.removeItemAt(position).apply {
        notifyItemRemoved(position)
    }

    override fun add(item: I, hide: Boolean): Int {
        return items.add(item).apply {
            notifyItemInserted(this)
            if (hide) {
                hiddenItems += item
            }
        }
    }

    override fun addAll(list: List<I>) {
        items.addAll(list)
    }

    override fun getItemAt(index: Int): I = items.get(index)
}