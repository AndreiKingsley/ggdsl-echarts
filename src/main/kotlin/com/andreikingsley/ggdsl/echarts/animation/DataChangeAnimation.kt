package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.ir.data.NamedData
import com.andreikingsley.ggdsl.ir.Plot


data class DataChangeAnimation internal constructor(
    val plot: Plot,
    val interval: Int,
    val dataChange: NamedData.() -> Unit
)

fun Plot.withDataChangeAnimation(
    interval: Int,
    dataChange: NamedData.() -> Unit
) = DataChangeAnimation(this, interval, dataChange)

