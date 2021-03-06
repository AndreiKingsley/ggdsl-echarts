package com.andreikingsley.ggdsl.echarts.stack

import com.andreikingsley.ggdsl.dsl.BarsContext
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

// todo in others contexts??
/**
 * Name of stack. On the same category axis,
 * the series with the same stack name would be put on top of each other.
 *
 *
 * @see <a href="https://echarts.apache.org/en/option.html#series-bar.stack">ECharts Dcoumentation</a>
 * @see stack
 */
var BarsContext.stack: Stack
get() = Stack("TODO")
set(value) {
    features[Stack.FEATURE_NAME] = value
}

//todo
data class Stack internal constructor(val name: String): LayerFeature {
    override val featureName: FeatureName = FEATURE_NAME

    companion object {
        val FEATURE_NAME = FeatureName("STACK_FEATURE")
    }
}

/**
 * Returns new stack.
 *
 * @param name the stack name
 * @return [Stack]
 */
fun stack(name: String) = Stack(name)


