package com.ryall.bitcoinwallet.domain.converter

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object BitcoinFluctuationCalculator {

    private val mathContext = MathContext.DECIMAL64

    fun calculateChangePercent(
        startBtcRate: BigDecimal,
        endBtcRate: BigDecimal,
        startCurrencyRate: BigDecimal,
        endCurrencyRate: BigDecimal
    ): BigDecimal? {
        if (
            startBtcRate <= BigDecimal.ZERO ||
            endBtcRate <= BigDecimal.ZERO ||
            startCurrencyRate <= BigDecimal.ZERO ||
            endCurrencyRate <= BigDecimal.ZERO
        ) {
            return null
        }

        val yesterdayValue = startCurrencyRate.divide(
            startBtcRate,
            mathContext
        )

        val todayValue = endCurrencyRate.divide(
            endBtcRate,
            mathContext
        )

        return todayValue
            .subtract(yesterdayValue)
            .divide(yesterdayValue, mathContext)
            .multiply(BigDecimal("100"))
            .setScale(2, RoundingMode.HALF_UP)
    }
}
