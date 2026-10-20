package com.ryall.bitcoinwallet.domain.converter

import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject

class BitcoinConverter @Inject constructor() {

    fun convert(
        amount: BigDecimal,
        rates: ExchangeRates
    ): ConvertedBitcoin {
        require(amount >= BigDecimal.ZERO) {
            "Bitcoin amount cannot be negative"
        }
        require(rates.btc > BigDecimal.ZERO) {
            "BTC exchange rate must be greater than zero"
        }
        require(rates.zar >= BigDecimal.ZERO) {
            "ZAR exchange rate cannot be negative"
        }
        require(rates.usd >= BigDecimal.ZERO) {
            "USD exchange rate cannot be negative"
        }
        require(rates.aud >= BigDecimal.ZERO) {
            "AUD exchange rate cannot be negative"
        }

        val mathContext = MathContext.DECIMAL128
        return ConvertedBitcoin(
            zar = amount.multiply(rates.zar)
                .divide(rates.btc, mathContext),
            usd = amount.multiply(rates.usd)
                .divide(rates.btc, mathContext),
            aud = amount.multiply(rates.aud)
                .divide(rates.btc, mathContext)
        )
    }
}

data class ConvertedBitcoin(
    val zar: BigDecimal,
    val usd: BigDecimal,
    val aud: BigDecimal
)
