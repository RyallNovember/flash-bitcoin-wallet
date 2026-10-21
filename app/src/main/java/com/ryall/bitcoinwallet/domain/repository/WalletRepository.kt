package com.ryall.bitcoinwallet.domain.repository

import com.ryall.bitcoinwallet.domain.model.ExchangeRateResult
import java.math.BigDecimal

interface WalletRepository {

    suspend fun getExchangeRates(): ExchangeRateResult

    suspend fun getDailyFluctuations(
        startDate: String,
        endDate: String
    ): Map<String, BigDecimal>
}