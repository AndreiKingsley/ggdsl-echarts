package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.ir.feature.*

class AnimationFeature(
    var enable: Boolean = true,
    var threshold: Int = 2000,
    var duration: Int = 1000,
    var easing: AnimationEasing = AnimationEasing.CUBIC_OUT,
    var delay: Int = 0
): PlotFeature {
    override val featureName: FeatureName = DATA_CHANGE_ANIMATION_FEATURE
}

val DATA_CHANGE_ANIMATION_FEATURE = FeatureName("DATA_CHANGE_ANIMATION_FEATURE")

class AnimationEasing internal constructor(val name: String) {
    companion object {
        val LINEAR = AnimationEasing("linear")
        val CUBIC_OUT = AnimationEasing("cubicOut")
    }
}