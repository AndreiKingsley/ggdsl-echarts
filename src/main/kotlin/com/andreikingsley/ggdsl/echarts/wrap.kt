package com.andreikingsley.ggdsl.echarts

import com.andreikingsley.ggdsl.echarts.animation.AnimationFeature
import com.andreikingsley.ggdsl.echarts.animation.DATA_CHANGE_ANIMATION_FEATURE
import com.andreikingsley.ggdsl.ir.*
import com.andreikingsley.ggdsl.ir.aes.*
import com.andreikingsley.ggdsl.ir.scale.*
import com.andreikingsley.ggdsl.util.color.*
import com.andreikingsley.ggdsl.util.linetype.CommonLineType
import com.andreikingsley.ggdsl.util.symbol.*
import kotlin.reflect.typeOf

internal fun NamedData.wrap(): Pair<List<List<String>>, Map<String, Int>> {
    val header = keys.toList()
    val values = values.toList()
    val size = values.first().size

    val idToDim = header.mapIndexed { index, s -> s to index }.toMap()

    val source = listOf(header) + (
        (0 until size).map { rowIndex ->
            header.indices.map { columnIndex -> values[columnIndex][rowIndex].toString() }
        }
    )
    //source.add(header)
    /*
    for (i in 0 until size) {
        source.add(
            header.map { data[it]!![i].toString() }
        )
    }

     */

    return source to idToDim
}

fun Geom.toType(): String {
    return when (this) {
        Geom.POINT -> "scatter"
        Geom.BAR -> "bar"
        Geom.LINE -> "line"
        else -> TODO()
    }
}

// todo!!!
val colors = listOf("red", "blue", "green", "yellow", "purple")
val sizes = listOf(20.0, 30.0, 40.0, 50.0, 60.0)
val alphas = listOf(0.2, 0.3, 0.4, 0.5, 0.6)
// TODO val symbols = listOf(TODO(), TODO(), TODO())

// TODO better
fun createInRange(aes: Aes, valuesString: List<String>, size: Int, isContinuous: Boolean): InRange {
    return when (aes) {
        COLOR -> InRange(
            color = if (valuesString.isNotEmpty()) {
                valuesString
            } else if (isContinuous) {
                listOf("red", "blue")
            } else {
                colors.take(size)
            }
        )
        SIZE -> InRange(
            symbolSize = if (valuesString.isNotEmpty()) {
                valuesString.map { it.toDouble() }
            } else if (isContinuous) {
                listOf(20.0, 60.0)
            } else {
                sizes.take(size)
            }
        )
        ALPHA -> InRange(
            colorAlpha = if (valuesString.isNotEmpty()) {
                valuesString.map { it.toDouble() }
            } else if (isContinuous) {
                listOf(0.3, 0.85)
            } else {
                alphas.take(size)
            }
        )
        // TODO SYMBOL
        else -> {
            TODO()
        }
    }
}

//todo
fun wrapValue(value: Any): String{
    return when(value){
        is CommonLineType -> value.description
        is CommonSymbol -> value.name
        is EchartsSymbol -> value.name
        is StandardColor -> value.description
        else -> value.toString()
    }
}

// TODO!!! seriesIndex
fun Scale.toVisualMap(aes: Aes, dim: Int, seriesIndex: Int, data: List<Any>): VisualMap {
    return when (this) {
        is CategoricalNonPositionalScale<*, *> -> {
            val categoriesString = if (categories.isNotEmpty()) {
                categories.map { value -> value.toString() }
            } else {
                data.toSet().map { it.toString() }
            }
            // TODO wrapValue
            val valuesString = values.map { value ->
                wrapValue(value)
            }
            val inRange = createInRange(aes, valuesString, categoriesString.size, isContinuous = false)
            VisualMap(
                type = "piecewise",
                show = true, // TODO
                dimension = dim,
                categories = categoriesString,
                inRange = inRange,
                seriesIndex = seriesIndex,
            )
        }
        is ContinuousNonPositionalScale<*, *> -> {
            val min = domainLimits?.first?.toString()
            val max = domainLimits?.second?.toString()
            val valuesString = range?.let {
                listOf(wrapValue(it), wrapValue(it))
            } ?: listOf()
            val inRange = createInRange(aes, valuesString, -1, isContinuous = true)
            VisualMap(
                type = "continuous",
                show = false, // TODO
                dimension = dim,
                min = min,
                max = max,
                inRange = inRange,
                seriesIndex = seriesIndex
            )
        }

        is DefaultNonPositionalScale<*, *> -> {
            // todo date

            when (domainType) {
                typeOf<String>() -> {
                    val categoriesString = data.toSet().map { it.toString() }
                    VisualMap(
                        type = "piecewise",
                        show = true, // TODO
                        dimension = dim,
                        categories = data.toSet().map { it.toString() },
                        inRange = createInRange(aes, listOf(), categoriesString.size, false),
                        seriesIndex = seriesIndex,
                    )
                }
                else -> {
                    VisualMap(
                        type = "continuous",
                        show = false, // TODO
                        dimension = dim,
                        // todo count
                        inRange = createInRange(aes, listOf(), -1, true),
                        seriesIndex = seriesIndex,
                    )
                }
            }
        }

        else -> {
            TODO()
        }
    }

}

