package com.ryall.bitcoinwallet.data.remote.dto

import com.squareup.moshi.Json

data class FixerRatesDto(
    @Json(name = "success")
    val success: Boolean,

    @Json(name = "base")
    val base: String,

    @Json(name = "rates")
    val rates: CurrencyRatesDto
)

data class CurrencyRatesDto(
    @Json(name = "BTC")
    val btc: Double,

    @Json(name = "ZAR")
    val zar: Double,

    @Json(name = "USD")
    val usd: Double,

    @Json(name = "AUD")
    val aud: Double
    )