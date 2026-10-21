package com.ryall.bitcoinwallet.domain.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.math.BigDecimal

class BitcoinFluctuationCalculatorTest {

    @Test
    fun `returns positive percentage when Bitcoin value increases`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal("0.00001"),
            endBtcRate = BigDecimal("0.00001"),
            startCurrencyRate = BigDecimal("20"),
            endCurrencyRate = BigDecimal("21")
        )

        assertEquals(BigDecimal("5.00"), result)
    }

    @Test
    fun `returns negative percentage when Bitcoin value decreases`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal("0.00001"),
            endBtcRate = BigDecimal("0.00001"),
            startCurrencyRate = BigDecimal("20"),
            endCurrencyRate = BigDecimal("19")
        )

        assertEquals(BigDecimal("-5.00"), result)
    }

    @Test
    fun `returns zero when Bitcoin value is unchanged`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal("0.00001"),
            endBtcRate = BigDecimal("0.00001"),
            startCurrencyRate = BigDecimal("20"),
            endCurrencyRate = BigDecimal("20")
        )

        assertEquals(BigDecimal("0.00"), result)
    }

    @Test
    fun `returns null when Bitcoin rate is zero`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal.ZERO,
            endBtcRate = BigDecimal("0.00001"),
            startCurrencyRate = BigDecimal("20"),
            endCurrencyRate = BigDecimal("21")
        )

        assertNull(result)
    }

    @Test
    fun `returns null when currency rate is negative`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal("0.00001"),
            endBtcRate = BigDecimal("0.00001"),
            startCurrencyRate = BigDecimal("-20"),
            endCurrencyRate = BigDecimal("21")
        )

        assertNull(result)
    }

    @Test
    fun `accounts for changes in the BTC base rate`() {
        val result = BitcoinFluctuationCalculator.calculateChangePercent(
            startBtcRate = BigDecimal("0.00001"),
            endBtcRate = BigDecimal("0.000008"),
            startCurrencyRate = BigDecimal("20"),
            endCurrencyRate = BigDecimal("20")
        )

        assertEquals(BigDecimal("25.00"), result)
    }
}
