package com.ryall.bitcoinwallet.domain.model

import java.math.BigDecimal

data class ExchangeRates(
    val btc: BigDecimal,
    val zar: BigDecimal,
    val usd: BigDecimal,
    val aud: BigDecimal
)