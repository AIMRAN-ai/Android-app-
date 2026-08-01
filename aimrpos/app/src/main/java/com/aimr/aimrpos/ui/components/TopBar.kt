package com.aimr.aimrpos.ui.components

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
        )
    )
}