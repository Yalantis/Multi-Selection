package com.yalantis.multiselection.lib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * Created by Artem Kholodnyi on 8/17/16.
 */
class ViewPagerAdapter(val pageWidth: Float) : PagerAdapter() {

    lateinit var pageLeft: View
    lateinit var pageRight: View

    override fun getPageWidth(position: Int): Float {
        return pageWidth // this should be configurable (dp)
    }

    override fun getCount(): Int = 2

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val pager = container as ViewPager
        val view = getView(position, pager)
        pager.addView(view)
        view.tag = position
        return view
    }

    private fun getView(position: Int, pager: ViewPager): View = when (position) {
        0 -> pageLeft
        1 -> pageRight
        else -> throw IllegalStateException()
    }

}


