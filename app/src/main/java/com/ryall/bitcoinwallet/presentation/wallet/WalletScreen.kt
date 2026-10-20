package com.ryall.bitcoinwallet.presentation.wallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ryall.bitcoinwallet.presentation.wallet.components.BitcoinHoldingsCard
import com.ryall.bitcoinwallet.presentation.wallet.components.CurrencyValueCard
import com.ryall.bitcoinwallet.presentation.wallet.components.ExchangeRateStatusCard
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ryall.bitcoinwallet.R
import com.ryall.bitcoinwallet.ui.theme.Spacing

@Composable
fun WalletScreen(
    viewModel: WalletViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val converted = uiState.convertedBitcoin
    val isDarkTheme = isSystemInDarkTheme()
    val logoRes = if (isDarkTheme) {
        R.drawable.flash_logo_dark
    } else {
        R.drawable.flash_logo_light
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.ExtraLarge),
        verticalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
        ) {
            Image(
                painter = painterResource(logoRes),
                contentDescription = "Flash logo",
                modifier = Modifier
                    .width(160.dp)
                    .height(65.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Bitcoin Wallet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        BitcoinHoldingsCard(
            bitcoinAmount = uiState.bitcoinAmount,
            zarValue = converted?.zar,
            onAmountChanged = viewModel::onBitcoinAmountChanged
        )
        Text(
            text = "Your currency values",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        CurrencyValueCard(
            currencyCode = "ZAR",
            currencyName = "South African Rand",
            value = converted?.zar
        )
        CurrencyValueCard(
            currencyCode = "USD",
            currencyName = "US Dollar",
            value = converted?.usd
        )
        CurrencyValueCard(
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            value = converted?.aud
        )
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )
            Button(onClick = viewModel::fetchExchangeRates) {
                Text("Retry")
            }
        }
        if (uiState.rateSource != null && uiState.lastUpdated != null) {
            ExchangeRateStatusCard(
                source = uiState.rateSource,
                lastUpdated = uiState.lastUpdated
            )
        }
        Text(
            text = "Values are estimates based on available exchange rates.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}