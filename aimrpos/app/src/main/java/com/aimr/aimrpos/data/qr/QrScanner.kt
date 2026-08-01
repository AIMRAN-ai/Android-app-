package com.aimr.aimrpos.data.qr

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

data class QrScanResult(
    val rawValue: String = "",
    val format: String = "",
    val displayName: String = "",
    val confidence: Float = 0.0f,
    val cornerPoints: List<Pair<Float, Float>> = emptyList()
)

class QrScanner {

    private val scanner = BarcodeScanning.getClient()

    fun processQrCode(bitmap: Bitmap): List<QrScanResult> {
        val results = mutableListOf<QrScanResult>()

        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val task = scanner.process(image)

            task.addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val result = QrScanResult(
                        rawValue = barcode.rawValue ?: "",
                        format = barcode.format.toString(),
                        displayName = barcode.displayValue ?: "",
                        confidence = barcode.cornerPoints?.let { 0.95f } ?: 0.0f,
                        cornerPoints = barcode.cornerPoints?.map { point ->
                            Pair(point.x.toFloat(), point.y.toFloat())
                        } ?: emptyList()
                    )
                    results.add(result)
                }
            }.addOnFailureListener { e ->
                Log.e("QrScanner", "Scan failed: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e("QrScanner", "Error processing image: ${e.message}")
        }

        return results
    }

    fun parseQrPayload(rawValue: String): Map<String, String> {
        val fields = mutableMapOf<String, String>()

        // Try JSON format
        try {
            if (rawValue.trim().startsWith("{")) {
                val json = rawValue.trim()
                val nameMatch = Regex(""""name"\s*:\s*"([^"]+)"""")
                val phoneMatch = Regex(""""phone"\s*:\s*"([^"]+)"""")
                val emailMatch = Regex(""""email"\s*:\s*"([^"]+)"""")
                val amountMatch = Regex(""""amount"\s*:\s*"([^"]+)"""")
                val invoiceMatch = Regex(""""invoice"\s*:\s*"([^"]+)"""")

                nameMatch.find(json)?.let { fields["name"] = it.groupValues[1] }
                phoneMatch.find(json)?.let { fields["phone"] = it.groupValues[1] }
                emailMatch.find(json)?.let { fields["email"] = it.groupValues[1] }
                amountMatch.find(json)?.let { fields["amount"] = it.groupValues[1] }
                invoiceMatch.find(json)?.let { fields["invoice"] = it.groupValues[1] }
            }
        } catch (e: Exception) {
            // Not JSON, try plain text parsing
        }

        // Try URL format (e.g., https://aimrpos.com/invoice/INV-001)
        val urlInvoiceMatch = Regex("""invoice[=/]([A-Z0-9\-]+)""", RegexOption.IGNORE_CASE)
        urlInvoiceMatch.find(rawValue)?.let { fields["invoice"] = it.groupValues[1] }

        // Try plain text with common patterns
        if (fields.isEmpty()) {
            fields["raw"] = rawValue
        }

        return fields
    }
}