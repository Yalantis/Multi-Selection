package com.yalantis.multiselection.lib.callbacks

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
interface ListInterface<I> {
    fun removeItemAt(position: Int): I
    fun indexOf(item: I): Int
    fun add(item: I): Int
}