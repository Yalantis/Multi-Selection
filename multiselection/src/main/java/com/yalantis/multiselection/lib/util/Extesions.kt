package com.yalantis.multiselection.lib.util

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import kotlin.jvm.internal.Lambda

/**
 * Created by Artem Kholodnyi on 8/23/16.
 */
fun View.removeFromParent() {
    val parent = this.parent
    if (parent is ViewGroup) {
        parent.removeView(this)
    }
}

inline fun <T: View> T.afterMeasured(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredHeight > 0 && measuredWidth > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}

fun View.setScaleXY(scale: Float) {
    scaleX = scale
    scaleY = scale
}

fun View.getLocationOnScreen(): IntArray {
    val loc = intArrayOf(0, 0)
    this.getLocationOnScreen(loc)
    return loc
}

// Please refer to GLSL docs to understand these functions [[

fun Float.clamp(floor: Float, ceil: Float): Float = Math.min(ceil, Math.max(floor, this))

fun Float.mix(x: Float, y: Float): Float = x * this + y * (1f - this)

fun Float.smoothstep(edge0: Float, edge1: Float): Float {
    val t = ((this - edge0) / (edge1 - edge0)).clamp(0f, 1f)
    return t * t * (3f - 2f * t)
}

// ]]
