package com.moham.digssquared.utils

import android.graphics.Color
import com.moham.digssquared.data.entities.RSRP
import com.moham.digssquared.data.entities.RSRQ
import com.moham.digssquared.data.entities.SINR

object ColoringValues {

    val RSRP_VALUES = listOf(
        RSRP(Color.parseColor("#000A00"),-150..-110),
        RSRP(Color.parseColor("#E51304"),-109..-100),
        RSRP(Color.parseColor("#FAFD0C"),-99..-90),
        RSRP(Color.parseColor("#02FA0E"),-89..-80),
        RSRP(Color.parseColor("#0B440D"),-89..-70),
        RSRP(Color.parseColor("#0EFFF8"),-69..-60),
        RSRP(Color.parseColor("#0007FF"),-59..0),
    )

    val RSRQ_VALUES = listOf(
        RSRQ(Color.parseColor("#000000"),-40..-20),
        RSRQ(Color.parseColor("#ff0000"),-19..-14),
        RSRQ(Color.parseColor("#ffee00"),-13..-9),
        RSRQ(Color.parseColor("#80ff00"),-8..-3),
        RSRQ(Color.parseColor("#3f7806"),-3..0),
    )

    val SINR_VALUES = listOf(
        SINR(Color.parseColor("#000000"),-10..0),
        SINR(Color.parseColor("#F90500"),1..5),
        SINR(Color.parseColor("#FD7632"),6..10),
        SINR(Color.parseColor("#FBFD00"),11..15),
        SINR(Color.parseColor("#00FF06"),16..20),
        SINR(Color.parseColor("#027500"),21..25),
        SINR(Color.parseColor("#0EFFF8"),26..30),
        SINR(Color.parseColor("#0000F0"),31..50),

    )

}