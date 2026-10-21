package com.ryall.bitcoinwallet.data.remote.dto

import com.squareup.moshi.Json

data class FixerFluctuationDto(
    val success: Boolean,
    @Json(name = "start_date")
    val startDate: String?,
    @Json(name = "end_date")
    val endDate: String?,
    val rates: Map<String, FluctuationRateDto>?,
    val error: FixerFluctuationErrorDto? = null
)

data class FluctuationRateDto(
    @Json(name = "start_rate")
    val startRate: Double,
    @Json(name = "end_rate")
    val endRate: Double,
    val change: Double,
    @Json(name = "change_pct")
    val changePercent: Double
)

data class FixerFluctuationErrorDto(
    val code: Int?,
    val type: String?,
    val info: String?
)