fun Scale.toAxis(data: List<Any>): Axis {
    return when (this) {
        is CategoricalPositionalScale<*> -> {
            Axis(
                name = axis.name,
                type = "category",
                data = if (categories.isEmpty()) {
                    data.toSet().map { it.toString() }
                } else {
                    categories.map { value -> value.toString() }
                }
            )
        }
        is ContinuousPositionalScale<*> -> {
            Axis(
                name = axis.name,
                type = "value",
                min = limits?.first?.toString(),
                max = limits?.second?.toString(),
            )
        }
        is DefaultPositionalScale<*> -> when (domainType) {
            typeOf<String>() -> {
                Axis(
                    type = "category",
                    data = data.toSet().map { it.toString() }
                )
            }
            else -> {
                Axis(
                    type = "value"
                )
            }
        }
        else -> {
            TODO()
        }
    }
}

fun Layer.toSeries(): Series {
    // TODO STYLE, type series

    return Series(
        type = geom.toType(),
        encode = XYEncode(
            x = mappings[X]!!,
            y = mappings[Y]!!
        ),
        symbolSize = settings[SIZE]?.let { (it as Double).toInt() * 4 }, // TODO
        itemStyle = ItemStyle(
            color = settings[COLOR]?.let { (it as StandardColor).description },
            opacity = settings[ALPHA]?.let { it as Double },
            borderColor = settings[BORDER_COLOR]?.let { (it as StandardColor).description },
            borderWidth = settings[BORDER_WIDTH]?.let { it as String },
        ),
        // TODO
        symbol = (settings[SYMBOL] as? Symbol)?.name,
        barWidth = if (geom == Geom.BAR) {
            settings[WIDTH] as? Double
        } else {
            null
        },
        lineStyle = if (geom == Geom.LINE) {
            LineStyle(
                width = settings[WIDTH]?.let { it as Double },
                color = settings[COLOR]?.let { (it as StandardColor).description },
                type = settings[LINE_TYPE]?.let { (it as CommonLineType).description }
            )
        } else {
            null
        }
    )
}

fun Plot.toOption(): MetaOption {
    val (source, idToDim) = dataset.wrap()
    // TODO!!!

    val visualMaps = mutableListOf<VisualMap>()
    var xAxis = Axis("value")
    var yAxis = Axis("value")

    //val xAxes = mutableListOf<Axis>()
    //val yAxes = mutableListOf<Axis>()
    //val series = mutableListOf<Series>()

    layers.forEachIndexed { index, layer ->
        layer.scales.forEach { (aes, scale) ->
            // TODO X  and Y
            //val xAxisIndex = xAxes.size
            //val yAxisIndex = yAxes.size
            val data = dataset[layer.mappings[aes]!!]!!
            when (aes) {
                X -> xAxis = scale.toAxis(data)
                Y -> yAxis = scale.toAxis(data)
                else -> visualMaps.add(
                    scale.toVisualMap(
                        aes,
                        idToDim[layer.mappings[aes]!!]!!,
                        index,
                        data
                    )
                ) // TODO
            }
        }

    }

    return MetaOption(
        Option(
            dataset = Dataset(source),
            xAxis = listOf(xAxis),
            yAxis = listOf(yAxis),
            visualMap = visualMaps,
            series = layers.map { it.toSeries() },
            title = layout.title?.let { Title(it) }
        ).apply {
            (features[DATA_CHANGE_ANIMATION_FEATURE] as? AnimationFeature)?.let {
                animation = true
                animationThreshold = it.threshold
                animationDuration = it.duration
                animationEasing = it.easing.name
                animationDelay = it.delay
            }
        }, layout.size
    )
}
