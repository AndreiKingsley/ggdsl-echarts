package com.andreikingsley.ggdsl.echarts

import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.*
import org.jetbrains.kotlinx.jupyter.api.libraries.*

import kotlinx.html.*
import kotlinx.html.stream.createHTML

@JupyterLibrary
internal class Integration : JupyterIntegration() {

    override fun Builder.onLoaded() {
        render<Option> { HTML(it.toHTML()) }
        // import("org.my.lib.*")
        // import("org.my.lib.io.*")
    }
}

private fun Option.toHTML(): String {
    return createHTML().div {
        style = "width: 100px;height:800px;background: red"
    }
}
