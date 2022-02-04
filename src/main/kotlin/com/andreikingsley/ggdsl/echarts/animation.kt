package com.andreikingsley.ggdsl.echarts

import com.andreikingsley.ggdsl.ir.NamedData
import com.andreikingsley.ggdsl.ir.Plot

data class DataChangeAnimation(
    val plot: Plot,
    val interval: Int,
    val dataChange: NamedData.() -> Unit
)

fun Plot.withDataChangeAnimation(
    interval: Int,
    dataChange: NamedData.() -> Unit
) = DataChangeAnimation(this, interval, dataChange)
