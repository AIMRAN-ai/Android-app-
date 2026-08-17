package com.aimr.aimrpos.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.aimr.aimrpos.domain.model.Product
import java.io.File

class ExtensionManager(private val context: Context) {
    private val extensions = mutableMapOf<String, DataSourceExtension>()

    fun register(extension: DataSourceExtension) {
        extensions[extension.id] = extension
    }

    fun getExtension(id: String): DataSourceExtension? = extensions[id]

    fun getAllExtensions(): List<DataSourceExtension> = extensions.values.toList()

    fun loadFromAllSources(currentProducts: List<Product>): List<Product> {
        return extensions.values.flatMap { it.loadProducts(currentProducts) }
    }

    fun shareFile(file: File, mimeType: String = "application/pdf") {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }

    fun openPdf(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }
}

interface DataSourceExtension {
    val id: String
    val name: String
    val description: String
    val iconRes: Int
    fun loadProducts(existing: List<Product>): List<Product>
    fun isAvailable(context: Context): Boolean = true
}

class CsvImportExtension : DataSourceExtension {
    override val id = "csv_import"
    override val name = "CSV Import"
    override val description = "Import products from CSV file"
    override val iconRes = android.R.drawable.ic_menu_save
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class ApiSyncExtension : DataSourceExtension {
    override val id = "api_sync"
    override val name = "API Sync"
    override val description = "Sync products from remote API"
    override val iconRes = android.R.drawable.ic_menu_upload
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class BarcodeScannerExtension : DataSourceExtension {
    override val id = "barcode_scanner"
    override val name = "Barcode Scanner"
    override val description = "Scan barcodes to add products"
    override val iconRes = android.R.drawable.ic_menu_camera
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class QrScannerExtension : DataSourceExtension {
    override val id = "qr_scanner"
    override val name = "QR Scanner"
    override val description = "Scan QR codes to add products"
    override val iconRes = android.R.drawable.ic_menu_camera
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class ManualEntryExtension : DataSourceExtension {
    override val id = "manual_entry"
    override val name = "Manual Entry"
    override val description = "Manually add products"
    override val iconRes = android.R.drawable.ic_menu_edit
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class ExcelImportExtension : DataSourceExtension {
    override val id = "excel_import"
    override val name = "Excel Import"
    override val description = "Import products from Excel file"
    override val iconRes = android.R.drawable.ic_menu_save
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class SmsSyncExtension : DataSourceExtension {
    override val id = "sms_sync"
    override val name = "SMS Sync"
    override val description = "Sync orders from SMS"
    override val iconRes = android.R.drawable.ic_menu_send
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}

class WebPortalExtension : DataSourceExtension {
    override val id = "web_portal"
    override val name = "Web Portal"
    override val description = "Sync from web dashboard"
    override val iconRes = android.R.drawable.ic_menu_upload
    override fun loadProducts(existing: List<Product>): List<Product> = emptyList()
}