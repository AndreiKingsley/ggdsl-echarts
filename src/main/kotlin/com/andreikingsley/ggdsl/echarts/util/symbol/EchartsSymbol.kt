package com.andreikingsley.ggdsl.echarts.util.symbol

import com.andreikingsley.ggdsl.util.symbol.Symbol

class EchartsSymbol internal constructor(val name: String): Symbol {
    companion object {
        val DIAMOND = EchartsSymbol("diamond")
        val ROUND_RECT = EchartsSymbol("roundRect")
        val PIN = EchartsSymbol("pin")
        val ARROW = EchartsSymbol("arrow")
        val NONE = EchartsSymbol("none")
    }
}
