package com.andreikingsley.ggdsl.echarts.stack

import com.andreikingsley.ggdsl.dsl.BarsContext
import com.andreikingsley.ggdsl.dsl.LayerContext
import com.andreikingsley.ggdsl.ir.feature.FeatureName
import com.andreikingsley.ggdsl.ir.feature.LayerFeature

/*
    val stackAd = Stack("Ad")

    bar {
        stack = stackAd
        stack with stackAd
        stack(stackAd)
    }
 */
/**/

// todo in others context??
var BarsContext.stack: Stack
get() = Stack("TODO")
set(value) {
    features[STACK_FEATURE_NAME] = value
}

//todo
data class Stack internal constructor(val name: String): LayerFeature {
    override val featureName: FeatureName = STACK_FEATURE_NAME
}

fun stack(name: String) = Stack(name)


