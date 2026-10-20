package com.ryall.bitcoinwallet.presentation.wallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ryall.bitcoinwallet.domain.model.RateSource
import com.ryall.bitcoinwallet.ui.theme.Spacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExchangeRateStatusCard(
    source: RateSource,
    lastUpdated: Long
) {
    val (statusText, statusColor) = when (source) {
        RateSource.LIVE -> "Live rates" to MaterialTheme.colorScheme.primary
        RateSource.CACHED -> "Cached rates" to MaterialTheme.colorScheme.primary
        RateSource.STALE -> "Outdated rates" to MaterialTheme.colorScheme.error
    }

    val formattedDate = SimpleDateFormat(
        "dd MMM yyyy, HH:mm",
        Locale.getDefault()
    ).format(Date(lastUpdated))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Large),
            verticalArrangement = Arrangement.spacedBy(Spacing.Small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exchange rate status",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }

            Text(
                text = "Last updated: $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (source == RateSource.STALE) {
                Text(
                    text = "Unable to refresh rates. Showing previously saved values, which may differ from current market prices.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}