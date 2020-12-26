package com.moham.digssquared.data.entities

import com.moham.digssquared.utils.ColoringValues

data class Values(
    val RSRP: Int,
    val RSRQ: Int,
    val SINR: Int
){
    fun getRSRPProgress(): Float {
        return (RSRP + 150).toFloat()
    }

    fun getRSRQProgress(): Float {
        return (RSRQ + 40).toFloat()
    }

    fun getSINRProgress(): Float {
        return (SINR + 10).toFloat()
    }

    fun getRSRPColor():Int{
        return ColoringValues.RSRP_VALUES.find { RSRP in it.range }?.Color ?: 0x3307775
    }

    fun getRSRQColor():Int{
        return ColoringValues.RSRQ_VALUES.find { RSRQ in it.range }?.Color ?: 0x3307775
    }

    fun getSINRColor():Int{
        return ColoringValues.SINR_VALUES.find { SINR in it.range }?.Color ?: 0x3307775
    }
}