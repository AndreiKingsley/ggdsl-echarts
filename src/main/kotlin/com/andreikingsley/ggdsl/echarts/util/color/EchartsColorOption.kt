package com.andreikingsley.ggdsl.echarts.util.color

@kotlinx.serialization.Serializable
data class EchartsColorOption(
    val type: String,
    val x: Double,
    val y: Double,

    val x2: Double? = null,
    val y2: Double? = null,
    val r: Double? = null,

    val colorStops: List<ColorStop>
)

@kotlinx.serialization.Serializable
data class ColorStop(
    val offset: Double,
    val color: String,
)
