package com.andreikingsley.ggdsl.echarts

import com.andreikingsley.ggdsl.echarts.animation.DataChangeAnimation
import com.andreikingsley.ggdsl.ir.Plot
import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.*
import org.jetbrains.kotlinx.jupyter.api.libraries.*

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@JupyterLibrary
internal class Integration : JupyterIntegration() {

    override fun Builder.onLoaded() {
        resources {
            js("echarts") {
                url(ECHARTS_SRC)
                classPath("js/echarts.min.js")
            }
        }
        render<Option> { HTML(it.toHTML(), true) }
        render<DataChangeAnimation> { HTML(it.toHTML(), true) }

        // TODO imports
        // import("org.my.lib.*")
        // import("org.my.lib.io.*")
    }
}

const val ECHARTS_SRC = "https://cdn.jsdelivr.net/npm/echarts@5.2.2/dist/echarts.min.js"

@ExperimentalSerializationApi
fun Option.toJSON(): String {
    return Json {
        explicitNulls = false
        encodeDefaults = true
    }.encodeToString(this)
}

fun Option.toHTML(): String {
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title("MY BEAUTIFUL PLOT")
            script {
                type = "text/javascript"
                src = ECHARTS_SRC
            }
        }
        body {
            div {
                id = "main" // TODO!!!
                style = "width: 1000px;height:800px;background: white"
            }
            script {
                type = "text/javascript"
                +("\n        var myChart = echarts.init(document.getElementById('main'));\n" +
                        "        var option = ${toJSON().replace('\"', '\'')};\n" +
                        "        myChart.setOption(option);")
            }
        }

    }


}

/*
fun NamedData.copy(): NamedData {
    return this.map { it.key to it.value.toList().toTypedArray() }.toMap()
}

 */

fun DataChangeAnimation.toHTML(): String {
    val encoder = Json {
        explicitNulls = false
        encodeDefaults = true
    }
    val maxStates = 100
    val initOption = plot.toOption().toJSON().replace('\"', '\'')
    var dataset = plot.dataset!!
    val options = mutableListOf<Option>()
    repeat(maxStates) {
        dataChange(dataset)
        val newPlot = Plot(dataset, plot.layers, plot.layout, plot.features)
        options.add(newPlot.toOption())
    }
    val encodedOptions = encoder.encodeToString(options).replace('\"', '\'')
    return createHTML().html {
        head {
            meta {
                charset = "utf-8"
            }
            title("MY BEAUTIFUL PLOT")
            script {
                type = "text/javascript"
                src = ECHARTS_SRC
            }
        }
        body {
            div {
                id = "main" // TODO!!!
                style = "width: 1000px;height:800px;background: white"
            }
            script {
                type = "text/javascript"
                +(
                        "\n        var myChart = echarts.init(document.getElementById('main'));\n" +
                                "        var option = $initOption;\n" +
                                "        myChart.setOption(option);\n" +
                                "        var options = $encodedOptions;\n" +
                                "var nextState = 0;\n" +
                                "var maxStates = $maxStates\n" +
                                "setInterval(function () {\n" +
                                "var newOption = options[nextState];\n" +
                                "nextState = Math.min(1 + nextState, maxStates-1); \n" +
                                "  myChart.setOption(newOption, true);\n" +
                                "}, $interval);\n"
                        )
            }
        }

    }
}