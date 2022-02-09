package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.ir.NamedData
import com.andreikingsley.ggdsl.ir.Plot

data class DataChangeAnimation(
    val plot: Plot,
    val interval: Int,
    val dataChange: NamedData.() -> Unit
)

/*
data class PlotChangeAnimation(
    val plot: Plot,
    val interval: Int,
    val plotChange: Plot.() -> Unit
)
 */

fun Plot.withDataChangeAnimation(
    interval: Int,
    dataChange: NamedData.() -> Unit
) = DataChangeAnimation(this, interval, dataChange)

