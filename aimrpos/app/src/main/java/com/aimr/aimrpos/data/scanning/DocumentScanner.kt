package com.aimr.aimrpos.data.scanning

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScanMode(val label: String, val code: String) {
    SIMPLE("Simple", "SIMPLE"),
    ENHANCED("Enhanced", "ENHANCED"),
    SUPER_MAGIC("Super Magic Enhanced", "SUPER_MAGIC")
}

enum class OcrOutputFormat(val label: String, val mimeType: String, val extension: String) {
    TEXT("Copy Text", "text/plain", "txt"),
    CSV("Export CSV", "text/csv", "csv"),
    EXCEL("Export Excel", "application/vnd.ms-excel", "xls")
}

data class ScanSession(
    val id: String = "",
    val sessionName: String = "",
    val folderPath: String = "",
    val documentTypeFilter: String = "ALL",
    val processingMode: String = ScanMode.SIMPLE.code,
    val createdAt: Long = 0L,
    val totalPages: Int = 0,
    val processedPages: Int = 0,
    val isCompleted: Boolean = false
)

data class ScannedPage(
    val id: String = "",
    val sessionId: String = "",
    val fileName: String = "",
    val filePath: String = "",
    val thumbnailPath: String? = null,
    val documentType: String = "UNKNOWN",
    val ocrRawText: String = "",
    val extractedFields: Map<String, String> = emptyMap(),
    val extractedFieldsCsv: String = "",
    val processingMode: String = ScanMode.SIMPLE.code,
    val extractionStatus: String = "PENDING",
    val pageNumber: Int = 1,
    val totalPages: Int = 1,
    val confidence: Float = 0f,
    val scannedAt: Long = System.currentTimeMillis()
)

class DocumentScanner {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun classifyDocument(text: String): String {
        val lower = text.lowercase()
        return when {
            "invoice" in lower || "فاتورہ" in lower || "invoice" in lower -> "INVOICE"
            "receipt" in lower || "رسید" in lower -> "RECEIPT"
            "delivery" in lower || "ڈلیوری" in lower -> "DELIVERY_NOTE"
            "cnic" in lower || "قومی شناختی" in lower || "identity" in lower -> "CNIC"
            "cheque" in lower || "چیک" in lower || "check" in lower -> "CHEQUE"
            "purchase" in lower || "خرید" in lower -> "PURCHASE_ORDER"
            "return" in lower || "واپسی" in lower -> "RETURN_INVOICE"
            "payment" in lower || "رقم" in lower || "amount" in lower -> "PAYMENT"
            else -> "UNKNOWN"
        }
    }

    fun extractFields(text: String, documentType: String): Map<String, String> {
        val fields = mutableMapOf<String, String>()

        val amountRegex = Regex("""\d{1,3}(?:[,\s]\d{3})*\.\d{2}""")
        val amounts = amountRegex.findAll(text).map { it.value.replace(",", "").replace(" ", "") }.toList()
        if (amounts.isNotEmpty()) {
            fields["total_amount"] = amounts.last()
            if (amounts.size > 1) fields["subtotal"] = amounts[0]
        }

        val dateRegex = Regex("""\d{1,2}[\/\-\.]\d{1,2}[\/\-\.]\d{2,4}""")
        val dates = dateRegex.findAll(text).map { it.value }.toList()
        if (dates.isNotEmpty()) fields["date"] = dates.first()

        val invoiceRegex = Regex("""(?:INV|inv|invoice)[\s\-]?\d+""", RegexOption.IGNORE_CASE)
        val invoices = invoiceRegex.findAll(text).map { it.value }.toList()
        if (invoices.isNotEmpty()) fields["invoice_number"] = invoices.first()

        val phoneRegex = Regex("""\+?\d{3}[\s\-]?\d{3}[\s\-]?\d{4}""")
        val phones = phoneRegex.findAll(text).map { it.value }.toList()
        if (phones.isNotEmpty()) fields["phone"] = phones.first()

        val cnicRegex = Regex("""\d{5}-\d{7}-\d{1,4}""")
        val cnic = cnicRegex.findAll(text).map { it.value }.toList()
        if (cnic.isNotEmpty()) fields["cnic"] = cnic.first()

        val nameRegex = Regex("""(?:name|customer|client|bill to)[\s:]+([A-Za-z\s]{3,30})""", RegexOption.IGNORE_CASE)
        val nameMatch = nameRegex.find(text)
        if (nameMatch != null) fields["customer_name"] = nameMatch.groupValues[1].trim()

        val addressRegex = Regex("""(?:address|addr|گھر)[\s:]+([A-Za-z0-9\s,\.\-]{5,50})""", RegexOption.IGNORE_CASE)
        val addressMatch = addressRegex.find(text)
        if (addressMatch != null) fields["address"] = addressMatch.groupValues[1].trim()

        return fields
    }

