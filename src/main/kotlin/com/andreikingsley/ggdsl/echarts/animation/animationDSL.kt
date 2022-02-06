package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.dsl.PlotContext

fun PlotContext.animation(block: AnimationFeature.() -> Unit){
    features[DATA_CHANGE_ANIMATION_FEATURE] = AnimationFeature().apply(block)
}
