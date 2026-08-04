package com.aimr.aimrpos.presentation.scanhub

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aimr.aimrpos.data.scanning.OcrOutputFormat
import com.aimr.aimrpos.data.scanning.ScanMode
import com.aimr.aimrpos.data.scanning.ScannedPage

@Composable
fun ScanHubScreen(navController: NavHostController) {
    val viewModel: ScanHubViewModel = viewModel()
    val currentSession by viewModel.currentSession
    val scannedPages by viewModel.scannedPages
    val selectedMode by viewModel.selectedMode
    val isProcessing by viewModel.isProcessing
    val statusMessage by viewModel.statusMessage

    var sessionName by remember { mutableStateOf("") }
    var showModeDialog by remember { mutableStateOf(false) }
    var showOcrDialog by remember { mutableStateOf(false) }
    var showImportDialog by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(statusMessage) {
        if (!statusMessage.isNullOrEmpty()) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Scan Hub",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        statusMessage?.let { msg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = msg,
                color = if (msg.contains("failed", true) || msg.contains("Failed", true)) Color(0xFFEF4444) else Color(0xFF10B981),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentSession == null) {
            CreateSessionCard(
                sessionName = sessionName,
                onNameChange = { sessionName = it },
                onCreate = {
                    viewModel.createSession(it, selectedMode)
                    sessionName = ""
                },
                onModeClick = { showModeDialog = true },
                selectedMode = selectedMode
            )
        } else {
            ActiveSessionCard(
                sessionName = currentSession!!.sessionName,
                mode = selectedMode,
                onClose = { viewModel.clearSession() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (currentSession != null) {
            ActionButtons(
                onScan = { capturedBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888) },
                onImport = { showImportDialog = true },
                onOcr = { showOcrDialog = true },
                isProcessing = isProcessing,
                pageCount = scannedPages.size
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Scanned Pages (${scannedPages.size})",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(scannedPages, key = { it.id }) { page ->
                ScannedPageCard(page = page)
            }
        }
    }

    if (capturedBitmap != null) {
        LaunchedEffect(capturedBitmap) {
            capturedBitmap?.let { viewModel.addImageFromCamera(it) }
            capturedBitmap = null
        }
    }

    if (showModeDialog) {
        ScanModeDialog(
            selectedMode = selectedMode,
            onModeSelected = { mode ->
                viewModel.setMode(mode)
                showModeDialog = false
            },
            onDismiss = { showModeDialog = false }
        )
    }

    if (showOcrDialog) {
        OcrExportDialog(
            pages = scannedPages,
            selectedFormat = viewModel.selectedOcrFormat,
            onFormatChange = {},
            onExportCsv = { viewModel.exportOcr(OcrOutputFormat.CSV) },
            onExportExcel = { viewModel.exportOcr(OcrOutputFormat.EXCEL) },
            onCopyText = { viewModel.copyOcrTextToClipboard() },
            onDismiss = { showOcrDialog = false }
        )
    }

    if (showImportDialog) {
        ImportImageDialog(
            onImport = { uris ->
                viewModel.addImagesFromUris(uris)
                showImportDialog = false
            },
            onDismiss = { showImportDialog = false }
        )
    }
}

@Composable
fun CreateSessionCard(
    sessionName: String,
    onNameChange: (String) -> Unit,
    onCreate: (String) -> Unit,
    onModeClick: () -> Unit,
    selectedMode: ScanMode
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Create New Scan Session", color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = sessionName,
                onValueChange = onNameChange,
                label = { Text("Session Name", color = Color(0xFF94A3B8)) },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedContainerColor = Color(0xFF1E293B)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onModeClick, modifier = Modifier.fillMaxWidth()) {
                Text("Mode: ${selectedMode.label}", color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { onCreate(sessionName) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Create Session")
            }
        }
    }
}

@Composable
fun ActiveSessionCard(sessionName: String, mode: ScanMode, onClose: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(sessionName, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Mode: ${mode.label}", color = Color(0xFF94A3B8), fontSize = 12.sp)
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Delete, contentDescription = "Close session", tint = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
fun ActionButtons(
    onScan: () -> Unit,
    onImport: () -> Unit,
    onOcr: () -> Unit,
    isProcessing: Boolean,
    pageCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onScan,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
            enabled = !isProcessing
        ) {
            Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("Scan")
        }
        Button(
            onClick = onImport,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
            enabled = !isProcessing
        ) {
            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("Import")
        }
        Button(
            onClick = onOcr,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
            enabled = pageCount > 0 && !isProcessing
        ) {
            Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text("OCR")
        }
    }
    if (isProcessing) {
        Spacer(modifier = Modifier.height(6.dp))
        Text("Processing...", color = Color(0xFFF59E0B), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun ScannedPageCard(page: com.aimr.aimrpos.data.scanning.ScannedPage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFF0F172A), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(32.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(page.fileName, color = Color.White, fontWeight = FontWeight.Medium)
                Text("Type: ${page.documentType}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                Text("Mode: ${page.processingMode}", color = Color(0xFF64748B), fontSize = 11.sp)
                Text("Confidence: ${(page.confidence * 100).toInt()}%", color = Color(0xFF94A3B8), fontSize = 12.sp)
                Text("Status: ${page.extractionStatus}", color = Color(0xFF10B981), fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ScanModeDialog(
    selectedMode: ScanMode,
    onModeSelected: (ScanMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Scan Mode", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ScanMode.entries.forEach { mode ->
                    OutlinedButton(
                        onClick = { onModeSelected(mode) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(mode.label, fontWeight = FontWeight.Bold, color = Color.White)
                            Text(
                                text = when (mode) {
                                    ScanMode.SIMPLE -> "Fast OCR, basic text extraction"
                                    ScanMode.ENHANCED -> "Contrast + denoise, better quality"
                                    ScanMode.SUPER_MAGIC -> "Binarization + deep field extraction"
                                },
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        },
        containerColor = Color(0xFF1E293B)
    )
}

@Composable
fun OcrExportDialog(
    pages: List<ScannedPage>,
    selectedFormat: OcrOutputFormat,
    onFormatChange: (OcrOutputFormat) -> Unit,
    onExportCsv: () -> Unit,
    onExportExcel: () -> Unit,
    onCopyText: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("OCR Export Options", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Output Format:", color = Color(0xFF94A3B8), fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OcrOutputFormat.entries.forEach { format ->
                        OutlinedButton(
                            onClick = { onFormatChange(format) },
                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                contentColor = if (format == selectedFormat) Color(0xFF3B82F6) else Color.White
                            )
                        ) {
                            Text(format.label)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onExportCsv, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Export CSV")
                }
                Button(onClick = onExportExcel, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Export Excel")
                }
                Button(onClick = onCopyText, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Copy Text to Clipboard")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Close", color = Color(0xFF94A3B8))
            }
        },
        containerColor = Color(0xFF1E293B)
    )
}

@Composable
fun ImportImageDialog(onImport: (List<android.net.Uri>) -> Unit, onDismiss: () -> Unit) {
    val launcher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) onImport(uris)
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Import Images", color = Color.White) },
        text = { Text("Select images from gallery to import into current session.", color = Color(0xFF94A3B8)) },
        confirmButton = {},
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF94A3B8))
            }
        },
        containerColor = Color(0xFF1E293B)
    )
}