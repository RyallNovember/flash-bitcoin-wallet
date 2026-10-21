package com.ryall.bitcoinwallet.presentation.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryall.bitcoinwallet.data.local.WalletPreferences
import com.ryall.bitcoinwallet.domain.model.ExchangeRates
import com.ryall.bitcoinwallet.domain.repository.WalletRepository
import com.ryall.bitcoinwallet.domain.usecase.GetWalletValueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val getWalletValueUseCase: GetWalletValueUseCase,
    private val walletPreferences: WalletPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState

    init {
        observeBitcoinAmount()
        fetchExchangeRates()
        fetchDailyFluctuations()
    }

    private fun observeBitcoinAmount() {
        viewModelScope.launch {
            walletPreferences.bitcoinAmount.collectLatest { amount ->
                _uiState.update {
                    it.copy(bitcoinAmount = amount)
                }
                _uiState.value.exchangeRates?.let { rates ->
                    calculateBitcoinValue(rates)
                }

            }
        }
    }

    fun fetchExchangeRates() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val result = walletRepository.getExchangeRates()

                val rates = result.rates
                val source = result.source
                val lastUpdated = result.lastUpdated

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        exchangeRates = rates,
                        rateSource = source,
                        lastUpdated = lastUpdated,
                        errorMessage = null
                    )
                }

                calculateBitcoinValue(rates)

            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Unable to load exchange rates"
                    )
                }
            }
        }
    }

    fun onBitcoinAmountChanged(amount: String) {
        if (!amount.matches(Regex("""\d*(\.\d{0,8})?"""))) {
            return
        }
        _uiState.update {
            it.copy(bitcoinAmount = amount)
        }

        _uiState.value.exchangeRates?.let { rates ->
            calculateBitcoinValue(rates)
        }

        viewModelScope.launch {
            walletPreferences.saveBitcoinAmount(amount)
        }
    }

    private fun calculateBitcoinValue(rates: ExchangeRates) {
        val amount = _uiState.value.bitcoinAmount
            .toBigDecimalOrNull()

        Log.d(
            "BitcoinDebug",
            "Amount=$amount, BTC=${rates.btc}, " +
                    "ZAR=${rates.zar}, USD=${rates.usd}, AUD=${rates.aud}"
        )
        val converted = amount
            ?.takeIf { it >= BigDecimal.ZERO }
            ?.let { getWalletValueUseCase(it, rates) }

        _uiState.update {
            it.copy(convertedBitcoin = converted)
        }
    }

    private fun fetchDailyFluctuations() {
        viewModelScope.launch {
            val today = LocalDate.now(ZoneOffset.UTC)
            val yesterday = today.minusDays(1)

            try {
                val fluctuations = walletRepository.getDailyFluctuations(
                    startDate = yesterday.toString(),
                    endDate = today.toString()
                )

                _uiState.update {
                    it.copy(dailyFluctuations = fluctuations)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(dailyFluctuations = emptyMap())
                }
            }
        }
    }

}