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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ryall.bitcoinwallet.presentation.util.CurrencyFormatter
import com.ryall.bitcoinwallet.ui.theme.Spacing
import java.math.BigDecimal

@Composable
fun CurrencyValueCard(
    currencyCode: String,
    currencyName: String,
    value: BigDecimal?,
    dailyChangePercent: BigDecimal? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.ExtraLarge),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Text(
                    text = currencyCode,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currencyName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(Spacing.ExtraSmall)
            ) {
                Text(
                    text = value?.let {
                        CurrencyFormatter.format(it, currencyCode)
                    } ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (dailyChangePercent != null && value != null) {
                    val direction = dailyChangePercent.signum()

                    val changeColor = when {
                        direction > 0 -> Color(0xFF2E7D32)
                        direction < 0 -> Color(0xFFC62828)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    val indicator = when {
                        direction > 0 -> "▲ +"
                        direction < 0 -> "▼ "
                        else -> "● "
                    }

                    Text(
                        text = "$indicator${dailyChangePercent.toPlainString()}% since yesterday",
                        style = MaterialTheme.typography.labelSmall,
                        color = changeColor
                    )
                }
            }
        }
    }
}