package com.ryall.bitcoinwallet.domain.repository

import com.ryall.bitcoinwallet.domain.model.ExchangeRateResult

interface WalletRepository {

    suspend fun getExchangeRates(): ExchangeRateResult
}