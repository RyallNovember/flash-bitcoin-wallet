package com.ryall.bitcoinwallet.presentation.wallet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ryall.bitcoinwallet.R

private const val SPLASH_ANIMATION_DURATION_MS = 1200
private const val FULL_ROTATION_DEGREES = 360f
private const val CAMERA_DISTANCE = 12f

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = FULL_ROTATION_DEGREES,
            animationSpec = tween(durationMillis = SPLASH_ANIMATION_DURATION_MS)
        )
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.flash_cow_splash_512),
            contentDescription = "Flash",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = CAMERA_DISTANCE * density
                },
            contentScale = ContentScale.Fit
        )
    }
}