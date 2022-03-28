package com.andreikingsley.ggdsl.echarts

import kotlinx.serialization.Serializable

@Serializable
data class MetaOption(val option: Option, val size: Pair<Int, Int>)
