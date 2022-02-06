package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.ir.FeatureName
import com.andreikingsley.ggdsl.ir.PlotFeature

class AnimationFeature(
    var enable: Boolean = true,
    var threshold: Int = 2000,
    var duration: Int = 1000,
    var easing: AnimationEasing = AnimationEasing.CUBIC_OUT,
    var delay: Int = 0
): PlotFeature

val DATA_CHANGE_ANIMATION_FEATURE = FeatureName("DATA_CHANGE_ANIMATION_FEATURE")

class AnimationEasing(val name: String) {
    companion object {
        val LINEAR = AnimationEasing("linear")
        val CUBIC_OUT = AnimationEasing("cubicOut")
    }
}