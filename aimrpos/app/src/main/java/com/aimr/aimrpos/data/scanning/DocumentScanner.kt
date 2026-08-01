package com.aimr.aimrpos.data.scanning

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

data class ScannedDocument(
    val bitmap: Bitmap,
    val ocrText: String = "",
    val documentType: String = "UNKNOWN",
    val extractedFields: Map<String, String> = emptyMap(),
    val confidence: Float = 0.0f
)

class DocumentScanner {

    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun classifyDocument(text: String): String {
        return when {
            text.contains("invoice|invoice|فواتیر", ignoreCase = true) -> "INVOICE"
            text.contains("receipt|receipt|رسید", ignoreCase = true) -> "RECEIPT"
            text.contains("delivery|delivery|ڈلیوری", ignoreCase = true) -> "DELIVERY_NOTE"
            text.contains("cnic|cnic|قومی شناختی", ignoreCase = true) -> "CNIC"
            text.contains("cheque|cheque|چیک", ignoreCase = true) -> "CHEQUE"
            text.contains("purchase|purchase|خرید", ignoreCase = true) -> "PURCHASE_ORDER"
            else -> "UNKNOWN"
        }
    }

    fun extractFields(text: String, documentType: String): Map<String, String> {
        val fields = mutableMapOf<String, String>()

        val amountRegex = Regex("""\d+[\s,]?\d*\.?\d+""")
        val amounts = amountRegex.findAll(text).map { it.value.replace(",", "").replace(" ", "") }.toList()
        if (amounts.isNotEmpty()) {
            fields["total_amount"] = amounts.last()
            if (amounts.size > 1) fields["subtotal"] = amounts[0]
        }

        val dateRegex = Regex("""\d{1,2}[/\-]\d{1,2}[/\-]\d{2,4}""")
        val dates = dateRegex.findAll(text).map { it.value }.toList()
        if (dates.isNotEmpty()) fields["date"] = dates.first()

        val invoiceRegex = Regex("""INV[- ]?\d+""", RegexOption.IGNORE_CASE)
        val invoices = invoiceRegex.findAll(text).map { it.value }.toList()
        if (invoices.isNotEmpty()) fields["invoice_number"] = invoices.first()

        val phoneRegex = Regex("""\d{3}[-\s]?\d{3}[-\s]?\d{4}""")
        val phones = phoneRegex.findAll(text).map { it.value }.toList()
        if (phones.isNotEmpty()) fields["phone"] = phones.first()

        val cnicRegex = Regex("""\d{5}-\d{7}-\d{4}""")
        val cnic = cnicRegex.findAll(text).map { it.value }.toList()
        if (cnic.isNotEmpty()) fields["cnic"] = cnic.first()

        return fields
    }

    suspend fun processImage(bitmap: Bitmap): ScannedDocument {
        val image = InputImage.fromBitmap(bitmap, 0)
        val result = textRecognizer.process(image)
            .await()

        val fullText = result.text
        val documentType = classifyDocument(fullText)
        val extractedFields = extractFields(fullText, documentType)

        return ScannedDocument(
            bitmap = bitmap,
            ocrText = fullText,
            documentType = documentType,
            extractedFields = extractedFields,
            confidence = if (fullText.isNotEmpty()) 0.85f else 0.0f
        )
    }
}