    suspend fun processImage(bitmap: Bitmap, mode: ScanMode = ScanMode.SIMPLE): ScannedPage {
        val preprocessed = when (mode) {
            ScanMode.SIMPLE -> bitmap
            ScanMode.ENHANCED -> preprocessEnhanced(bitmap)
            ScanMode.SUPER_MAGIC -> preprocessSuperMagic(bitmap)
        }

        val image = InputImage.fromBitmap(preprocessed, 0)
        val result = textRecognizer.process(image).await()
        val fullText = result.text
        val documentType = classifyDocument(fullText)
        val extractedFields = extractFields(fullText, documentType)
        val confidence = if (fullText.isNotBlank()) {
            result.textBlocks.map { it.confidence ?: 0f }.average().toFloat()
        } else 0f

        return ScannedPage(
            ocrRawText = fullText,
            documentType = documentType,
            extractedFields = extractedFields,
            extractedFieldsCsv = buildCsvFromFields(extractedFields, documentType),
            processingMode = mode.code,
            extractionStatus = if (fullText.isNotBlank()) "COMPLETED" else "FAILED",
            confidence = confidence
        )
    }

    private fun preprocessEnhanced(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val processed = Bitmap.createBitmap(width, height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(processed)
        val paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        val src = IntArray(width * height)
        bitmap.getPixels(src, 0, width, 0, 0, width, height)

        val contrast = 1.3f
        val brightness = 20
        val dst = IntArray(width * height)
        for (i in src.indices) {
            val p = src[i]
            val a = Color.alpha(p)
            var r = (Color.red(p) - 128) * contrast + 128 + brightness
            var g = (Color.green(p) - 128) * contrast + 128 + brightness
            var b = (Color.blue(p) - 128) * contrast + 128 + brightness
            r = r.coerceIn(0f, 255f)
            g = g.coerceIn(0f, 255f)
            b = b.coerceIn(0f, 255f)
            dst[i] = Color.argb(a, r.toInt(), g.toInt(), b.toInt())
        }

        val enhanced = Bitmap.createBitmap(dst, width, height, bitmap.config ?: Bitmap.Config.ARGB_8888)
        canvas.drawBitmap(enhanced, 0f, 0f, null)
        return processed
    }

    private fun preprocessSuperMagic(bitmap: Bitmap): Bitmap {
        val base = preprocessEnhanced(bitmap)
        val width = base.width
        val height = base.height
        val processed = Bitmap.createBitmap(width, height, base.config)
        val canvas = android.graphics.Canvas(processed)
        val src = IntArray(width * height)
        base.getPixels(src, 0, width, 0, 0, width, height)
        val dst = IntArray(width * height)
        for (i in src.indices) {
            val p = src[i]
            val a = Color.alpha(p)
            val r = Color.red(p)
            val g = Color.green(p)
            val b = Color.blue(p)
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            val inv = 255 - gray
            val thresh = if (inv > 128) 255 else 0
            dst[i] = Color.argb(a, thresh, thresh, thresh)
        }
        val bw = Bitmap.createBitmap(dst, width, height, base.config)
        canvas.drawBitmap(bw, 0f, 0f, null)
        return processed
    }

    private fun buildCsvFromFields(fields: Map<String, String>, documentType: String): String {
        val sb = StringBuilder()
        sb.append("field_name,field_value,document_type,extracted_at\n")
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        fields.forEach { (key, value) ->
            val escaped = value.replace("\"", "\"\"")
            sb.append("\"$key\",\"$escaped\",\"$documentType\",\"$dateStr\"\n")
        }
        return sb.toString()
    }

    suspend fun exportCsvFile(pages: List<ScannedPage>, outputFile: File): File {
        val sb = StringBuilder()
        sb.append("page_number,file_name,document_type,field_name,field_value,confidence,processing_mode,extraction_status\n")
        val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        pages.forEach { page ->
            page.extractedFields.forEach { (key, value) ->
                val escaped = value.replace("\"", "\"\"")
                sb.append("\"${page.pageNumber}\",\"${page.fileName}\",\"${page.documentType}\",\"$key\",\"$escaped\",\"${page.confidence}\",\"${page.processingMode}\",\"${page.extractionStatus}\",\"$dateStr\"\n")
            }
        }
        FileOutputStream(outputFile).use { it.write(sb.toString().toByteArray(Charsets.UTF_8)) }
        return outputFile
    }

    suspend fun exportTextFile(pages: List<ScannedPage>, outputFile: File): File {
        val sb = StringBuilder()
        pages.forEach { page ->
            sb.append("=== Page ${page.pageNumber} ===\n")
            sb.append("Document Type: ${page.documentType}\n")
            sb.append("Confidence: ${page.confidence}\n")
            sb.append("Processing Mode: ${page.processingMode}\n\n")
            sb.append("--- OCR Text ---\n")
            sb.append(page.ocrRawText)
            sb.append("\n\n--- Extracted Fields ---\n")
            page.extractedFields.forEach { (key, value) ->
                sb.append("$key: $value\n")
            }
            sb.append("\n")
        }
        FileOutputStream(outputFile).use { it.write(sb.toString().toByteArray(Charsets.UTF_8)) }
        return outputFile
    }
}