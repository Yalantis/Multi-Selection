package com.yalantis.multiselection.lib.callbacks

import android.support.v7.util.SortedList.Callback

/**
 * Created by Artem Kholodnyi on 9/4/16.
 */
open class SortedListCallback<T : Comparable<T>> : Callback<T>() {

    override fun areItemsTheSame(item1: T, item2: T): Boolean {
        return item1 == item2
    }

    override fun compare(o1: T, o2: T): Int {
        return o1.compareTo(o2)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.equals(newItem)
    }

    override fun onChanged(position: Int, count: Int) {

    }

    override fun onRemoved(position: Int, count: Int) {

    }

    override fun onInserted(position: Int, count: Int) {

    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {

    }

}