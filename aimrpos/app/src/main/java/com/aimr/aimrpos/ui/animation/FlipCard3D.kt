package com.aimr.aimrpos.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun FlipCard3D(
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val rotation = remember { Animatable(0f) }
    var isFlipped by remember { mutableStateOf(false) }

    LaunchedEffect(isFlipped) {
        rotation.animateTo(
            targetValue = if (isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = 600)
        )
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = rotation.value
                    cameraDistance = 12 * density
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            isFlipped = !isFlipped
                        }
                    )
                },
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (rotation.value <= 90f) {
                    frontContent()
                } else {
                    Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                        backContent()
                    }
                }
            }
        }
    }
}

@Composable
fun ShimmerCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF1E293B),
                            Color(0xFF334155).copy(alpha = alpha),
                            Color(0xFF1E293B)
                        )
                    )
                )
        ) {
            content()
        }
    }
}