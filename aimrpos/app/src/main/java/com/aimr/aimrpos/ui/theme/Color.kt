package com.aimr.aimrpos.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PrimaryBlue = Color(0xFF3B82F6)
val PrimaryDark = Color(0xFF0F172A)
val SecondaryAmber = Color(0xFFF59E0B)
val SuccessGreen = Color(0xFF10B981)
val ErrorRed = Color(0xFFEF4444)
val WarningYellow = Color(0xFFF59E0B)
val BackgroundDark = Color(0xFF0F172A)
val SurfaceDark = Color(0xFF1E293B)
val SurfaceLight = Color(0xFFF8FAFC)
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)
val BorderColor = Color(0xFF334155)
val CardDark = Color(0xFF1E293B)
val InStockGreen = Color(0xFF10B981)
val LowStockYellow = Color(0xFFF59E0B)
val OutOfStockRed = Color(0xFFEF4444)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryAmber,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderColor
)

val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryAmber,
    background = SurfaceLight,
    surface = Color.White,
    error = ErrorRed,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    outline = BorderColor
)