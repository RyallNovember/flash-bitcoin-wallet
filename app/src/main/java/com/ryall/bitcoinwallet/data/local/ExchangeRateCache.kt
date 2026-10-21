package com.ryall.bitcoinwallet.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

private val Context.exchangeRateDataStore by preferencesDataStore(
    name = "exchange_rate_cache"
)

@Singleton
class ExchangeRateCache @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        val BTC = stringPreferencesKey("btc_rate")
        val ZAR = stringPreferencesKey("zar_rate")
        val USD = stringPreferencesKey("usd_rate")
        val AUD = stringPreferencesKey("aud_rate")
        val LAST_UPDATED = longPreferencesKey("last_updated")

        val FLUCTUATION_DATE = stringPreferencesKey("fluctuation_date")
        val FLUCTUATION_ZAR = stringPreferencesKey("fluctuation_zar")
        val FLUCTUATION_USD = stringPreferencesKey("fluctuation_usd")
        val FLUCTUATION_AUD = stringPreferencesKey("fluctuation_aud")

        const val CACHE_DURATION_MS = 60 * 60 * 1000L
    }

    suspend fun getCachedRates(): ExchangeRates? {
        val preferences = context.exchangeRateDataStore.data.first()

        val lastUpdated = preferences[LAST_UPDATED] ?: return null
        val age = System.currentTimeMillis() - lastUpdated

        if (age < 0 || age >= CACHE_DURATION_MS) {
            return null
        }

        return readRates(preferences)
    }

    suspend fun getLastSavedRates(): ExchangeRates? {
        val preferences = context.exchangeRateDataStore.data.first()
        return readRates(preferences)
    }

    private fun readRates(
        preferences: androidx.datastore.preferences.core.Preferences
    ): ExchangeRates? {
        val btc = preferences[BTC]?.toBigDecimalOrNull() ?: return null
        val zar = preferences[ZAR]?.toBigDecimalOrNull() ?: return null
        val usd = preferences[USD]?.toBigDecimalOrNull() ?: return null
        val aud = preferences[AUD]?.toBigDecimalOrNull() ?: return null

        if (btc <= BigDecimal.ZERO ||
            zar <= BigDecimal.ZERO ||
            usd <= BigDecimal.ZERO ||
            aud <= BigDecimal.ZERO
        ) {
            return null
        }

        return ExchangeRates(
            btc = btc,
            zar = zar,
            usd = usd,
            aud = aud
        )
    }

    suspend fun saveRates(rates: ExchangeRates) {
        context.exchangeRateDataStore.edit { preferences ->
            preferences[BTC] = rates.btc.toPlainString()
            preferences[ZAR] = rates.zar.toPlainString()
            preferences[USD] = rates.usd.toPlainString()
            preferences[AUD] = rates.aud.toPlainString()
            preferences[LAST_UPDATED] = System.currentTimeMillis()
        }
    }

    suspend fun getLastUpdated(): Long? {
        val preferences = context.exchangeRateDataStore.data.first()
        return preferences[LAST_UPDATED]
    }

    suspend fun hasFluctuationCacheForDate(date: String): Boolean {
        val preferences = context.exchangeRateDataStore.data.first()
        return preferences[FLUCTUATION_DATE] == date
    }

    suspend fun getCachedFluctuations(
        date: String
    ): Map<String, BigDecimal>? {
        val preferences = context.exchangeRateDataStore.data.first()

        if (preferences[FLUCTUATION_DATE] != date) {
            return null
        }

        val result = mutableMapOf<String, BigDecimal>()

        preferences[FLUCTUATION_ZAR]
            ?.toBigDecimalOrNull()
            ?.let { result["ZAR"] = it }

        preferences[FLUCTUATION_USD]
            ?.toBigDecimalOrNull()
            ?.let { result["USD"] = it }

        preferences[FLUCTUATION_AUD]
            ?.toBigDecimalOrNull()
            ?.let { result["AUD"] = it }

        return result
    }

    suspend fun saveFluctuations(
        date: String,
        fluctuations: Map<String, BigDecimal>
    ) {
        context.exchangeRateDataStore.edit { preferences ->
            preferences[FLUCTUATION_DATE] = date

            preferences.remove(FLUCTUATION_ZAR)
            preferences.remove(FLUCTUATION_USD)
            preferences.remove(FLUCTUATION_AUD)

            fluctuations["ZAR"]?.let {
                preferences[FLUCTUATION_ZAR] = it.toPlainString()
            }

            fluctuations["USD"]?.let {
                preferences[FLUCTUATION_USD] = it.toPlainString()
            }

            fluctuations["AUD"]?.let {
                preferences[FLUCTUATION_AUD] = it.toPlainString()
            }
        }
    }
}