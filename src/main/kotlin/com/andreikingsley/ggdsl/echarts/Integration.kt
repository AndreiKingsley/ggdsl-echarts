package com.andreikingsley.ggdsl.echarts

import org.jetbrains.kotlinx.jupyter.api.annotations.JupyterLibrary
import org.jetbrains.kotlinx.jupyter.api.*
import org.jetbrains.kotlinx.jupyter.api.libraries.*

@JupyterLibrary
internal class Integration : JupyterIntegration() {

    override fun Builder.onLoaded() {
        render<Option> { it.xAxis.type }
       // import("org.my.lib.*")
       // import("org.my.lib.io.*")
    }
}

private fun Option.toHTML(): String {
    return ""
}
