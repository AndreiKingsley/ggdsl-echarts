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
        render<Option> { HTML(it.toHTML()) }
        // import("org.my.lib.*")
        // import("org.my.lib.io.*")
    }
}


@ExperimentalSerializationApi
fun Option.toJSON(): String {
    return Json {
        explicitNulls = false
        encodeDefaults = true
    }.encodeToString(this)
}

fun Option.toHTML(): String {
    return createHTML().div {
        div {
            id = "main" // TODO!!!
            style = "width: 600;height:400;background: red"
        }
        script { src = "https://cdn.jsdelivr.net/npm/echarts@5.2.2/dist/echarts.min.js" }
        script {
            type = "text/javascript"
            +("\n        var myChart = echarts.init(document.getElementById('main'));\n" +
                    "        var option = ${toJSON()};\n" +
                    "        myChart.setOption(option);")
        }
    }
}
