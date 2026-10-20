package com.ryall.bitcoinwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ryall.bitcoinwallet.presentation.wallet.SplashScreen
import com.ryall.bitcoinwallet.presentation.wallet.WalletScreen
import com.ryall.bitcoinwallet.presentation.wallet.WalletViewModel
import com.ryall.bitcoinwallet.ui.theme.BitcoinWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BitcoinWalletTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showSplash by rememberSaveable {
                        mutableStateOf(true)
                    }

                    if (showSplash) {
                        SplashScreen(
                            onAnimationFinished = {
                                showSplash = false
                            }
                        )
                    } else {
                        WalletScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}