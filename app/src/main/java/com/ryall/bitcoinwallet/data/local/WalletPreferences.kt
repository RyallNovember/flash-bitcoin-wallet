package com.ryall.bitcoinwallet.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.walletDataStore by preferencesDataStore(
    name = "wallet_preferences"
)

@Singleton
class WalletPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private companion object {
        val BITCOIN_AMOUNT = stringPreferencesKey("bitcoin_amount")
    }

    val bitcoinAmount: Flow<String> =
        context.walletDataStore.data.map { preferences ->
            preferences[BITCOIN_AMOUNT] ?: "0"

        }

    suspend fun saveBitcoinAmount(amount: String) {
        context.walletDataStore.edit { preferences ->
            preferences[BITCOIN_AMOUNT] = amount
        }
    }
}