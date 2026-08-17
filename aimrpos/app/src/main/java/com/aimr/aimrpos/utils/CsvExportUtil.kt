package com.aimr.aimrpos.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.aimr.aimrpos.data.scanning.ScannedPage
import com.aimr.aimrpos.data.scanning.OcrOutputFormat
import com.aimr.aimrpos.data.scanning.DocumentScanner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CsvExportUtil(private val context: Context) {

    suspend fun exportToCsv(
        pages: List<ScannedPage>,
        fileName: String = "scan_export_${System.currentTimeMillis()}.csv"
    ): File = withContext(Dispatchers.IO) {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AIMR_Exports")
        dir.mkdirs()
        val file = File(dir, fileName)
        DocumentScanner().exportCsvFile(pages, file)
        file
    }

    suspend fun exportToText(
        pages: List<ScannedPage>,
        fileName: String = "scan_export_${System.currentTimeMillis()}.txt"
    ): File = withContext(Dispatchers.IO) {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AIMR_Exports")
        dir.mkdirs()
        val file = File(dir, fileName)
        DocumentScanner().exportTextFile(pages, file)
        file
    }

    fun getExportDir(): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "AIMR_Exports").apply { mkdirs() }
    }

    fun getScanDir(sessionId: String): File {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "AIMR_Scans/$sessionId")
        dir.mkdirs()
        return dir
    }

    fun createOutputFile(sessionId: String): File {
        val dir = getScanDir(sessionId)
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(dir, "scan_${timestamp}.jpg")
    }

    fun shareFile(file: File, mimeType: String = "text/csv") {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        val intent = android.content.Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Export"))
    }
}

class ScanSessionManager(private val context: Context) {

    private val csvExportUtil = CsvExportUtil(context)

    suspend fun createSession(name: String, mode: String = ScanMode.SIMPLE.code): com.aimr.aimrpos.data.scanning.ScanSession {
        val id = java.util.UUID.randomUUID().toString()
        val folderPath = csvExportUtil.getScanDir(id).absolutePath
        val session = com.aimr.aimrpos.data.scanning.ScanSession(
            id = id,
            sessionName = name,
            folderPath = folderPath,
            processingMode = mode
        )
        return session
    }

    suspend fun exportSession(
        pages: List<ScannedPage>,
        format: OcrOutputFormat
    ): File? = withContext(Dispatchers.IO) {
        when (format) {
            OcrOutputFormat.CSV -> csvExportUtil.exportToCsv(pages)
            OcrOutputFormat.EXCEL -> csvExportUtil.exportToCsv(pages, fileName = "scan_export_${System.currentTimeMillis()}.xls")
            OcrOutputFormat.TEXT -> csvExportUtil.exportToText(pages)
        }
    }

    fun copyTextToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("OCR Text", text)
        clipboard.setPrimaryClip(clip)
    }
}