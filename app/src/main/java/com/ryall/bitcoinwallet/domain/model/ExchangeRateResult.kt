package com.ryall.bitcoinwallet.domain.model

data class ExchangeRateResult(
    val rates: ExchangeRates,
    val source: RateSource,
    val lastUpdated: Long
)

enum class RateSource {
    LIVE,
    CACHED,
    STALE
}