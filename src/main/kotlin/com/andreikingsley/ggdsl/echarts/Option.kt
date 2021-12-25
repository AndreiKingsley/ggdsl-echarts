package com.andreikingsley.ggdsl.echarts

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    val dataset: Dataset,
    val xAxis: List<Axis>,
    val yAxis: List<Axis>,
    val visualMap: List<VisualMap>? = null,
    val series: List<Series>,
    val title: Title? = null,
)

@Serializable
data class Title(
    val text: String
)

@Serializable
data class Dataset(
    // TODO val source: List<List<@Contextual Any>>
    val source: List<List<String>>
)

@Serializable
data class Axis(
    val type: String,
    val name: String? = null,

    val min: String? = null,
    val max: String? = null,

    val data: List<String>? = null,

    val axisTick: AxisTick? = AxisTick(),
)

@Serializable
data class AxisTick(
    val alignWithLabel: Boolean = true,
)

@Serializable
data class Series(
    //  val name: String,
    val type: String,
    val encode: XYEncode,
    val barWidth: Double? = null,
    val itemStyle: ItemStyle? = null,
    val symbolSize: Int? = null,
    val symbol: String? = null,
    val lineStyle: LineStyle? = null
)

@Serializable
data class XYEncode(
    val x: String,
    val y: String
)

@Serializable
data class ItemStyle(
    val color: String? = null,
    val opacity: Double? = null,
    val borderColor: String? = null,
    val borderWidth: String? = null,
)

@Serializable
data class LineStyle(
    val width: Double? = null,
)

@Serializable
data class VisualMap(
    val type: String,

    val show: Boolean = false,
    val dimension: Int,
    val seriesIndex: Int,

    val min: String? = null,
    val max: String? = null,

    val categories: List<String>? = null,

    val inRange: InRange
)

@Serializable
data class InRange(
    // TODO
    val symbolSize: List<Double>? = null,
    val color: List<String>? = null,
    val colorAlpha: List<Double>? = null,
)
