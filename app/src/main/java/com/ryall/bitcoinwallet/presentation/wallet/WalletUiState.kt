package com.ryall.bitcoinwallet.presentation.wallet

import com.ryall.bitcoinwallet.domain.converter.ConvertedBitcoin
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import com.ryall.bitcoinwallet.domain.model.RateSource

data class WalletUiState(
    val bitcoinAmount: String = "0",
    val convertedBitcoin: ConvertedBitcoin? = null,
    val isLoading: Boolean = false,
    val exchangeRates: ExchangeRates? = null,
    val rateSource: RateSource? = null,
    val lastUpdated: Long? = null,
    val errorMessage: String? = null
)