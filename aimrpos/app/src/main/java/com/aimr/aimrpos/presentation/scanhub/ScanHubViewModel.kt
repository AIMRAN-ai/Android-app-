package com.aimr.aimrpos.presentation.scanhub

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.data.scanning.DocumentScanner
import com.aimr.aimrpos.data.scanning.OcrOutputFormat
import com.aimr.aimrpos.data.scanning.ScanMode
import com.aimr.aimrpos.data.scanning.ScannedPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanHubViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val scanner = DocumentScanner()

    private val _sessions = MutableStateFlow<List<com.aimr.aimrpos.data.scanning.ScanSession>>(emptyList())
    val sessions: StateFlow<List<com.aimr.aimrpos.data.scanning.ScanSession>> = _sessions.asStateFlow()

    private val _currentSession = MutableStateFlow<com.aimr.aimrpos.data.scanning.ScanSession?>(null)
    val currentSession: StateFlow<com.aimr.aimrpos.data.scanning.ScanSession?> = _currentSession.asStateFlow()

    private val _scannedPages = MutableStateFlow<List<ScannedPage>>(emptyList())
    val scannedPages: StateFlow<List<ScannedPage>> = _scannedPages.asStateFlow()

    private val _selectedMode = MutableStateFlow(ScanMode.SIMPLE)
    val selectedMode: StateFlow<ScanMode> = _selectedMode.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _selectedOcrFormat = MutableStateFlow(OcrOutputFormat.CSV)
    val selectedOcrFormat: StateFlow<OcrOutputFormat> = _selectedOcrFormat.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        loadSessions()
    }

    private fun loadSessions() {
        viewModelScope.launch {
            _sessions.value = emptyList()
        }
    }

    fun createSession(name: String, mode: ScanMode) {
        viewModelScope.launch {
            val session = com.aimr.aimrpos.data.scanning.ScanSession(
                id = "session_${System.currentTimeMillis()}",
                sessionName = name.ifBlank { "Scan Session ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}" },
                folderPath = File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES), "AIMR_Scans/${System.currentTimeMillis()}").absolutePath,
                processingMode = mode.code
            )
            _currentSession.value = session
            _scannedPages.value = emptyList()
            _statusMessage.value = "Session created: ${session.sessionName}"
        }
    }

    fun setMode(mode: ScanMode) {
        _selectedMode.value = mode
        _currentSession.value = _currentSession.value?.copy(processingMode = mode.code)
    }

    fun addImageFromCamera(bitmap: android.graphics.Bitmap) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            try {
                val dir = File(session.folderPath)
                dir.mkdirs()
                val file = File(dir, "scan_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { out ->
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                }
                val page = ScannedPage(
                    id = "page_${System.currentTimeMillis()}",
                    sessionId = session.id,
                    fileName = file.name,
                    filePath = file.absolutePath,
                    documentType = "UNKNOWN",
                    processingMode = _selectedMode.value.code,
                    pageNumber = _scannedPages.value.size + 1
                )
                _scannedPages.value = _scannedPages.value + page
                _statusMessage.value = "Image added: ${file.name}"
            } catch (e: Exception) {
                _statusMessage.value = "Failed to save image: ${e.message}"
            }
        }
    }

    fun addImagesFromUris(uris: List<Uri>) {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            uris.forEach { uri ->
                val bitmap = uriToBitmap(context, uri) ?: return@forEach
                addImageFromCamera(bitmap)
            }
        }
    }

    fun runOcrOnAll() {
        val session = _currentSession.value ?: return
        viewModelScope.launch {
            _isProcessing.value = true
            val processed = _scannedPages.value.toMutableList()
            processed.replaceAll { page ->
                val bitmap = BitmapFactory.decodeFile(page.filePath)
                if (bitmap != null) {
                    try {
                        val result = scanner.processImage(bitmap, _selectedMode.value)
                        page.copy(
                            ocrRawText = result.ocrRawText,
                            documentType = result.documentType,
                            extractedFields = result.extractedFields,
                            extractedFieldsCsv = result.extractedFieldsCsv,
                            processingMode = _selectedMode.value.code,
                            extractionStatus = result.extractionStatus,
                            confidence = result.confidence
                        )
                    } catch (e: Exception) {
                        page.copy(extractionStatus = "FAILED")
                    }
                } else page
            }
            _scannedPages.value = processed
            _isProcessing.value = false
            _statusMessage.value = "OCR completed on ${processed.size} pages (${_selectedMode.value.label})"
        }
    }

    fun exportOcr(format: OcrOutputFormat) {
        viewModelScope.launch {
            if (_scannedPages.value.isEmpty()) {
                _statusMessage.value = "No scanned pages to export"
                return@launch
            }
            try {
                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = when (format) {
                    OcrOutputFormat.CSV -> "scan_export_$timestamp.csv"
                    OcrOutputFormat.EXCEL -> "scan_export_$timestamp.xls"
                    OcrOutputFormat.TEXT -> "scan_export_$timestamp.txt"
                }
                val exportDir = File(context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS), "AIMR_Exports")
                exportDir.mkdirs()
                val file = File(exportDir, fileName)
                val resultFile = when (format) {
                    OcrOutputFormat.CSV -> scanner.exportCsvFile(_scannedPages.value, file)
                    OcrOutputFormat.EXCEL -> scanner.exportCsvFile(_scannedPages.value, file)
                    OcrOutputFormat.TEXT -> scanner.exportTextFile(_scannedPages.value, file)
                }
                shareFile(resultFile, format.mimeType)
                _statusMessage.value = "Exported: ${resultFile.absolutePath}"
            } catch (e: Exception) {
                _statusMessage.value = "Export failed: ${e.message}"
            }
        }
    }

    fun copyOcrTextToClipboard() {
        val text = _scannedPages.value.joinToString("\n\n") { page ->
            "=== Page ${page.pageNumber} ===\n${page.ocrRawText}"
        }
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("OCR Text", text)
        clipboard.setPrimaryClip(clip)
        _statusMessage.value = "Text copied to clipboard"
    }

    fun clearSession() {
        _currentSession.value = null
        _scannedPages.value = emptyList()
        _statusMessage.value = null
    }

    fun clearStatus() {
        _statusMessage.value = null
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun shareFile(file: File, mimeType: String) {
        val uri = androidx.core.content.FileProvider.getUriForFile(
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