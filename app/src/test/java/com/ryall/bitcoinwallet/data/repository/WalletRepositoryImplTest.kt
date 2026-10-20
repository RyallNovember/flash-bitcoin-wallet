package com.ryall.bitcoinwallet.data.repository

import com.ryall.bitcoinwallet.data.local.ExchangeRateCache
import com.ryall.bitcoinwallet.data.remote.api.FixerApi
import com.ryall.bitcoinwallet.data.remote.dto.CurrencyRatesDto
import com.ryall.bitcoinwallet.data.remote.dto.FixerRatesDto
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import com.ryall.bitcoinwallet.domain.model.RateSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import kotlin.coroutines.cancellation.CancellationException

class WalletRepositoryImplTest {

    private val fixerApi = mockk<FixerApi>()
    private val exchangeRateCache = mockk<ExchangeRateCache>()

    private val repository = WalletRepositoryImpl(
        fixerApi = fixerApi,
        exchangeRateCache = exchangeRateCache
    )

    private val cachedRates = ExchangeRates(
        btc = BigDecimal("0.0000138"),
        zar = BigDecimal("18.46"),
        usd = BigDecimal("1.12"),
        aud = BigDecimal("1.6")
    )

    private val timestamp = 1_792_500_000_000L

    @Test
    fun `returns fresh cashed rates without calling API`() = runTest {
        coEvery { exchangeRateCache.getCachedRates() } returns cachedRates
        coEvery { exchangeRateCache.getLastUpdated() } returns timestamp

        val result = repository.getExchangeRates()

        assertEquals(RateSource.CACHED, result.source)
        assertEquals(cachedRates, result.rates)
        assertEquals(timestamp, result.lastUpdated)

        coVerify(exactly = 1) {
            exchangeRateCache.getCachedRates()
        }

        coVerify(exactly = 0) {
            fixerApi.getLatestRates(any())
        }

        coVerify(exactly = 0) {
            exchangeRateCache.saveRates(any())
        }
    }

    @Test
    fun `fetches live rates when cache is empty`() = runTest {
        coEvery {
            exchangeRateCache.getCachedRates()
        } returns null

        coEvery {
            exchangeRateCache.getLastUpdated()
        } returns timestamp

        coEvery {
            exchangeRateCache.saveRates(any())
        } returns Unit

        coEvery {
            fixerApi.getLatestRates(any())
        } returns FixerRatesDto(
            success = true,
            base = "EUR",
            rates = CurrencyRatesDto(
                btc = 0.0000138,
                zar = 18.46,
                usd = 1.12,
                aud = 1.60
            )
        )

        val result = repository.getExchangeRates()

        assertEquals(RateSource.LIVE, result.source)
        assertEquals(timestamp, result.lastUpdated)
        assertEquals(cachedRates, result.rates)

        coVerify(exactly = 1) {
            fixerApi.getLatestRates(any())
        }

        coVerify(exactly = 1) {
            exchangeRateCache.saveRates(cachedRates)
        }
    }


    @Test
    fun `returns stale rates when API fails`() = runTest {
        // Arrange: No fresh cached rates available
        coEvery {
            exchangeRateCache.getCachedRates()
        } returns null

        // Simulate an API failure
        coEvery {
            fixerApi.getLatestRates(any())
        } throws IOException("No internet connection")

        // Previously saved rates are still available
        coEvery {
            exchangeRateCache.getLastSavedRates()
        } returns cachedRates

        coEvery {
            exchangeRateCache.getLastUpdated()
        } returns timestamp

        val result = repository.getExchangeRates()

        assertEquals(RateSource.STALE, result.source)
        assertEquals(cachedRates, result.rates)
        assertEquals(timestamp, result.lastUpdated)

        // Verify the API was attempted
        coVerify(exactly = 1) {
            fixerApi.getLatestRates(any())
        }

        // Verify fallback cache was used
        coVerify(exactly = 1) {
            exchangeRateCache.getLastSavedRates()
        }

        // Failed requests must not overwrite saved rates
        coVerify(exactly = 0) {
            exchangeRateCache.saveRates(any())
        }
    }

    @Test
    fun `propagates coroutine cancellation`() = runTest {
        // Arrange: No fresh cached rates
        coEvery {
            exchangeRateCache.getCachedRates()
        } returns null

        // Simulate a cancelled API request
        coEvery {
            fixerApi.getLatestRates(any())
        } throws CancellationException("Request cancelled")

        // Act and assert
        try {
            repository.getExchangeRates()
            fail("Expected CancellationException")
        } catch (exception: CancellationException) {
            assertEquals("Request cancelled", exception.message)
        }

        // Cancellation must not trigger offline fallback
        coVerify(exactly = 0) {
            exchangeRateCache.getLastSavedRates()
        }

        // No new rates should be saved
        coVerify(exactly = 0) {
            exchangeRateCache.saveRates(any())
        }
    }
}