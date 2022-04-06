package com.andreikingsley.ggdsl.echarts.util.color

import com.andreikingsley.ggdsl.util.color.Color
import com.andreikingsley.ggdsl.util.color.StandardColor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// todo offset
open class LinearGradientColor(
    val x1: Double = 0.0,
    val y1: Double = 0.0,
    val x2: Double = 0.0,
    val y2: Double = 1.0,
    val colors: List<Pair<Double, StandardColor>> = listOf(0.0 to Color.RED, 1.0 to Color.BLUE),
): EchartsColor

class SingleColor(color: StandardColor): LinearGradientColor(colors = listOf(0.0 to color))

internal fun LinearGradientColor.toEchartsColorOption() = EchartsColorOption(
    "linear", x1, y1, x2, y2, null, colors.map { it.toColorStop() }
)

private fun Pair<Double, StandardColor>.toColorStop() =
    ColorStop(first, second.description)

class RadialGradientColor(
    val x: Double = 0.5,
    val y: Double = 0.5,
    val r: Double = 0.5,
    val colors: List<Pair<Double, StandardColor>> = listOf(0.0 to Color.RED, 1.0 to Color.BLUE),
): EchartsColor

internal fun RadialGradientColor.toEchartsColorOption() = EchartsColorOption(
    "radial", x, y, null, null, r, colors.map { it.toColorStop() }
)

