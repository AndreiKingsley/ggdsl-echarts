package com.andreikingsley.ggdsl.echarts

import com.andreikingsley.ggdsl.ir.symbol.Symbol

class EchartsSymbol(override val name: String): Symbol {
    companion object {
        val DIAMOND = EchartsSymbol("diamond")
    }
}
