package com.andreikingsley.ggdsl.echarts

import com.andreikingsley.ggdsl.echarts.animation.AnimationFeature
import com.andreikingsley.ggdsl.echarts.animation.DATA_CHANGE_ANIMATION_FEATURE
import com.andreikingsley.ggdsl.echarts.scale.guide.EchartsAxis
import com.andreikingsley.ggdsl.echarts.scale.guide.EchartsLegend
import com.andreikingsley.ggdsl.echarts.stack.STACK_FEATURE_NAME
import com.andreikingsley.ggdsl.echarts.stack.Stack
import com.andreikingsley.ggdsl.echarts.util.color.*
import com.andreikingsley.ggdsl.echarts.util.color.toEchartsColorOption
import com.andreikingsley.ggdsl.echarts.util.symbol.EchartsSymbol
import com.andreikingsley.ggdsl.ir.*
import com.andreikingsley.ggdsl.ir.aes.*
import com.andreikingsley.ggdsl.ir.bindings.NonPositionalSetting
import com.andreikingsley.ggdsl.ir.bindings.ScalableMapping
import com.andreikingsley.ggdsl.ir.bindings.Setting
import com.andreikingsley.ggdsl.ir.data.NamedData
import com.andreikingsley.ggdsl.ir.scale.*
import com.andreikingsley.ggdsl.util.color.*
import com.andreikingsley.ggdsl.util.linetype.CommonLineType
import com.andreikingsley.ggdsl.util.symbol.*
import kotlin.reflect.typeOf

data class DataInfo(
    val data: List<List<String>>,
    val header: Map<String, Int>
)

