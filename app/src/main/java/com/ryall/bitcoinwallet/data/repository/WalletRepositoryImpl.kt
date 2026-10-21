package com.ryall.bitcoinwallet.data.repository

import com.ryall.bitcoinwallet.data.local.ExchangeRateCache
import com.ryall.bitcoinwallet.data.mapper.toDomain
import com.ryall.bitcoinwallet.data.remote.api.FixerApi
import com.ryall.bitcoinwallet.domain.converter.BitcoinFluctuationCalculator
import com.ryall.bitcoinwallet.domain.model.ExchangeRateResult
import com.ryall.bitcoinwallet.domain.model.RateSource
import com.ryall.bitcoinwallet.domain.repository.WalletRepository
import kotlinx.coroutines.CancellationException
import java.math.BigDecimal
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

    override suspend fun getDailyFluctuations(
        startDate: String,
        endDate: String
    ): Map<String, BigDecimal> {
        // Return today's cached comparison if it exists.
        exchangeRateCache.getCachedFluctuations(endDate)?.let {
            return it
        }

        return try {
            val response = fixerApi.getFluctuation(
                startDate = startDate,
                endDate = endDate
            )

            val fluctuations = if (response.success) {
                val rates = response.rates
                val btcRates = rates?.get("BTC")

                if (rates == null || btcRates == null) {
                    emptyMap()
                } else {
                    listOf("ZAR", "USD", "AUD").mapNotNull { currency ->
                        val currencyRates = rates[currency]
                            ?: return@mapNotNull null

                        val change = BitcoinFluctuationCalculator
                            .calculateChangePercent(
                                startBtcRate = BigDecimal.valueOf(
                                    btcRates.startRate
                                ),
                                endBtcRate = BigDecimal.valueOf(
                                    btcRates.endRate
                                ),
                                startCurrencyRate = BigDecimal.valueOf(
                                    currencyRates.startRate
                                ),
                                endCurrencyRate = BigDecimal.valueOf(
                                    currencyRates.endRate
                                )
                            )

                        change?.let { currency to it }
                    }.toMap()
                }
            } else {
                emptyMap()
            }

            // Cache even empty results to avoid repeated requests.
            exchangeRateCache.saveFluctuations(
                date = endDate,
                fluctuations = fluctuations
            )

            fluctuations
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            exchangeRateCache.saveFluctuations(
                date = endDate,
                fluctuations = emptyMap()
            )
            emptyMap()
        }

    }
}