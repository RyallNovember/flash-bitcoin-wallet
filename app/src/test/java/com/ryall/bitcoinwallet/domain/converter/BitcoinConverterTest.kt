package com.ryall.bitcoinwallet.domain.converter

import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.math.BigDecimal

class BitcoinConverterTest {

    private val converter = BitcoinConverter()

    private val rates = ExchangeRates(
        btc = BigDecimal("0.00002"),
        zar = BigDecimal("20"),
        usd = BigDecimal("1"),
        aud = BigDecimal("1.5")
    )

    @Test
    fun `convert returns correct currency values for fractional bitcoin`() {
        val amount = BigDecimal("0.025")

        val result = converter.convert(amount, rates)

        assertEquals(0, BigDecimal(25000).compareTo(result.zar))
        assertEquals(0, BigDecimal(1250).compareTo(result.usd))
        assertEquals(0, BigDecimal(1875).compareTo(result.aud))
    }

    @Test
    fun `convert returns zero for zero bitcoin holdings`() {
        val result = converter.convert(BigDecimal.ZERO, rates)

        assertEquals(0, BigDecimal.ZERO.compareTo(result.zar))
        assertEquals(0, BigDecimal.ZERO.compareTo(result.usd))
        assertEquals(0, BigDecimal.ZERO.compareTo(result.aud))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convert rejects negative bitcoin amounts`() {
        converter.convert(BigDecimal("-0,5"), rates)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convert rejects zero BTC exchange rate`() {
        val invalidRates = rates.copy(
            btc = BigDecimal.ZERO
        )

        converter.convert(
            BigDecimal("0.025"),
            invalidRates
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `convert rejects negative fiat exchange rate`() {
        val invalidRates = rates.copy(
            zar = BigDecimal("-20")
        )

        converter.convert(
            BigDecimal("0.025"),
            invalidRates
        )
    }
}
