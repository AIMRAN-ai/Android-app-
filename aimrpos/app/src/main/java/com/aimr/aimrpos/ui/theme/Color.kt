package com.aimr.aimrpos.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF1565C0)
val SecondaryAmber = Color(0xFFFF8F00)
val SurfaceWhite = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFD32F2F)
val LowStockYellow = Color(0xFFFFC107)
val OutOfStockRed = Color(0xFFD32F2F)
val InStockGreen = Color(0xFF388E3C)

val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryAmber,
    background = SurfaceWhite,
    surface = SurfaceWhite,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryAmber,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF2C2B2F),
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)