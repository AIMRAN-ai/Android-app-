package com.aimr.aimrpos.ui.adaptive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker

enum class WindowSizeClass { Compact, Medium, Expanded }

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    return remember(screenWidth) {
        when {
            screenWidth < 600 -> WindowSizeClass.Compact
            screenWidth < 840 -> WindowSizeClass.Medium
            else -> WindowSizeClass.Expanded
        }
    }
}

data class FoldableState(
    val isFoldable: Boolean = false,
    val isBookFold: Boolean = false,
    val isTableTop: Boolean = false,
    val hingePosition: androidx.compose.ui.geometry.Rect? = null,
    val orientation: androidx.window.layout.FoldingFeature.Orientation? = null
)

@Composable
fun rememberFoldableState(): FoldableState {
    val context = LocalContext.current
    val windowInfoTracker = WindowInfoTracker.getOrCreate(context)
    val foldingFeature by remember { windowInfoTracker.windowLayoutInfo(context) }
        .let { flow ->
            androidx.compose.runtime.remember { kotlinx.coroutines.flow.MutableStateFlow<FoldingFeature?>(null) }
        }

    val state by remember {
        derivedStateOf {
            val feature = foldingFeature
            if (feature != null) {
                FoldableState(
                    isFoldable = true,
                    isBookFold = feature.orientation == FoldingFeature.Orientation.VERTICAL,
                    isTableTop = feature.orientation == FoldingFeature.Orientation.HORIZONTAL,
                    hingePosition = feature.bounds,
                    orientation = feature.orientation
                )
            } else {
                FoldableState()
            }
        }
    }

    return state
}

@Composable
fun AdaptiveLayout(
    compact: @Composable () -> Unit,
    medium: @Composable () -> Unit,
    expanded: @Composable () -> Unit
) {
    val windowSizeClass = rememberWindowSizeClass()
    when (windowSizeClass) {
        WindowSizeClass.Compact -> compact()
        WindowSizeClass.Medium -> medium()
        WindowSizeClass.Expanded -> expanded()
    }
}