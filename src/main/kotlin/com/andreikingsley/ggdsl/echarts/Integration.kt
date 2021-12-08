package com.andreikingsley.ggdsl.echarts

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
                style = "width: 600;height:400;background: red"
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
