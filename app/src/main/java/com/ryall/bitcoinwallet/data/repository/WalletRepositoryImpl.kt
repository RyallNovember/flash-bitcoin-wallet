package com.ryall.bitcoinwallet.data.repository

import com.ryall.bitcoinwallet.data.local.ExchangeRateCache
import com.ryall.bitcoinwallet.data.mapper.toDomain
import com.ryall.bitcoinwallet.data.remote.api.FixerApi
import com.ryall.bitcoinwallet.domain.model.ExchangeRateResult
import com.ryall.bitcoinwallet.domain.model.RateSource
import com.ryall.bitcoinwallet.domain.repository.WalletRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val fixerApi: FixerApi,
    private val exchangeRateCache: ExchangeRateCache
) : WalletRepository {

    override suspend fun getExchangeRates(): ExchangeRateResult {
        //Use cached rates if they are still fresh.
        exchangeRateCache.getCachedRates()?.let { rates ->
            return ExchangeRateResult(
                rates = rates,
                source = RateSource.CACHED,
                lastUpdated = requireNotNull(
                    exchangeRateCache.getLastUpdated()
                )
            )
        }
        //Otherwise, fetch the latest rates.
        return try {
            val rates = fixerApi.getLatestRates().toDomain()
            exchangeRateCache.saveRates(rates)

            ExchangeRateResult(
                rates = rates,
                source = RateSource.LIVE,
                lastUpdated = requireNotNull(
                    exchangeRateCache.getLastUpdated()
                )
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            //If the API fails, fall back to saved rates.
            val savedRates = exchangeRateCache.getLastSavedRates()
                ?: throw exception
            ExchangeRateResult(
                rates = savedRates,
                source = RateSource.STALE,
                lastUpdated = requireNotNull(
                    exchangeRateCache.getLastUpdated()
                )
            )
        }
    }
}