internal fun NamedData.wrap(): DataInfo {
    val header = keys.toList()
    val values = values.toList()
    val size = values.first().size

    val idToDim = header.mapIndexed { index, s -> s to index }.toMap()

    val source = listOf(header) + (
        (0 until size).map { rowIndex ->
            header.indices.map { columnIndex -> values.getOrNull(columnIndex)?.getOrNull(rowIndex).toString() }
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

    return DataInfo(source, idToDim)
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
val colors = listOf("red", "blue", "green", "yellow", "purple", "orange", "pink")
val sizes = listOf(20.0, 28.0, 36.0, 44.0, 52.0, 60.0, 68.0)
val alphas = listOf(0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5)
val symbols = listOf("circle", "rect", "triangle", "diamond", "roundRect", "pin", "arrow")

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
        SYMBOL -> InRange(
            symbol = if (valuesString.isNotEmpty()) {
                valuesString.map { wrapValue(it) }
            } else if (isContinuous) {
                TODO("error")
            } else {
                symbols.take(size)
            }
        )
        else -> {
            TODO()
        }
    }
}

//todo
fun wrapValue(value: Any): String{
    return when(value){
        is CommonLineType -> value.description
        is CommonSymbol -> value.description
        is EchartsSymbol -> value.name
        is StandardColor -> value.description
        else -> value.toString()
    }
}

internal var visualMapCounter = 0
// TODO!!! seriesIndex
fun Scale.toVisualMap(aes: Aes, dim: Int, seriesIndex: Int, data: List<Any>): VisualMap {
    val legend = (this as NonPositionalScale<*, *>).legend as? EchartsLegend<*, *>
    val name = legend?.name
    val show = legend?.show
    val calculable = legend?.calculable

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
                show = show,
                text = name?.let { listOf(it) },
                calculable = calculable,
                type = "piecewise",
                dimension = dim,
                categories = categoriesString,
                inRange = inRange,
                seriesIndex = seriesIndex,

                right = 10,
                top = (visualMapCounter++) * 150
            )
        }
        is ContinuousNonPositionalScale<*, *> -> {
            val min = domainLimits?.first?.toString()?.toDouble()
            val max = domainLimits?.second?.toString()?.toDouble()
            val valuesString = range?.let {
                listOf(wrapValue(it.first), wrapValue(it.second))
            } ?: listOf()
            val inRange = createInRange(aes, valuesString, -1, isContinuous = true)
            VisualMap(
                show = show,
                text = name?.let { listOf(it) },
                calculable = calculable,
                type = "continuous",
              //  show = false, // TODO
                dimension = dim,
                min = min,
                max = max,
                inRange = inRange,
                seriesIndex = seriesIndex,

                right = 10,
                top = (visualMapCounter++) * 150
            )
        }

        is DefaultNonPositionalScale<*, *> -> {
            // todo date

            when (domainType) {
                // todo
                typeOf<String>() -> {
                    val categoriesString = data.toSet().map { it.toString() }
                    VisualMap(
                        show = show,
                        text = name?.let { listOf(it) },
                        calculable = calculable,
                        type = "piecewise",
                     //   show = true, // TODO
                        dimension = dim,
                        categories = data.toSet().map { it.toString() },
                        inRange = createInRange(aes, listOf(), categoriesString.size, false),
                        seriesIndex = seriesIndex,

                        right = 10,
                        top = (visualMapCounter++) * 150
                    )
                }
                else -> {
                    VisualMap(
                        show = show,
                        text = name?.let { listOf(it) },
                        calculable = calculable,
                        type = "continuous",
                      //  show = false, // TODO
                        dimension = dim,
                        // todo count
                        inRange = createInRange(aes, listOf(), -1, true),
                        seriesIndex = seriesIndex,

                        right = 10,
                        top = (visualMapCounter++) * 150
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
    val axis = (this as PositionalScale<*>).axis as? EchartsAxis<*>
    val name = axis?.name
    val show = axis?.show
    return when (this) {
        is CategoricalPositionalScale<*> -> {
            Axis(
                show = show,
                // TODO SORT NUMERICAL???
                name = name,
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
                show = show,
                name = name,
                type = "value",
                min = limits?.first?.toString(),
                max = limits?.second?.toString(),
            )
        }
        is DefaultPositionalScale<*> -> when (domainType) {
            // todo other types
            typeOf<String>() -> {
                Axis(
                    show = show,
                    name = name,
                    type = "category",
                    data = data.toSet().map { it.toString() }
                )
            }
            else -> {
                Axis(
                    show = show,
                    name = name,
                    type = "value"
                )
            }
        }
        else -> {
            TODO()
        }
    }
}

fun<T: Any> Map<Aes, Setting>.getNPSValue(key: NonPositionalAes<T>): T?{
    return (this[key] as? NonPositionalSetting<*>)?.value as? T
}

fun Layer.toSeries(dataset: List<List<String>>?): Series {

    // TODO STYLE, type series

    val size = settings.getNPSValue(SIZE)
    val color = settings.getNPSValue(COLOR)
    val alpha = settings.getNPSValue(ALPHA)
    val borderColor = settings.getNPSValue(BORDER_COLOR)
    val borderWidth = settings.getNPSValue(BORDER_WIDTH)
    val symbol = settings.getNPSValue(SYMBOL)
    val width = settings.getNPSValue(WIDTH)
    val lineType = settings.getNPSValue(LINE_TYPE)

    val stack = (features[STACK_FEATURE_NAME] as? Stack)?.name

    return Series(
        type = geom.toType(),
        encode = XYEncode(
            x = mappings[X]!!.source.id,
            y = mappings[Y]!!.source.id
        ),
        symbolSize = size?.let { it.toInt() * 4 }, // TODO
        itemStyle = ItemStyle(
            color = color?.let { wrapColor(it) },
            opacity = alpha,
            borderColor = borderColor?.let { wrapColor(it) },
            borderWidth = borderWidth,
        ),
        // TODO
        symbol = symbol?.let { wrapValue(it) },
        barWidth = if (geom == Geom.BAR) {
            width
        } else {
            null
        },
        lineStyle = if (geom == Geom.LINE) {
            LineStyle(
                width = width,
                color = color?.let { wrapColor(it) },
                type = lineType?.let { (it as CommonLineType).description } // todo add wrapper
            )
        } else {
            null
        },
        stack = stack,
        data = dataset
    )
}

// todo better serializer
fun wrapColor(color: Color): EchartsColorOption {
    return when(color){
        is StandardColor -> SingleColor(color).toEchartsColorOption()
        is LinearGradientColor -> color.toEchartsColorOption()
        is RadialGradientColor -> color.toEchartsColorOption()
        else -> TODO()
    }
}

fun Plot.toOption(): MetaOption {
    val (source, idToDim) = dataset.wrap()
    // TODO!!!

    val visualMaps = mutableListOf<VisualMap>()
    visualMapCounter = 0

    var xAxis = Axis("value")
    var yAxis = Axis("value")

    //val xAxes = mutableListOf<Axis>()
    //val yAxes = mutableListOf<Axis>()
    //val series = mutableListOf<Series>()

    val layerToData = layers.mapIndexed { index, layer ->
        index to if (layer.data === dataset) {
            null
        } else {
            layer.data?.wrap()
        }
    }.toMap()

    layers.forEachIndexed { index, layer ->
        layer.mappings.forEach { (aes, mapping) ->
            if (mapping is ScalableMapping) {
                val scale = mapping.scale
                val srcId = layer.mappings[aes]!!.source.id
                val data = layer.data?.get(srcId) ?: dataset[srcId]!!
                val seriesIndex = layerToData[index]?.header?.get(srcId) ?: idToDim[srcId]!!
                when (aes) {
                    X -> xAxis = scale.toAxis(data)
                    Y -> yAxis = scale.toAxis(data)
                    else -> visualMaps.add(
                        scale.toVisualMap(
                            aes,
                            seriesIndex,
                            index,
                            data
                        )
                    ) // TODO
                }
            }
            // TODO X  and Y
            //val xAxisIndex = xAxes.size
            //val yAxisIndex = yAxes.size
        }

    }

    return MetaOption(
        Option(
            dataset = Dataset(source),
            xAxis = listOf(xAxis),
            yAxis = listOf(yAxis),
            visualMap = visualMaps,
            series = layers.mapIndexed { index, layer -> layer.toSeries(layerToData[index]?.data) },
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
