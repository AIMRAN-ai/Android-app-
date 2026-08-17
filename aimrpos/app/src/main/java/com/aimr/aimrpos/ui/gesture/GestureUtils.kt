package com.aimr.aimrpos.ui.gesture

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

object GestureUtils {

    const val SWIPE_THRESHOLD = 100f
    const val TAP_THRESHOLD = 10f
}

@Composable
fun Modifier.swipeToNavigate(
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    onSwipeUp: (() -> Unit)? = null,
    onSwipeDown: (() -> Unit)? = null
): Modifier {
    var startX by remember { mutableFloatStateOf(0f) }
    var startY by remember { mutableFloatStateOf(0f) }

    return this.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = { startX = it.x },
            onDragEnd = {
                val dragAmount = startX - it.x
                if (abs(dragAmount) > GestureUtils.SWIPE_THRESHOLD) {
                    if (dragAmount > 0) {
                        onSwipeRight?.invoke()
                    } else {
                        onSwipeLeft?.invoke()
                    }
                }
            }
        )
    }
}

@Composable
fun Modifier.pinchToZoom(
    onZoom: (Float) -> Unit,
    onZoomReset: (() -> Unit)? = null
): Modifier {
    var scale by remember { mutableFloatStateOf(1f) }

    return this.pointerInput(Unit) {
        detectTransformGestures { _, pan, zoom, _ ->
            scale = (scale * zoom).coerceIn(0.5f, 3f)
            onZoom(scale)
            if (scale == 1f) {
                onZoomReset?.invoke()
            }
        }
    }
}

@Composable
fun Modifier.doubleTapToReset(
    onDoubleTap: (() -> Unit)? = null
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = { onDoubleTap?.invoke() }
        )
    }
}

@Composable
fun Modifier.longPressToAction(
    onLongPress: (Offset) -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onLongPress = onLongPress
        )
    }
}