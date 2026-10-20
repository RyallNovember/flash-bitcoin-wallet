package com.ryall.bitcoinwallet.data.mapper

import com.ryall.bitcoinwallet.data.remote.dto.FixerRatesDto
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import java.math.BigDecimal

fun FixerRatesDto.toDomain(): ExchangeRates {
    require(success) {
        "Fixer API returned an unsuccessful response"
    }
    require(base == "EUR") {
        "Unexpected exchange rate base: $base"
    }
    return ExchangeRates(
        btc = BigDecimal.valueOf(rates.btc),
        zar = BigDecimal.valueOf(rates.zar),
        usd = BigDecimal.valueOf(rates.usd),
        aud = BigDecimal.valueOf(rates.aud)
    )
}