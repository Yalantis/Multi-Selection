package com.yalantis.multiselection.lib

import android.support.v4.view.ViewPager
import android.view.View
import com.yalantis.multiselection.lib.util.mix
import com.yalantis.multiselection.lib.util.setScaleXY
import com.yalantis.multiselection.lib.util.smoothstep

class ZoomPageTransformer(val pageWidth: Float) : ViewPager.PageTransformer {

    companion object {
        const val MIN_ZOOM = 0.8f
    }

    private val sidebarWidth: Float

    init {
        sidebarWidth = 1f - pageWidth
    }

    override fun transformPage(page: View, position: Float) {
        val scale = when {
            // left page is never scaled
            position <= 0 -> 1f
            else -> position.smoothstep(pageWidth, sidebarWidth).mix(1f, MIN_ZOOM)
        }

        page.pivotX = 0f
        page.setScaleXY(scale)
    }

}