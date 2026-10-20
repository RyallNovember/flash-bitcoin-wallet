package com.ryall.bitcoinwallet.presentation.util

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.math.BigDecimal

class CurrencyFormatterTest {

    @Test
    fun `format preserves large currency values`() {
        val amount = BigDecimal("1338044.75")

        val result = CurrencyFormatter.format(amount, "ZAR")

        val digits = result.filter { it.isDigit() }

        assertEquals("133804475", digits)
    }

    @Test
    fun `format displays two decimal places`() {
        val amount = BigDecimal("123.5")

        val result = CurrencyFormatter.format(amount, "USD")

        assertTrue(result.contains("123.50"))
    }

    @Test
    fun `format rounds to two decimal places`() {
        val amount = BigDecimal("99.999")

        val result = CurrencyFormatter.format(amount, "USD")

        assertTrue(result.contains("100.00"))
    }

    @Test
    fun `format supports all wallet currencies`() {
        val amount = BigDecimal("1500.25")

        listOf("ZAR", "USD", "AUD").forEach { currency ->
            val result = CurrencyFormatter.format(amount, currency)

            assertTrue(
                "Formatting failed for $currency",
                result.isNotBlank()
            )
        }
    }
}