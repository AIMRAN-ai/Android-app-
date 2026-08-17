package com.aimr.aimrpos.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import com.aimr.aimrpos.domain.model.Invoice
import com.aimr.aimrpos.domain.model.InvoiceItem
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReceiptGenerator(private val context: Context) {

    fun generatePdfReceipt(
        invoice: Invoice,
        items: List<InvoiceItem>,
        businessName: String = "AIMRAN POS"
    ): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
        }

        var y = 20f

        canvas.drawText(businessName, 20f, y, paint.apply { textSize = 16f; isFakeBoldText = true })
        y += 30f
        canvas.drawText("INVOICE", 20f, y, paint.apply { textSize = 14f; isFakeBoldText = true })
        y += 25f

        canvas.drawText("Invoice #: ${invoice.invoiceNumber}", 20f, y, paint.apply { textSize = 12f; isFakeBoldText = false })
        y += 20f

        val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        canvas.drawText("Date: ${dateFormat.format(Date(invoice.updatedAt))}", 20f, y, paint)
        y += 20f

        if (!invoice.customerName.isNullOrEmpty()) {
            canvas.drawText("Customer: ${invoice.customerName}", 20f, y, paint)
            y += 20f
        }

        y += 10f
        canvas.drawLine(20f, y, 280f, y, paint)
        y += 20f

        canvas.drawText("Item", 20f, y, paint.apply { isFakeBoldText = true })
        canvas.drawText("Qty", 150f, y, paint)
        canvas.drawText("Price", 200f, y, paint)
        canvas.drawText("Total", 250f, y, paint)
        y += 20f

        items.forEach { item ->
            canvas.drawText(item.productName.take(15), 20f, y, paint)
            canvas.drawText(item.quantity.toString(), 150f, y, paint)
            canvas.drawText("Rs ${"%.2f".format(item.unitPrice)}", 200f, y, paint)
            canvas.drawText("Rs ${"%.2f".format(item.lineTotal)}", 250f, y, paint)
            y += 18f
        }

        y += 10f
        canvas.drawLine(20f, y, 280f, y, paint)
        y += 20f

        canvas.drawText("Subtotal: Rs ${"%.2f".format(invoice.subtotal)}", 20f, y, paint)
        y += 18f
        canvas.drawText("Tax (${invoice.paymentMethod}): Rs ${"%.2f".format(invoice.taxAmount)}", 20f, y, paint)
        y += 18f
        if (invoice.discount > 0) {
            canvas.drawText("Discount: Rs ${"%.2f".format(invoice.discount)}", 20f, y, paint)
            y += 18f
        }
        canvas.drawText("TOTAL: Rs ${"%.2f".format(invoice.total)}", 20f, y, paint.apply { isFakeBoldText = true })
        y += 25f

        canvas.drawText("Payment: ${invoice.paymentMethod}", 20f, y, paint)
        y += 18f
        canvas.drawText("Status: ${invoice.paymentStatus}", 20f, y, paint)

        document.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "receipt_${invoice.invoiceNumber}.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()
        return file
    }

    fun shareViaWhatsApp(invoice: Invoice, items: List<InvoiceItem>, businessName: String = "AIMRAN POS") {
        val message = buildString {
            appendLine("🛒 *$businessName*")
            appendLine("📄 Invoice: ${invoice.invoiceNumber}")
            appendLine("📅 ${SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(invoice.updatedAt))}")
            appendLine()
            appendLine("*Items:*")
            items.forEach { item ->
                appendLine("• ${item.productName} x${item.quantity} = Rs ${"%.2f".format(item.lineTotal)}")
            }
            appendLine()
            appendLine("Subtotal: Rs ${"%.2f".format(invoice.subtotal)}")
            appendLine("Tax: Rs ${"%.2f".format(invoice.taxAmount)}")
            if (invoice.discount > 0) appendLine("Discount: Rs ${"%.2f".format(invoice.discount)}")
            appendLine("*Total: Rs ${"%.2f".format(invoice.total)}*")
            appendLine("Payment: ${invoice.paymentMethod}")
            appendLine("Status: ${invoice.paymentStatus}")
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
            `package` = "com.whatsapp"
        }
        context.startActivity(intent)
    }

    fun printReceipt(invoice: Invoice, items: List<InvoiceItem>, businessName: String = "AIMRAN POS") {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as android.print.PrintManager
        val printAdapter = object : android.print.PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: android.print.PrintAttributes?,
                newAttributes: android.print.PrintAttributes,
                cancellationSignal: android.os.CancellationSignal,
                callback: android.print.PrintDocumentAdapter.LayoutResultCallback,
                extras: android.os.Bundle?
            ) {
                val builder = android.print.PrintAttributes.Builder()
                builder.setMediaSize(android.print.PrintAttributes.MediaSize.NA_LETTER)
                builder.setResolution(android.print.PrintAttributes.Resolution("receipt", "receipt", 300, 300))
                callback.onLayoutFinished(newAttributes, true)
            }

            override fun onWrite(
                pages: Array<android.print.PageRange>,
                destination: android.print.PdfDocument,
                cancellationSignal: android.os.CancellationSignal,
                callback: android.print.WriteResultCallback
            ) {
                val pageInfo = android.print.PdfDocument.PageInfo.Builder(300, 600, 1).create()
                val page = destination.startPage(pageInfo)
                val canvas = page.canvas

                val paint = Paint().apply { color = Color.BLACK; textSize = 12f }
                var y = 20f

                canvas.drawText(businessName, 20f, y, paint.apply { textSize = 16f; isFakeBoldText = true })
                y += 30f
                canvas.drawText("INVOICE", 20f, y, paint.apply { textSize = 14f; isFakeBoldText = true })
                y += 25f
                canvas.drawText("Invoice #: ${invoice.invoiceNumber}", 20f, y, paint)
                y += 20f

                val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                canvas.drawText("Date: ${dateFormat.format(Date(invoice.updatedAt))}", 20f, y, paint)
                y += 20f

                if (!invoice.customerName.isNullOrEmpty()) {
                    canvas.drawText("Customer: ${invoice.customerName}", 20f, y, paint)
                    y += 20f
                }

                y += 10f
                canvas.drawLine(20f, y, 280f, y, paint)
                y += 20f

                items.forEach { item ->
                    canvas.drawText("${item.productName} x${item.quantity}", 20f, y, paint)
                    canvas.drawText("Rs ${"%.2f".format(item.lineTotal)}", 250f, y, paint)
                    y += 18f
                }

                y += 10f
                canvas.drawLine(20f, y, 280f, y, paint)
                y += 20f
                canvas.drawText("TOTAL: Rs ${"%.2f".format(invoice.total)}", 20f, y, paint.apply { isFakeBoldText = true })

                destination.finishPage(page)
                callback.onWriteFinished(arrayOf(android.print.PageRange.ALL_PAGES))
            }
        }

        val jobName = "Receipt_${invoice.invoiceNumber}"
        printManager.print(jobName, printAdapter, null)
    }
}