package com.ryall.bitcoinwallet.domain.usecase

import com.ryall.bitcoinwallet.domain.converter.BitcoinConverter
import com.ryall.bitcoinwallet.domain.converter.ConvertedBitcoin
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import java.math.BigDecimal
import javax.inject.Inject

class GetWalletValueUseCase @Inject constructor(
    private val bitcoinConverter: BitcoinConverter
) {
     operator fun invoke(
        amount: BigDecimal,
        rates: ExchangeRates
    ): ConvertedBitcoin {
        return bitcoinConverter.convert(amount, rates)
    }
}