package com.ryall.bitcoinwallet.presentation.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {
    fun format(
        amount: BigDecimal,
        currencyCode: String
    ): String {
        val locale = when (currencyCode) {
            "ZAR" -> Locale.forLanguageTag("en-ZA")
            "USD" -> Locale.US
            "AUD" -> Locale.forLanguageTag("en-AU")
            else -> Locale.US
        }

        val formatter = NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode)
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return formatter.format(amount)
    }
}