package com.andreikingsley.ggdsl.echarts.animation

import com.andreikingsley.ggdsl.ir.Plot

data class PlotChangeAnimation(
    val plots: List<Plot>,
    val interval: Int,
)
