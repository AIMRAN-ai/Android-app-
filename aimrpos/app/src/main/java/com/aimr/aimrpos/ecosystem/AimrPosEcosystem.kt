package com.aimr.aimrpos.ecosystem

import com.aimr.aimrpos.data.local.entity.*
import com.aimr.aimrpos.data.local.dao.*
import com.aimr.aimrpos.data.repository.*
import com.aimr.aimrpos.domain.model.*
import com.aimr.aimrpos.domain.repository.*
import com.aimr.aimrpos.domain.usecase.*
import com.aimr.aimrpos.di.AppModule
import com.aimr.aimrpos.navigation.Screen
import com.aimr.aimrpos.ui.bottombar.BottomNavItem
import com.aimr.aimrpos.ui.bottombar.CinematicBottomBar
import com.aimr.aimrpos.ui.bottombar.CinematicBottomBarWithGradient

/**
 * AIMR POS Complete Ecosystem Register
 *
 * This file registers all components of the AIMR POS ecosystem:
 * - Screens and navigation
 * - Data models and entities
 * - Repositories and use cases
 * - Scanning pipeline (barcode + QR)
 * - Document intelligence (OCR)
 * - Approval workflows
 * - Multi-warehouse support
 * - Audit trails
 * - Document vault
 * - Payment processing
 * - Aging reports
 *
 * Architecture: Clean Architecture + MVVM + Offline-First
 * Backend: Supabase (managed Postgres + Auth + Realtime)
 * Local DB: Room (SQLite) — source of truth
 * Sync: WorkManager delta sync with last-write-wins conflict resolution
 */

data class EcosystemComponent(
    val name: String,
    val category: String,
    val description: String,
    val status: String,
    val screenRoute: String? = null,
    val dependencies: List<String> = emptyList()
)

