package com.aimr.aimrpos.ui.shortcuts

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.ContextCompat
import com.aimr.aimrpos.R

object AppShortcuts {
    private const val SHORTCUT_ID_NEW_INVOICE = "new_invoice"
    private const val SHORTCUT_ID_SCAN = "scan_product"
    private const val SHORTCUT_ID_ADD_PRODUCT = "add_product"

    fun setupShortcuts(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)
            if (shortcutManager == null || !shortcutManager.isRequestPinShortcutSupported) return

            val shortcuts = mutableListOf<ShortcutInfo>()

            val newInvoiceShortcut = ShortcutInfo.Builder(context, SHORTCUT_ID_NEW_INVOICE)
                .setShortLabel("New Invoice")
                .setLongLabel("Create a new invoice")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_invoice))
                .setIntent(android.content.Intent(context, com.aimr.aimrpos.MainActivity::class.java).apply {
                    action = "ACTION_NEW_INVOICE"
                })
                .build()

            val scanShortcut = ShortcutInfo.Builder(context, SHORTCUT_ID_SCAN)
                .setShortLabel("Scan")
                .setLongLabel("Scan product barcode")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_scan))
                .setIntent(android.content.Intent(context, com.aimr.aimrpos.MainActivity::class.java).apply {
                    action = "ACTION_SCAN"
                })
                .build()

            val addProductShortcut = ShortcutInfo.Builder(context, SHORTCUT_ID_ADD_PRODUCT)
                .setShortLabel("Add Product")
                .setLongLabel("Add new product")
                .setIcon(Icon.createWithResource(context, R.drawable.ic_shortcut_add))
                .setIntent(android.content.Intent(context, com.aimr.aimrpos.MainActivity::class.java).apply {
                    action = "ACTION_ADD_PRODUCT"
                })
                .build()

            shortcuts.add(newInvoiceShortcut)
            shortcuts.add(scanShortcut)
            shortcuts.add(addProductShortcut)

            shortcutManager.addDynamicShortcuts(shortcuts)
        }
    }
}