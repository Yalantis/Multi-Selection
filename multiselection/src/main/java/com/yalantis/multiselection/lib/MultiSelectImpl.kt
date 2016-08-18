package com.yalantis.multiselection.lib

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.NonNull
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import com.yalantis.multiselection.R
import com.yalantis.multiselection.lib.adapter.BaseAdapter
import com.yalantis.multiselection.lib.adapter.BaseLeftAdapter
import com.yalantis.multiselection.lib.adapter.BaseRightAdapter
import com.yalantis.multiselection.lib.adapter.ViewPagerAdapter
import com.yalantis.multiselection.lib.util.getLocationOnScreen
import com.yalantis.multiselection.lib.util.removeFromParent
import java.io.Serializable

/**
 * Created by Artem Kholodnyi on 8/21/16.
 */

internal class MultiSelectImpl<I : Comparable<I>>(myContext: Context,
                                                  val parent: ViewGroup)
    : FrameLayout(myContext), MultiSelect<I> {

    init {
        isSaveEnabled = true
    }


    private val STATE_SUPER = "state super"
    private val STATE_SELECTED = "state selected"
    private val STATE_LEFT_POS = "state left pos"
    private val STATE_RIGHT_POS = "state right pos"

    private val location = intArrayOf(0, 0)

    private var pageWidth = 0f


    override val selectedItems: List<I>?
        @NonNull
        get() {
            @Suppress("UNCHECKED_CAST")
            return (recyclerRight.adapter as BaseRightAdapter<I, *>).items
        }

    override val recyclerLeft: RecyclerView by lazy {
        pagesAdapter.pageLeft.findViewById(R.id.yal_ms_recycler) as RecyclerView
    }

    override val recyclerRight: RecyclerView by lazy {
        pagesAdapter.pageRight.findViewById(R.id.yal_ms_recycler) as RecyclerView
    }

    override var leftAdapter: BaseLeftAdapter<I, out RecyclerView.ViewHolder>?
        get() {
            @Suppress("UNCHECKED_CAST")
            return recyclerLeft.adapter as BaseLeftAdapter<I, out RecyclerView.ViewHolder>?
        }
        set(value) {
            recyclerLeft.adapter = value
        }

    override var rightAdapter: BaseRightAdapter<I, out RecyclerView.ViewHolder>?
        get() {
            @Suppress("UNCHECKED_CAST")
            return recyclerRight.adapter as BaseRightAdapter<I, out RecyclerView.ViewHolder>?
        }
        set(value) {
            recyclerRight.adapter = value
        }

    private val viewPager: MultiSelectViewPager
    private lateinit var pagesAdapter: ViewPagerAdapter

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.yal_ms_multiselect, parent, true)
        viewPager = view.findViewById(R.id.view_pager) as MultiSelectViewPager
    }

    override fun setSidebarWidthDp(iconWidthDp: Float) {
        val pageWidth = calcPageWidth(iconWidthDp)
        pagesAdapter = ViewPagerAdapter(pageWidth = pageWidth)
        viewPager.adapter = pagesAdapter
        viewPager.onClickCallback = { x, y ->
            pagesAdapter.pageRight.getLocationInWindow(location)

            when {
                viewPager.currentItem == 0 && x > location[0] -> {
                    // left pane is opened and clicked on the right one
                    viewPager.currentItem = 1
                    true
                }
                viewPager.currentItem == 1 && x < location[0] -> {
                    // right pane is opened and clicked on the left one
                    viewPager.currentItem = 0

                    true
                }
                else -> false
            }
        }
        setUpViews()
    }

    /**
     * @param sidebarWidthDp
     * @return page width in [0; 1]
     */
    private fun calcPageWidth(sidebarWidthDp: Float): Float {
        val metrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)

        val density = context.resources.displayMetrics.density
        val dpWidth = metrics.widthPixels / density

        return 1f - sidebarWidthDp / dpWidth
    }

    private fun setUpViews() {
        val inflater = LayoutInflater.from(context)

        pagesAdapter.pageLeft = inflater.inflate(R.layout.yal_ms_page_left, viewPager, false).apply {
            (findViewById(R.id.yal_ms_recycler) as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = MultiSelectItemAnimator()
            }
        }

        pagesAdapter.pageRight = inflater.inflate(R.layout.yal_ms_page_right, viewPager, false).apply {
            (findViewById(R.id.yal_ms_recycler) as RecyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                itemAnimator = MultiSelectItemAnimator()
            }
        }
    }

    override fun select(position: Int) = animate(recyclerLeft, recyclerRight, position)

    override fun deselect(position: Int) = animate(recyclerRight, recyclerLeft, position)

    private fun animate(sourceRecycler: RecyclerView, targetRecycler: RecyclerView, position: Int) {
        val view = sourceRecycler.layoutManager.findViewByPosition(position) ?: return

        view.isClickable = false

        val initial = view.getLocationOnScreen()

        sourceRecycler.layoutManager.removeViewAt(position)

        @Suppress("UNCHECKED_CAST")
        val removedItem: I = (sourceRecycler.adapter as BaseAdapter<I, *>).removeItemAt(position)

        val width = view.width
        view.removeFromParent()
        parent.addView(view)
        view.layoutParams = view.layoutParams.apply {
            this.width = width
        }

        val container = sourceRecycler.getLocationOnScreen()

        view.translationX = (initial[0].toFloat())
        view.translationY = (initial[1] - container[1]).toFloat()

        @Suppress("UNCHECKED_CAST")
        val newPos = (targetRecycler.adapter as BaseAdapter<I, *>).add(removedItem, hide = true)
        val targetCoordinates = getTarget(targetRecycler, newPos)


        val targetX = (targetCoordinates[0] - initial[0]).toFloat()
        val targetY = (targetCoordinates[1] - initial[1]).toFloat()
        val duration = calcDuration(targetX, targetY)
        animateAlpha(removedItem, targetRecycler, view, duration)
        animateTranslation(view, deltaX = targetX, deltaY = targetY, duration = duration)
    }

    private fun calcDuration(targetX: Float, targetY: Float): Long {
        return Math.sqrt((targetX * targetX + targetY * targetY).toDouble()).times(0.7f).toLong()
    }

    private fun getTarget(targetRecycler: RecyclerView, index: Int): IntArray {
        val prev = Math.max(0, index - 0)
        var targetView: View? = targetRecycler.findViewHolderForAdapterPosition(prev)?.itemView
        if (targetView == null) {
            targetView = targetRecycler.findViewHolderForAdapterPosition(prev - 1)?.itemView
            if (targetView != null) {
                val targetCoordinates = targetView.getLocationOnScreen()
                targetCoordinates[1] += targetView.height
                return targetCoordinates
            }
        }

        if (targetView == null) {
            val targetCoordinates = targetRecycler.getLocationOnScreen()
            if (targetRecycler.childCount != 0) {
                // target view is not visible because recycler view is filled
                targetCoordinates[1] += targetRecycler.height
            }
            return targetCoordinates
        }

        return intArrayOf(0, 0)
    }

    override fun showSelectedPage() {
        viewPager.currentItem = 1
    }

    override fun showNotSelectedPage() {
        viewPager.currentItem = 0
    }

    internal fun animateAlpha(removedItem: I, targetRecycler: RecyclerView, view: View, duration: Long) {
        ValueAnimator.ofFloat(1f, 0f).setDuration(duration).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                if (view is ViewGroup) {
                    (0..view.childCount - 1)
                            .map { view.getChildAt(it) }
                            .filter { it.id != R.id.yal_ms_avatar }
                            .forEach { it?.alpha = value }
                }
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) = Unit
                override fun onAnimationRepeat(p0: Animator?) = Unit
                override fun onAnimationEnd(p0: Animator?) = finallyDo()
                override fun onAnimationCancel(p0: Animator?) = finallyDo()

                val finallyDo = {
                    view.removeFromParent()
                    @Suppress("UNCHECKED_CAST")
                    (targetRecycler.adapter as BaseAdapter<I, out RecyclerView.ViewHolder>).showItem(removedItem)
                    Unit
                }
            })
        }.start()
    }

    internal fun animateTranslation(view: View, deltaX: Float, deltaY: Float, duration: Long) {
        view.animate().setDuration(duration)
                .setInterpolator(OvershootInterpolator(1.1f))
                .translationXBy(deltaX)
                .translationYBy(deltaY)
                .start()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(STATE_SUPER, superState)

            val selected = selectedItems
            if (selected is Serializable) {
                putSerializable(STATE_SELECTED, selected)
            }

            (recyclerLeft.layoutManager as? LinearLayoutManager)?.let {
                putInt(STATE_LEFT_POS, it.findFirstCompletelyVisibleItemPosition())
            }

            (recyclerRight.layoutManager as? LinearLayoutManager)?.let {
                putInt(STATE_RIGHT_POS, it.findFirstCompletelyVisibleItemPosition())
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable(STATE_SUPER))

            @Suppress("UNCHECKED_CAST")
            val selected = state.getSerializable(STATE_SELECTED) as? List<I>
            if (selected != null) {
                restoreState(selected)
            }

            val leftScrollPos = state.getInt(STATE_LEFT_POS, -1)
            if (leftScrollPos != -1) {
                recyclerLeft.layoutManager.scrollToPosition(leftScrollPos)
            }

            val rightScrollPos = state.getInt(STATE_RIGHT_POS, -1)
            if (rightScrollPos != -1) {
                recyclerRight.layoutManager.scrollToPosition(rightScrollPos)
            }
        }
    }

    private fun restoreState(selectedItems: List<I>) {
        selectedItems.forEach {
            val index = leftAdapter?.items?.indexOf(it)
            if (index != null && index > -1) {
                leftAdapter?.removeItemAt(index)
                rightAdapter?.add(it, hide = false)
            }
        }
    }

}