val AIMR_POSEcosystem = listOf(
    // === AUTHENTICATION ===
    EcosystemComponent(
        name = "PIN Login",
        category = "Authentication",
        description = "4-digit PIN-based local authentication with SHA-256 hashing",
        status = "COMPLETE",
        screenRoute = Screen.Login.route
    ),
    EcosystemComponent(
        name = "Supabase Sync Auth",
        category = "Authentication",
        description = "Anonymous Supabase auth session for cloud sync",
        status = "COMPLETE",
        dependencies = listOf("PIN Login")
    ),

    // === CORE INVENTORY ===
    EcosystemComponent(
        name = "Product Management",
        category = "Inventory",
        description = "CRUD for products with SKU, barcode, category, pricing, stock tracking",
        status = "COMPLETE",
        screenRoute = Screen.InventoryList.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Barcode Scanning",
        category = "Inventory",
        description = "ML Kit on-device barcode scanning for product lookup",
        status = "COMPLETE",
        screenRoute = Screen.ProductForm.route,
        dependencies = listOf("Product Management")
    ),
    EcosystemComponent(
        name = "Category Management",
        category = "Inventory",
        description = "Product categorization with filtering and search",
        status = "COMPLETE",
        dependencies = listOf("Product Management")
    ),
    EcosystemComponent(
        name = "Low Stock Alerts",
        category = "Inventory",
        description = "Automatic alerts when stock falls below threshold",
        status = "COMPLETE",
        dependencies = listOf("Product Management")
    ),

    // === INVOICING ===
    EcosystemComponent(
        name = "Invoice Creation",
        category = "Invoicing",
        description = "Create invoices with line items, tax calculation, discounts",
        status = "COMPLETE",
        screenRoute = Screen.NewInvoice.route,
        dependencies = listOf("PIN Login", "Product Management", "Customer Management")
    ),
    EcosystemComponent(
        name = "Invoice Preview & Export",
        category = "Invoicing",
        description = "PDF export, WhatsApp share, print support",
        status = "COMPLETE",
        screenRoute = Screen.InvoicePreview.route,
        dependencies = listOf("Invoice Creation")
    ),
    EcosystemComponent(
        name = "QR Invoice Scanning",
        category = "Invoicing",
        description = "Scan QR codes to auto-fill invoice data from customer/supplier",
        status = "COMPLETE",
        screenRoute = Screen.QrScanner.route,
        dependencies = listOf("Invoice Creation")
    ),

    // === CUSTOMER/SUPPLIER ===
    EcosystemComponent(
        name = "Customer Ledger",
        category = "Customers",
        description = "Credit/udhaar tracking, payment history, outstanding balance",
        status = "COMPLETE",
        screenRoute = Screen.CustomerLedger.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Supplier Management",
        category = "Suppliers",
        description = "Supplier records with CNIC, tax number, bank details, credit terms",
        status = "COMPLETE",
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Payment Processing",
        category = "Finance",
        description = "Payment tracking linked to invoices, customers, and suppliers",
        status = "COMPLETE",
        dependencies = listOf("Customer Ledger", "Supplier Management")
    ),

    // === PURCHASE & STOCK ===
    EcosystemComponent(
        name = "Purchase Orders",
        category = "Purchasing",
        description = "PO creation with approval workflow, supplier linkage",
        status = "COMPLETE",
        screenRoute = Screen.PurchaseOrder.route,
        dependencies = listOf("PIN Login", "Supplier Management")
    ),
    EcosystemComponent(
        name = "Goods Received Notes",
        category = "Purchasing",
        description = "GRN creation linked to POs, stock-in confirmation",
        status = "COMPLETE",
        screenRoute = Screen.GRN.route,
        dependencies = listOf("Purchase Orders")
    ),
    EcosystemComponent(
        name = "Returns & Refunds",
        category = "Purchasing",
        description = "Return invoice creation with approval workflow",
        status = "COMPLETE",
        screenRoute = Screen.ReturnInvoice.route,
        dependencies = listOf("Invoice Creation")
    ),
    EcosystemComponent(
        name = "Stock Transfer",
        category = "Warehousing",
        description = "Inter-warehouse stock transfers with approval workflow",
        status = "COMPLETE",
        screenRoute = Screen.StockTransfer.route,
        dependencies = listOf("PIN Login", "Warehouse Management")
    ),

    // === WAREHOUSE ===
    EcosystemComponent(
        name = "Multi-Warehouse",
        category = "Warehousing",
        description = "Multi-location support with stock tracking per warehouse",
        status = "COMPLETE",
        screenRoute = Screen.Warehouse.route,
        dependencies = listOf("PIN Login")
    ),

    // === DOCUMENT INTELLIGENCE ===
    EcosystemComponent(
        name = "Document Scanner",
        category = "Document Intelligence",
        description = "CameraX + ML Kit OCR pipeline for invoice/receipt/CNIC/cheque scanning",
        status = "COMPLETE",
        screenRoute = Screen.DocumentScanner.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "OCR Field Extraction",
        category = "Document Intelligence",
        description = "Structured field extraction via regex/NER for amounts, dates, invoice numbers",
        status = "COMPLETE",
        dependencies = listOf("Document Scanner")
    ),
    EcosystemComponent(
        name = "Document Review (Human-in-loop)",
        category = "Document Intelligence",
        description = "Review screen showing original scan beside extracted fields for correction",
        status = "COMPLETE",
        screenRoute = Screen.DocumentReview.route,
        dependencies = listOf("OCR Field Extraction")
    ),
    EcosystemComponent(
        name = "Document Vault",
        category = "Document Intelligence",
        description = "Searchable document repository with confidence scores and entity linking",
        status = "COMPLETE",
        screenRoute = Screen.DocumentVault.route,
        dependencies = listOf("Document Scanner")
    ),

    // === APPROVAL WORKFLOWS ===
    EcosystemComponent(
        name = "Approval Queue",
        category = "Workflows",
        description = "Role-based approval chains for POs, returns, and stock transfers",
        status = "COMPLETE",
        screenRoute = Screen.ApprovalQueue.route,
        dependencies = listOf("PIN Login")
    ),

    // === REPORTING ===
    EcosystemComponent(
        name = "Dashboard",
        category = "Reporting",
        description = "Today's sales, low stock alerts, quick actions with KPI cards",
        status = "COMPLETE",
        screenRoute = Screen.Dashboard.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Aging Report",
        category = "Reporting",
        description = "Customer aging breakdown (0-30, 31-60, 61-90, 90+ days)",
        status = "COMPLETE",
        screenRoute = Screen.AgingReport.route,
        dependencies = listOf("Customer Ledger")
    ),
    EcosystemComponent(
        name = "Audit Log",
        category = "Reporting",
        description = "Full audit trail — who changed what, when, from which device",
        status = "COMPLETE",
        screenRoute = Screen.AuditLog.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Reports (Sales/Profit/Stock)",
        category = "Reporting",
        description = "Daily/weekly/monthly sales, profit margin, stock valuation with Vico charts",
        status = "COMPLETE",
        screenRoute = Screen.Reports.route,
        dependencies = listOf("PIN Login")
    ),

    // === SETTINGS ===
    EcosystemComponent(
        name = "Settings",
        category = "Configuration",
        description = "Language toggle, business info, tax config, user management, sync status",
        status = "COMPLETE",
        screenRoute = Screen.Settings.route,
        dependencies = listOf("PIN Login")
    ),

    // === SYNC ===
    EcosystemComponent(
        name = "Delta Sync Engine",
        category = "Sync",
        description = "WorkManager-based delta sync with last-write-wins conflict resolution",
        status = "COMPLETE",
        dependencies = listOf("PIN Login", "Supabase Sync Auth")
    ),
    EcosystemComponent(
        name = "Sync Worker",
        category = "Sync",
        description = "Background sync with network/battery/storage constraints",
        status = "COMPLETE",
        dependencies = listOf("Delta Sync Engine")
    ),

    // === UI ===
    EcosystemComponent(
        name = "Cinematic Bottom Bar",
        category = "UI",
        description = "Gradient bottom navigation with animated icons, badge support, custom styling",
        status = "COMPLETE",
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "QR Scanner Screen",
        category = "UI",
        description = "QR code scanning with ML Kit and payload parsing",
        status = "COMPLETE",
        screenRoute = Screen.QrScanner.route,
        dependencies = listOf("Cinematic Bottom Bar")
    ),
    EcosystemComponent(
        name = "Scan Hub",
        category = "Document Intelligence",
        description = "Camera capture with auto-crop preview and document type selector",
        status = "COMPLETE",
        screenRoute = Screen.ScanHub.route,
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Supplier Ledger",
        category = "Finance",
        description = "Payment terms, outstanding balance tracking, and aging report",
        status = "COMPLETE",
        screenRoute = Screen.SupplierLedger.route,
        dependencies = listOf("PIN Login", "Supplier Management")
    ),
    EcosystemComponent(
        name = "Material 3 Theme",
        category = "UI",
        description = "High-contrast Material 3 theme with RTL support, Urdu font, bilingual",
        status = "COMPLETE",
        dependencies = listOf("PIN Login")
    ),
    EcosystemComponent(
        name = "Bilingual Support",
        category = "UI",
        description = "English + Urdu (RTL) with Noto Nastaliq font and strings.xml + strings-ur.xml",
        status = "COMPLETE",
        dependencies = listOf("Material 3 Theme")
    ),

    // === MULTI-LOCATION ===
    EcosystemComponent(
        name = "Multi-Location Support",
        category = "Warehousing",
        description = "Locations with business_id, is_warehouse flag, address, phone, manager",
        status = "COMPLETE",
        screenRoute = Screen.Warehouse.route,
        dependencies = listOf("PIN Login")
    ),

    // === DOCUMENT INTELLIGENCE (ENHANCED) ===
    EcosystemComponent(
        name = "Scanned Documents",
        category = "Document Intelligence",
        description = "Document capture with extraction_status (PENDING|REVIEWED|CONFIRMED), linked records, location tracking",
        status = "COMPLETE",
        screenRoute = Screen.ScanHub.route,
        dependencies = listOf("PIN Login", "Multi-Location Support")
    ),
    EcosystemComponent(
        name = "OCR Extraction Pipeline",
        category = "Document Intelligence",
        description = "Auto-crop, OCR raw text extraction with confidence scoring",
        status = "COMPLETE",
        dependencies = listOf("Scanned Documents")
    ),
    EcosystemComponent(
        name = "Document Linking",
        category = "Document Intelligence",
        description = "Link scanned documents to POs, GRNs, invoices, returns via linked_record_type and linked_record_id",
        status = "COMPLETE",
        dependencies = listOf("Scanned Documents")
    ),

    // === PURCHASE WORKFLOW ===
    EcosystemComponent(
        name = "Purchase Order Workflow",
        category = "Purchasing",
        description = "PO creation with DRAFT|PENDING_APPROVAL|APPROVED|RECEIVED|CANCELLED statuses, location linkage",
        status = "COMPLETE",
        screenRoute = Screen.PurchaseOrder.route,
        dependencies = listOf("PIN Login", "Supplier Management", "Multi-Location Support")
    ),
    EcosystemComponent(
        name = "PO Line Items",
        category = "Purchasing",
        description = "Per-line item tracking with quantity_ordered, quantity_received, unit_cost",
        status = "COMPLETE",
        dependencies = listOf("Purchase Order Workflow")
    ),
    EcosystemComponent(
        name = "Goods Received Note",
        category = "Purchasing",
        description = "GRN with discrepancy_notes, source_document_id, location tracking",
        status = "COMPLETE",
        screenRoute = Screen.GRN.route,
        dependencies = listOf("Purchase Order Workflow", "PO Line Items")
    ),
    EcosystemComponent(
        name = "PO-to-GRN Conversion",
        category = "Purchasing",
        description = "Convert approved POs to GRNs on delivery with quantity tracking",
        status = "COMPLETE",
        dependencies = listOf("Goods Received Note")
    ),

    // === STOCK MOVEMENT ===
    EcosystemComponent(
        name = "Multi-Location Stock Ledger",
        category = "Warehousing",
        description = "Stock IN/OUT/TRANSFER/ADJUSTMENT movements with reference tracking per location",
        status = "COMPLETE",
        dependencies = listOf("Multi-Location Support")
    ),
    EcosystemComponent(
        name = "Stock Transfer Workflow",
        category = "Warehousing",
        description = "Inter-location transfers with from/to location tracking and approval",
        status = "COMPLETE",
        screenRoute = Screen.StockTransfer.route,
        dependencies = listOf("Multi-Location Support", "Multi-Location Stock Ledger")
    ),

    // === APPROVAL CHAINS ===
    EcosystemComponent(
        name = "Approval Chain Management",
        category = "Workflows",
        description = "Configurable approval chains with approval_chain_json, current approver tracking",
        status = "COMPLETE",
        screenRoute = Screen.ApprovalQueue.route,
        dependencies = listOf("PIN Login")
    ),

    // === AUDIT TRAIL ===
    EcosystemComponent(
        name = "Enterprise Audit Trail",
        category = "Reporting",
        description = "Full audit log with business_id, old/new value JSON, device_id, IP tracking, searchable",
        status = "COMPLETE",
        screenRoute = Screen.AuditLog.route,
        dependencies = listOf("PIN Login")
    ),

    // === SUPPLIER LEDGER ===
    EcosystemComponent(
        name = "Supplier Ledger Enhanced",
        category = "Finance",
        description = "Payment terms, outstanding balance, current_balance, tax_number, aging report",
        status = "COMPLETE",
        screenRoute = Screen.SupplierLedger.route,
        dependencies = listOf("PIN Login", "Supplier Management")
    )
)

/**
 * Get all components by category
 */
fun getComponentsByCategory(category: String): List<EcosystemComponent> {
    return AIMR_POSEcosystem.filter { it.category == category }
}

/**
 * Get all complete components
 */
fun getCompleteComponents(): List<EcosystemComponent> {
    return AIMR_POSEcosystem.filter { it.status == "COMPLETE" }
}

/**
 * Get component by route
 */
fun getComponentByRoute(route: String?): EcosystemComponent? {
    return AIMR_POSEcosystem.find { it.screenRoute == route }
}

/**
 * Get total component count
 */
fun getTotalComponentCount(): Int {
    return AIMR_POSEcosystem.size
}

/**
 * Get completion percentage
 */
fun getCompletionPercentage(): Float {
    val complete = AIMR_POSEcosystem.count { it.status == "COMPLETE" }
    return if (AIMR_POSEcosystem.isNotEmpty()) complete.toFloat() / AIMR_POSEcosystem.size else 0f
}