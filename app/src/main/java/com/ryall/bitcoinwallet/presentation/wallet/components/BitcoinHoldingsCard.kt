package com.ryall.bitcoinwallet.presentation.wallet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ryall.bitcoinwallet.presentation.util.CurrencyFormatter
import com.ryall.bitcoinwallet.ui.theme.FlashDarkGreen
import com.ryall.bitcoinwallet.ui.theme.FlashLime
import com.ryall.bitcoinwallet.ui.theme.Spacing
import java.math.BigDecimal

@Composable
fun BitcoinHoldingsCard(
    bitcoinAmount:String,
    zarValue: BigDecimal?,
    onAmountChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = FlashDarkGreen
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.ExtraLarge),
            verticalArrangement = Arrangement.spacedBy(Spacing.ExtraLarge)
        ) {
            Text(
                text = "YOUR BITCOIN HOLDINGS",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
            OutlinedTextField(
                value = bitcoinAmount,
                onValueChange = onAmountChanged,
                label = { Text("Bitcoin amount") },
                suffix = { Text("BTC") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = FlashLime,
                    unfocusedLabelColor = Color.White,
                    focusedBorderColor = FlashLime,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                    focusedSuffixColor = FlashLime,
                    unfocusedSuffixColor = Color.White
                )
            )
            Text(
                text = "Enter up to 8 decimal places",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FlashLime
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.Large),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Small)
                ) {
                    Text(
                        text = "ESTIMATED VALUE IN ZAR",
                        style = MaterialTheme.typography.labelMedium,
                        color = FlashDarkGreen
                    )

                    Text(
                        text = zarValue?.let {
                            CurrencyFormatter.format(it, "ZAR")
                        } ?: "—",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = FlashDarkGreen
                    )
                }
            }
        }
    }
}