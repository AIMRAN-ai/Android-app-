# AIMR POS — Complete Architecture & Features Document

> **Version:** 3.0 | **Last Updated:** 2026-08-04 | **Branch:** `session/agent_e66dfb91-8910-4b0c-9b31-efb73db492db`

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Architecture](#2-architecture)
3. [Feature Matrix](#3-feature-matrix)
4. [Database Schema (v3)](#4-database-schema-v3)
5. [Clean Architecture Layers](#5-clean-architecture-layers)
6. [Navigation & Screens](#6-navigation--screens)
7. [UI/UX Design System](#7-uiux-design-system)
8. [Business Logic](#8-business-logic)
9. [Barcode & QR Scanning](#9-barcode--qr-scanning)
10. [Receipt Generation](#10-receipt-generation)
11. [Payment Methods](#11-payment-methods)
12. [Extension / Plugin System](#12-extension--plugin-system)
13. [Data Flow Diagrams](#13-data-flow-diagrams)
14. [Build & Deployment](#14-build--deployment)

---

## 1. Project Overview

**AIMR POS** is an enterprise-grade Android Point-of-Sale application built with modern Android architecture components. It provides a complete business management solution including inventory, invoicing, customer/supplier management, analytics, document scanning, and multi-location support.

### Key Characteristics

| Attribute | Value |
|-----------|-------|
| Compile SDK | 34 |
| Min SDK | 26 |
| Target SDK | 34 |
| Language | Kotlin |
| UI Framework | Jetpack Compose (Material 3) |
| Database | Room (SQLite) v3 |
| DI | Hilt (Dagger 2.50) |
| Architecture | MVVM + Clean Architecture |
| Sync | Supabase (offline-first) |
| Charts | Vico (compose-m3 2.1.0) |
| Barcode | ML Kit Barcode Scanning 17.2.0 |
| OCR | ML Kit Text Recognition 16.0.0 |
| Camera | CameraX 1.3.0 |
| Coroutines | kotlinx-coroutines-android 1.7.3 |
| Navigation | Jetpack Navigation Compose 2.7.6 |
| Bilingual | English + Urdu (RTL) |

---

## 2. Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                      │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────────────┐ │
│  │  Screens  │ │ ViewModels│ │  Composables│ │  Bottom Nav Bar │ │
│  └──────────┘ └──────────┘ └──────────┘ └───────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│                      DOMAIN LAYER                              │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────────────┐ │
│  │  Use Cases│ │ Models   │ │ Repos    │ │  Extension Registry│ │
│  └──────────┘ └──────────┘ └──────────┘ └───────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│                      DATA LAYER                                │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────────────┐ │
│  │  DAOs     │ │ Entities │ │ Database │ │  Repo Implementations│ │
│  └──────────┘ └──────────┘ └──────────┘ └───────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│                      DI LAYER (Hilt)                           │
│  ┌───────────────────────────────────────────────────────────┐ │
│  │  AppModule → Provides all DAOs, Repositories, Database   │ │
│  └───────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────────┤
│                      INFRASTRUCTURE                            │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────────────┐ │
│  │  Room DB  │ │ Supabase │ │ ML Kit   │ │  CameraX          │ │
│  └──────────┘ └──────────┘ └──────────┘ └───────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Design Principles

1. **Single Responsibility** — Each class has one clear purpose
2. **Dependency Inversion** — Domain layer defines interfaces; data layer implements them
3. **Offline-First** — All data is local-first; sync to Supabase in background
4. **Reactive UI** — All screens observe `Flow<T>` from ViewModels
5. **Type Safety** — Sealed classes for screens, enums for product types/payment methods
6. **Bilingual** — All strings defined in `strings.xml` with Urdu equivalents

---

## 3. Feature Matrix

### Core Features

| Feature | Status | Description |
|---------|--------|-------------|
| User Authentication | ✅ | PIN-based login |
| Dashboard | ✅ | Live metrics from database |
| Inventory Management | ✅ | CRUD, stock tracking, low-stock alerts |
| Invoice Creation | ✅ | Multi-item, tax calc, stock deduction |
| Invoice Preview & Receipts | ✅ | PDF, Print, WhatsApp |
| Customer Ledger | ✅ | Real credit balance calculation |
| Supplier Ledger | ✅ | Payment terms, outstanding balances |
| Reports & Analytics | ✅ | Vico charts, live data |
| Document Scanner | ✅ | OCR for invoices, receipts, CNIC |
| Scan Hub | ✅ | Multi-document type capture |
| Purchase Orders | ✅ | PO creation, approval workflow |
| Goods Received Notes | ✅ | GRN with item-level tracking |
| Returns & Refunds | ✅ | Return invoice workflow |
| Approval Queue | ✅ | Approve/reject pending requests |
| Audit Log | ✅ | Full action tracking |
| Document Vault | ✅ | Scanned document storage |
| Multi-Location | ✅ | Locations with warehouse support |
| Stock Transfers | ✅ | Inter-location transfers |
| Aging Report | ✅ | Customer/supplier aging analysis |
| Settings | ✅ | Language, sync, receipt config |

### Product Types (8 Types)

| Type | Code | Use Case |
|------|------|----------|
| Physical Goods | `PHYSICAL` | Standard retail products |
| Perishable | `PERISHABLE` | Food items with expiry tracking |
| Digital Product | `DIGITAL` | Software, e-books, licenses |
| Service | `SERVICE` | Labor, consultations |
| Wholesale | `WHOLESALE` | Bulk pricing products |
| Retail | `RETAIL` | Individual retail items |
| Raw Material | `RAW_MATERIAL` | Manufacturing inputs |
| Finished Goods | `FINISHED_GOODS` | Completed manufactured products |

### Payment Methods (8 Methods)

| Method | Code | Description |
|--------|------|-------------|
| Cash | `CASH` | Physical cash payment |
| Bank Transfer | `BANK` | Bank wire/transfer |
| UPI | `UPI` | Unified Payments Interface |
| Credit Card | `CREDIT_CARD` | Credit card payment |
| Debit Card | `DEBIT_CARD` | Debit card payment |
| Wallet | `WALLET` | Digital wallet (JazzCash, Easypaisa) |
| Check | `CHECK` | Cheque payment |
| Credit (Udhaar) | `CREDIT` | Credit/deferred payment |

### Data Source Extensions (9 Extensions)

| Extension | ID | Description |
|-----------|----|-------------|
| CSV Import | `csv_import` | Import products from CSV file |
| Excel Import | `excel_import` | Import products from Excel file |
| API Sync | `api_sync` | Sync products from remote API |
| Barcode Scanner | `barcode_scanner` | Scan barcodes to add products |
| QR Scanner | `qr_scanner` | Scan QR codes to add products |
| Manual Entry | `manual_entry` | Manually add products |
| SMS Sync | `sms_sync` | Sync orders from SMS |
| Web Portal | `web_portal` | Sync from web dashboard |
| Supabase Sync | (built-in) | Cloud sync with offline support |

---

## 4. Database Schema (v3)

### Entity Count: 19 entities, 20 tables

```
┌─────────────────────────────────────────────────────────────────┐
│                         DATABASE v3                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────┐  │
│  │  products    │  │  locations   │  │  categories        │  │
│  │  users       │  │  customers   │  │  suppliers         │  │
│  │  invoices    │  │  invoice_items│ │  payments          │  │
│  │  purchase_orders│ │ po_items  │  │  grns              │  │
│  │  stock_transfers│ │ stock_ledger│ │  return_invoices   │  │
│  │  approval_requests│ │ audit_log │  │  scanned_documents │  │
│  │  business_config│ │          │  │                    │  │
│  └──────────────┘  └──────────────┘  └────────────────────┘  │
│                                                                 │
│  Schema Version: 3                                             │
│  Total Entities: 19                                            │
│  Total Tables: 20                                              │
└─────────────────────────────────────────────────────────────────┘
```

### Key Schema Changes (v2 → v3)

| Change | Before | After |
|--------|--------|-------|
| Warehouse Entity | `warehouses` table | `locations` table with `businessId` + `isWarehouse` |
| Product Types | Single type | 8 types via `productType` field |
| Barcode Support | `barcode` field only | `barcode` + `qrCode` fields |
| PO Line Items | Not tracked | `purchase_order_items` table |
| Stock Ledger | Not tracked | `stock_ledger` table |
| Supplier Credit | `creditBalance` | `currentBalance` (distinguished from customer credit) |
| PO Creator | Not tracked | `createdByUserId` field |
| GRN Received Date | Not tracked | `receivedAt` field |
| Stock Transfer Request | `transferredByUserId` | `requestedByUserId` |
| Document Vault | `document_vault` | `scanned_documents` |
| Return Invoices | `return_invoices` | `returns` |
| Audit Logs | `audit_logs` | `audit_log` |

### Entity Relationships

```
locations ──┬── products (locationId)
            │
            ├── purchase_orders (locationId)
            │       └── purchase_order_items
            │
            ├── grns (locationId)
            │
            ├── stock_transfers (fromLocationId, toLocationId)
            │
            └── stock_ledger (locationId)

customers ──┬── invoices
            │       └── invoice_items
            │       └── payments
            │
            └── return_invoices

suppliers ──┬── purchase_orders
            │       └── grns
            │
            └── return_invoices

users ───────┬── invoices (createdByUserId)
             ├── purchase_orders (createdByUserId)
             ├── approval_requests (requestedByUserId)
             └── audit_log

categories ──┬── products
             └── business_config
```

---

## 5. Clean Architecture Layers

### Presentation Layer

```
presentation/
├── dashboard/
│   ├── DashboardScreen.kt          # Live metrics dashboard
│   └── DashboardViewModel.kt       # Real data aggregation
├── inventory/
│   ├── InventoryListScreen.kt      # Product list with filters
│   ├── InventoryListViewModel.kt
│   ├── ProductFormScreen.kt        # Add/edit product
│   └── ProductFormViewModel.kt
├── invoice/
│   ├── NewInvoiceScreen.kt         # Invoice creation with stock deduction
│   ├── NewInvoiceViewModel.kt      # Business logic for invoicing
│   ├── InvoicePreviewScreen.kt     # Receipt preview with PDF/print/WhatsApp
│   └── InvoicePreviewViewModel.kt
├── customer/
│   ├── CustomerLedgerScreen.kt     # Real credit balance calculation
│   └── CustomerLedgerViewModel.kt
├── reports/
│   ├── ReportsScreen.kt            # Vico charts with live data
│   └── ReportsViewModel.kt
├── settings/
│   ├── SettingsScreen.kt           # Extension management
│   └── SettingsViewModel.kt
├── scanhub/
│   ├── ScanHubScreen.kt            # Multi-document capture hub
│   └── ScanHubViewModel.kt
├── supplierledger/
│   ├── SupplierLedgerScreen.kt     # Supplier payment tracking
│   └── SupplierLedgerViewModel.kt
├── scan/
│   ├── DocumentScannerScreen.kt    # Camera-based OCR scanning
│   └── DocumentScannerViewModel.kt
├── document/
│   ├── DocumentReviewScreen.kt     # OCR result review
│   └── DocumentReviewViewModel.kt
├── purchase/
│   ├── PurchaseOrderScreen.kt      # PO creation workflow
│   └── PurchaseOrderViewModel.kt
├── grn/
│   ├── GRNScreen.kt                # Goods Received Note
│   └── GRNViewModel.kt
├── returninvoice/
│   ├── ReturnInvoiceScreen.kt      # Return/refund workflow
│   └── ReturnInvoiceViewModel.kt
├── approval/
│   ├── ApprovalQueueScreen.kt      # Approval workflow
│   └── ApprovalQueueViewModel.kt
├── audit/
│   ├── AuditLogScreen.kt           # Audit trail
│   └── AuditLogViewModel.kt
├── vault/
│   ├── DocumentVaultScreen.kt      # Document storage
│   └── DocumentVaultViewModel.kt
├── warehouse/
│   ├── WarehouseScreen.kt          # Location management
│   └── WarehouseViewModel.kt
├── transfer/
│   ├── StockTransferScreen.kt      # Inter-location transfers
│   └── StockTransferViewModel.kt
├── aging/
│   ├── AgingReportScreen.kt        # Aging analysis
│   └── AgingReportViewModel.kt
├── qr/
│   ├── QrScannerScreen.kt          # QR code scanner
│   └── QrScannerViewModel.kt
├── login/
│   ├── LoginScreen.kt              # PIN authentication
│   └── LoginViewModel.kt
└── bottombar/
    └── CinematicBottomBar.kt       # Animated bottom navigation
```

### Domain Layer

```
domain/
├── model/
│   ├── Models.kt                   # Core domain models (Product, Invoice, etc.)
│   └── EnterpriseModels.kt         # Enterprise domain models
├── repository/
│   └── RepositoryInterfaces.kt     # All repository interfaces
├── usecase/
│   └── UseCases.kt                 # All use cases (20+ use cases)
└── model/
    └── (data classes for use case results)
```

### Data Layer

```
data/
├── local/
│   ├── entity/
│   │   ├── ProductEntity.kt        # 8 product types, barcode/QR
│   │   ├── InvoiceEntity.kt        # Multi-payment method support
│   │   ├── InvoiceItemEntity.kt
│   │   ├── CustomerEntity.kt
│   │   ├── SupplierEntity.kt
│   │   ├── LocationEntity.kt       # Replaces WarehouseEntity
│   │   ├── CategoryEntity.kt
│   │   ├── UserEntity.kt
│   │   ├── PaymentEntity.kt
│   │   ├── PurchaseOrderEntity.kt
│   │   ├── PurchaseOrderItemEntity.kt
│   │   ├── GRNEntity.kt
│   │   ├── StockTransferEntity.kt
│   │   ├── StockLedgerEntity.kt
│   │   ├── ReturnInvoiceEntity.kt
│   │   ├── ApprovalRequestEntity.kt
│   │   ├── AuditLogEntity.kt
│   │   ├── DocumentVaultEntity.kt
│   │   ├── BusinessEntity.kt
│   │   └── WarehouseEntity.kt      # Legacy
│   ├── dao/
│   │   ├── ProductDao.kt           # + barcode/QR queries + stock ops
│   │   ├── InvoiceDao.kt
│   │   ├── InvoiceItemDao.kt
│   │   ├── CustomerDao.kt
│   │   ├── SupplierDao.kt
│   │   ├── LocationDao.kt
│   │   ├── CategoryDao.kt
│   │   ├── UserDao.kt
│   │   ├── PaymentDao.kt
│   │   ├── PurchaseOrderDao.kt
│   │   ├── PurchaseOrderItemDao.kt
│   │   ├── GRNDao.kt
│   │   ├── StockTransferDao.kt
│   │   ├── StockLedgerDao.kt
│   │   ├── ReturnInvoiceDao.kt
│   │   ├── ApprovalRequestDao.kt
│   │   ├── AuditLogDao.kt
│   │   ├── DocumentVaultDao.kt
│   │   ├── BusinessDao.kt
│   │   └── WarehouseDao.kt
│   └── AimrPosDatabase.kt          # v3, 19 entities
├── repository/
│   └── RepositoryImpl.kt           # All repository implementations
├── scanning/
│   ├── DocumentScanner.kt          # OCR document scanning
│   └── ImageCaptureHelper.kt       # Camera image capture
├── qr/
│   └── QrScanner.kt                # QR code scanning
├── sync/
│   └── SyncWorker.kt               # Background sync with Supabase
└── utils/
    ├── ReceiptGenerator.kt         # PDF, Print, WhatsApp
    ├── PaymentMethods.kt           # Payment method constants
    └── ExtensionManager.kt         # Extension/plugin management
```

---

## 6. Navigation & Screens

### Screen Routes

```kotlin
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object InventoryList : Screen("inventory")
    object ProductForm : Screen("inventory_add_edit")
    object NewInvoice : Screen("invoice_new")
    object InvoicePreview : Screen("invoice_preview/{invoiceId}")
    object CustomerLedger : Screen("customers")
    object Reports : Screen("reports")
    object Settings : Screen("settings")
    object DocumentScanner : Screen("document_scanner")
    object DocumentReview : Screen("document_review/{docId}")
    object PurchaseOrder : Screen("purchase_order")
    object GRN : Screen("grn")
    object ReturnInvoice : Screen("return_invoice")
    object ApprovalQueue : Screen("approval_queue")
    object AuditLog : Screen("audit_log")
    object DocumentVault : Screen("document_vault")
    object Warehouse : Screen("warehouse")
    object StockTransfer : Screen("stock_transfer")
    object AgingReport : Screen("aging_report")
    object QrScanner : Screen("qr_scanner")
    object ScanHub : Screen("scan_hub")
    object SupplierLedger : Screen("supplier_ledger")
}
```

### Navigation Flow

```
Login ──→ Dashboard ──┬── Inventory ──→ ProductForm
                      ├── Invoice New ──→ InvoicePreview
                      ├── Customers ──→ CustomerLedger
                      ├── Reports ──→ ReportsScreen
                      ├── Settings ──→ SettingsScreen
                      ├── Scan Hub ──→ DocumentScanner
                      ├── QR Scanner ──→ QrScannerScreen
                      ├── Purchase Order ──→ GRN ──→ ReturnInvoice
                      ├── Approval Queue ──→ Audit Log
                      ├── Document Vault ──→ Warehouse ──→ Stock Transfer
                      └── Aging Report ──→ Supplier Ledger
```

---

## 7. UI/UX Design System

### Color Scheme (Dark Theme)

| Token | Value | Usage |
|-------|-------|-------|
| `BackgroundDark` | `#FF0F172A` | Root background |
| `SurfaceDark` | `#FF1E293B` | Cards, surfaces |
| `PrimaryBlue` | `#FF3B82F6` | Primary buttons, accents |
| `SecondaryAmber` | `#FFF59E0B` | Warnings, highlights |
| `SuccessGreen` | `#FF10B981` | Success states, totals |
| `ErrorRed` | `#FFEF4444` | Errors, low stock |
| `TextPrimary` | `#FFF1F5F9` | Primary text |
| `TextSecondary` | `#FF94A3B8` | Secondary text |
| `TextMuted` | `#FF64748B` | Muted/placeholder text |
| `BorderColor` | `#FF334155` | Borders |
| `CardDark` | `#FF1E293B` | Card backgrounds |

### Typography

| Style | Size | Weight | Line Height |
|-------|------|--------|-------------|
| `displayLarge` | 28sp | Bold | 36sp |
| `headlineMedium` | 22sp | Bold | 28sp |
| `titleLarge` | 18sp | SemiBold | 24sp |
| `bodyLarge` | 16sp | Normal | 24sp |
| `bodyMedium` | 14sp | Normal | 20sp |
| `labelLarge` | 14sp | Medium | 20sp |
| `labelMedium` | 12sp | Medium | 16sp |

### UI Components Library

| Component | File | Description |
|-----------|------|-------------|
| `CinematicBottomBar` | `ui/bottombar/` | Animated bottom navigation |
| `StockBadge` | `ui/components/` | Stock status indicator |
| `PrimaryButton` | `ui/components/` | Primary action button |
| `OutlinedButton` | `ui/components/` | Secondary action button |
| `TopBar` | `ui/components/` | Top app bar |
| `EmptyState` | `ui/components/` | Empty state placeholder |
| `LoadingSpinner` | `ui/components/` | Loading indicator |
| `EnhancedDisplays` | `ui/display/` | Enhanced display components |

---

## 8. Business Logic

### Invoice Creation Flow

```
User opens NewInvoiceScreen
    │
    ├── Enter customer name & phone
    ├── Select product from dropdown (shows stock, price, type)
    ├── Enter quantity (validated against stock)
    ├── Add line item to invoice
    │       │
    │       └── Stock validation: qty <= product.stockQty
    │
    ├── Select payment method (CASH/BANK/UPI/CREDIT_CARD/DEBIT_CARD/WALLET/CHECK/CREDIT)
    ├── Enter discount (optional)
    │
    ├── Calculate totals:
    │   subtotal = sum(lineTotal)
    │   taxAmount = subtotal * 17%
    │   total = subtotal + taxAmount - discount
    │
    ├── Save invoice → generates INV-yyyyMMdd-001
    │       │
    │       ├── Deduct stock: product.stockQty -= invoiceItem.quantity
    │       ├── If paymentMethod == "CREDIT": set paymentStatus = "CREDIT"
    │       └── Navigate to InvoicePreviewScreen
    │
    └── InvoicePreviewScreen:
        ├── Generate PDF receipt (ReceiptGenerator)
        ├── Print receipt (Android PrintManager)
        ├── Share via WhatsApp
        └── Navigate back
```

### Credit Balance Calculation

```
Credit Balance = Total Invoiced - Total Paid

Where:
  Total Invoiced = sum(invoices where customerId = X)
  Total Paid = sum(invoices where customerId = X AND paymentStatus = "PAID")

Displayed in CustomerLedgerScreen with color coding:
  - Red: Outstanding balance (credit > 0)
  - Green: Account clear (credit <= 0)
```

### Stock Deduction

```
On invoice save:
  For each invoiceItem:
    UPDATE products SET stockQty = stockQty - item.quantity
    WHERE id = item.productId AND stockQty >= item.quantity
  
  If rowsAffected == 0:
    Show "Insufficient stock" error
```

### Low Stock Detection

```
Low Stock = products where stockQty <= lowStockThreshold AND isDeleted = 0

DashboardCard shows count of low stock items in red.
InventoryListScreen filter chip for "Low Stock" view.
```

### Daily Sales Aggregation

```
Today's Sales = sum(invoices.total) where updatedAt >= startOfDay
Total Orders = count(invoices) where updatedAt >= startOfDay
Avg Order Value = Today's Sales / Total Orders
```

### Stock Valuation

```
Stock Valuation = sum(product.stockQty * product.costPrice) for all active products
```

---

## 9. Barcode & QR Scanning

### ML Kit Integration

```
build.gradle.kts:
  implementation("com.google.mlkit:barcode-scanning:17.2.0")
  implementation("com.google.mlkit:text-recognition:16.0.0")
  implementation("androidx.camera:camera-core:1.3.0")
  implementation("androidx.camera:camera-camera2:1.3.0")
  implementation("androidx.camera:camera-lifecycle:1.3.0")
  implementation("androidx.camera:camera-view:1.3.0")
  implementation("androidx.camera:camera-extensions:1.3.0")
```

### Scan Hub Workflow

```
ScanHubScreen
    │
    ├── Select Scan Method: Camera / Barcode / QR Code
    ├── Select Document Type: Invoice / Receipt / CNIC / Cheque / Delivery Note / Other
    │
    ├── Camera Preview (CameraX)
    │       │
    │       ├── Capture image
    │       ├── Auto-crop (edge detection)
    │       └── OCR processing (ML Kit Text Recognition)
    │
    ├── Document Review Screen
    │       ├── Extracted text fields
    │       ├── Confidence score
    │       └── Confirm / Edit
    │
    └── Save to Document Vault
```

### Barcode Product Lookup

```kotlin
// ProductEntity has barcode and qrCode fields
@Query("SELECT * FROM products WHERE barcode = :barcode AND isDeleted = 0 LIMIT 1")
suspend fun getByBarcode(barcode: String): ProductEntity?

@Query("SELECT * FROM products WHERE qrCode = :qrCode AND isDeleted = 0 LIMIT 1")
suspend fun getByQrCode(qrCode: String): ProductEntity?
```

---

## 10. Receipt Generation

### ReceiptGenerator Class

```kotlin
class ReceiptGenerator(private val context: Context) {
    fun generatePdfReceipt(invoice: Invoice, items: List<InvoiceItem>): File
    fun shareViaWhatsApp(invoice: Invoice, items: List<InvoiceItem>)
    fun printReceipt(invoice: Invoice, items: List<InvoiceItem>)
}
```

### PDF Receipt Features

- Business name header
- Invoice number and date
- Customer name and phone
- Itemized list with quantity, unit price, line total
- Subtotal, tax (17%), discount, total
- Payment method and status
- Saved to `Documents/receipt_{invoiceNumber}.pdf`

### Print Support

- Uses Android `PrintManager` API
- Generates PDF document for printing
- Configures print attributes (NA_LETTER, 300 DPI)

### WhatsApp Sharing

- Formats receipt as formatted text message
- Opens WhatsApp share intent
- Includes all invoice details in message body

---

## 11. Payment Methods

### PaymentMethod Utility

```kotlin
object PaymentMethods {
    const val CASH = "CASH"
    const val BANK = "BANK"
    const val UPI = "UPI"
    const val CREDIT_CARD = "CREDIT_CARD"
    const val DEBIT_CARD = "DEBIT_CARD"
    const val WALLET = "WALLET"
    const val CHECK = "CHECK"
    const val CREDIT = "CREDIT"
    
    val ALL = listOf(CASH, BANK, UPI, CREDIT_CARD, DEBIT_CARD, WALLET, CHECK, CREDIT)
    
    fun getDisplayName(method: String): String
}
```

### Payment Method Selection UI

```
NewInvoiceScreen
    └── Payment Method Selector
        ├── CASH (default)
        ├── BANK
        ├── UPI
        ├── CREDIT_CARD
        ├── DEBIT_CARD
        ├── WALLET
        ├── CHECK
        └── CREDIT (Udhaar)
```

### Payment Status Tracking

| Status | Meaning |
|--------|---------|
| `PENDING` | Invoice created, awaiting payment |
| `PAID` | Payment received in full |
| `CREDIT` | Payment deferred (customer credit) |
| `PARTIAL` | Partial payment received |
| `OVERDUE` | Payment past due date |

---

## 12. Extension / Plugin System

### ExtensionManager

```kotlin
class ExtensionManager(private val context: Context) {
    fun register(extension: DataSourceExtension)
    fun getExtension(id: String): DataSourceExtension?
    fun getAllExtensions(): List<DataSourceExtension>
    fun loadFromAllSources(currentProducts: List<Product>): List<Product>
    fun shareFile(file: File, mimeType: String)
    fun openPdf(file: File)
}
```

### DataSourceExtension Interface

```kotlin
interface DataSourceExtension {
    val id: String
    val name: String
    val description: String
    val iconRes: Int
    fun loadProducts(existing: List<Product>): List<Product>
    fun isAvailable(context: Context): Boolean
}
```

### Registered Extensions

| Extension | ID | Icon | Purpose |
|-----------|----|------|---------|
| CSV Import | `csv_import` | Save | Import products from CSV |
| Excel Import | `excel_import` | Save | Import products from Excel |
| API Sync | `api_sync` | Upload | Sync from remote API |
| Barcode Scanner | `barcode_scanner` | Camera | Scan barcodes |
| QR Scanner | `qr_scanner` | Camera | Scan QR codes |
| Manual Entry | `manual_entry` | Edit | Manual product entry |
| SMS Sync | `sms_sync` | Send | Sync orders from SMS |
| Web Portal | `web_portal` | Upload | Web dashboard sync |
| Supabase Sync | (built-in) | Cloud | Cloud sync |

### Settings Screen — Extension Management

```
SettingsScreen
    ├── Data Sources & Extensions
    │   ├── CSV Import (toggle)
    │   ├── API Sync (toggle)
    │   ├── Barcode Scanner (toggle)
    │   ├── QR Scanner (toggle)
    │   ├── Manual Entry (toggle)
    │   ├── Excel Import (toggle)
    │   ├── SMS Sync (toggle)
    │   └── Web Portal (toggle)
    │
    ├── App Configuration
    │   ├── Language (English / Urdu)
    │   ├── Sync Status
    │   └── Receipt Settings (PDF / Print / WhatsApp)
    │
    └── Back to Dashboard
```

---

## 13. Data Flow Diagrams

### Invoice Creation Data Flow

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│  NewInvoice  │────▶│ NewInvoiceVM │────▶│ Calculate   │
│   Screen     │     │              │     │ InvoiceTotals│
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                  │
                                                  ▼
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ InvoicePreview│◀────│  saveInvoice │◀────│ DeductStock │
│   Screen     │     │              │     │ UseCase     │
└─────────────┘     └──────────────┘     └─────────────┘
        │
        ▼
┌─────────────┐     ┌──────────────┐
│ ReceiptGen   │────▶│ PDF/Print/   │
│ er           │     │ WhatsApp     │
└─────────────┘     └──────────────┘
```

### Product Scanning Data Flow

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ ScanHubScreen│────▶│ ScanHubVM    │────▶│ ML Kit      │
│              │     │              │     │ Barcode/QR  │
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                  │
                                                  ▼
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ Product      │◀────│ getByBarcode │◀────│ ProductDao  │
│ Found/Add    │     │ or getByQrCode│    │             │
└─────────────┘     └──────────────┘     └─────────────┘
```

### Dashboard Data Flow

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ Dashboard    │────▶│ DashboardVM  │────▶│ GetDailySales│
│ Screen       │     │              │     │ UseCase     │
└─────────────┘     └──────────────┘     └──────┬──────┘
                                                  │
                                                  ▼
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│ Live Metrics │◀────│ loadDashboard│◀────│ ProductDao  │
│ (Today Sales │     │ Data()       │     │ InvoiceDao  │
│  Orders,     │     │              │     │             │
│  Low Stock,  │     └──────────────┘     └─────────────┘
│  Credit)     │
└─────────────┘
```

---

## 14. Build & Deployment

### Build Instructions

```bash
# Prerequisites
# - Android Studio Hedgehog or later
# - JDK 17
# - Android SDK 34

# Build debug APK
cd aimrpos
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test
```

### Note on gradlew

The project does not include a Gradle wrapper (`gradlew`). To build:

1. Open the project in Android Studio
2. Use **Build > Build Bundle(s) / APK(s) > Build APK(s)**
3. Or use a locally installed Gradle: `gradle assembleDebug`

### ProGuard (Release)

```proguard
-keep class com.aimr.aimrpos.** { *; }
-keep class androidx.room.** { *; }
-keep class com.google.mlkit.** { *; }
```

---

## Git History

| Commit | Message | Files |
|--------|---------|-------|
| `f6a6c88` | Practical POS business logic, 8 product types, barcode, receipts, payment methods, extensions, professional UI | 22 files |
| `170965f` | Comprehensive README with architecture diagrams and feature visualization | README.md |
| `c681138` | Initialize AIMR POS ecosystem | 118 files |
| `6254c5b` | Initial commit | — |

---

## File Count Summary

| Layer | Count |
|-------|-------|
| Presentation Screens | 30+ |
| ViewModels | 25+ |
| Entities | 19 |
| DAOs | 20 |
| Repositories | 10+ |
| Use Cases | 20+ |
| UI Components | 10+ |
| Utils | 3 |
| Theme | 3 |
| Navigation | 2 |
| DI | 1 |
| **Total Kotlin Files** | **~120** |

---

*Document generated for AIMR POS v3.0 — Enterprise Android POS Application*
