# AIMR POS — Enterprise Android Application

> **AIMRAN POS** — A complete enterprise-grade Point of Sale system for Android, built with Jetpack Compose, MVVM architecture, Room database, Hilt DI, and offline-first sync.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                        AIMR POS Architecture                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────┐    ┌──────────────┐    ┌─────────────────────┐  │
│  │  UI Layer     │    │  Domain Layer │    │  Data Layer         │  │
│  │  (Compose)    │───▶│  (Models +    │───▶│  (Room + Repo +     │  │
│  │  - Screens    │    │   UseCases)   │    │   Sync + DI)        │  │
│  │  - ViewModels │    │               │    │                     │  │
│  │  - Navigation │    │               │    │                     │  │
│  └──────────────┘    └──────────────┘    └─────────────────────┘  │
│         │                   │                    │                  │
│         ▼                   ▼                    ▼                  │
│  ┌──────────────┐    ┌──────────────┐    ┌─────────────────────┐  │
│  │  Bottom Nav   │    │  Clean Arch  │    │  Room Database      │  │
│  │  + Top Bar    │    │  + MVVM      │    │  + Hilt DI          │  │
│  │  + QR Scanner │    │  + Offline   │    │  + Supabase Sync    │  │
│  └──────────────┘    └──────────────┘    └─────────────────────┘  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📱 Screen Navigation Map

```
┌─────────────────────────────────────────────────────────────────────┐
│                        APPLICATION FLOW                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌───────────┐                                                      │
│  │   LOGIN   │  (PIN-based auth)                                    │
│  └─────┬─────┘                                                      │
│        ▼                                                            │
│  ┌───────────┐    ┌─────────────────────────────────────────────┐   │
│  │ DASHBOARD │───▶│  Quick Actions:                             │   │
│  │ (Home)    │    │  • New Invoice                              │   │
│  └─────┬─────┘    │  • Add Product                              │   │
│        │          │  • View Customers                            │   │
│        │          └─────────────────────────────────────────────┘   │
│        │                                                            │
│        ▼                                                            │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                    BOTTOM NAVIGATION BAR                      │  │
│  │  Home │ Stock │ Invoice │ Customers │ Settings               │  │
│  └───────────────────────────────────────────────────────────────┘  │
│                                                                     │
│  ┌─────────────────────── ENTERPRISE SCREENS ────────────────────┐ │
│  │                                                                 │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐   │ │
│  │  │ SCAN HUB    │  │ DOCUMENT    │  │ PURCHASE ORDERS     │   │ │
│  │  │ Camera      │  │ VAULT       │  │ Create PO, Track    │   │ │
│  │  │ Capture     │  │ Searchable  │  │ Status, Convert     │   │ │
│  │  │ Auto-Crop   │  │ Archive     │  │ to GRN              │   │ │
│  │  │ Type Select │  │ Filterable  │  │                     │   │ │
│  │  └──────┬──────┘  └──────┬──────┘  └──────────┬──────────┘   │ │
│  │         │                │                     │               │ │
│  │         ▼                ▼                     ▼               │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐   │ │
│  │  │ SCAN REVIEW │  │ APPROVAL    │  │ MULTI-LOCATION      │   │ │
│  │  │ Side-by-side│  │ INBOX       │  │ DASHBOARD           │   │ │
│  │  │ Original +  │  │ Approve/    │  │ Stock Levels,       │   │ │
│  │  │ Extracted   │  │ Reject      │  │ Transfer Requests   │   │ │
│  │  │ Fields Edit │  │ with Comments│ │                     │   │ │
│  │  └─────────────┘  └─────────────┘  └─────────────────────┘   │ │
│  │                                                                 │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐   │ │
│  │  │ AUDIT TRAIL │  │ SUPPLIER    │  │ GRN / RETURN /      │   │ │
│  │  │ Viewer      │  │ LEDGER      │  │ STOCK TRANSFER      │   │ │
│  │  │ Searchable  │  │ Payment     │  │                     │   │ │
│  │  │ Change Hist │  │ Terms,      │  │                     │   │ │
│  │  │ (Admin)     │  │ Aging Report│  │                     │   │ │
│  │  └─────────────┘  └─────────────┘  └─────────────────────┘   │ │
│  └───────────────────────────────────────────────────────────────┘ │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Database Schema (v3)

```
┌─────────────────────────────────────────────────────────────────────┐
│                     DATABASE ENTITIES                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  LOCATIONS       │  │  SUPPLIERS       │  │  PRODUCTS        │ │
│  │──────────────────│  │──────────────────│  │──────────────────│ │
│  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │ │
│  │ businessId       │  │ businessId       │  │ name             │ │
│  │ name             │  │ name             │  │ sku              │ │
│  │ address          │  │ contactPerson    │  │ barcode          │ │
│  │ phone            │  │ phone            │  │ categoryId       │ │
│  │ managerUserId    │  │ email            │  │ costPrice        │ │
│  │ isWarehouse      │  │ paymentTerms     │  │ salePrice        │ │
│  │ latitude/longitude│ │ creditLimit      │  │ stockQty         │ │
│  │ updatedAt        │  │ currentBalance   │  │ unit             │ │
│  │ syncStatus       │  │ taxNumber        │  │ lowStockThreshold│ │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘ │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  PURCHASE_ORDERS │  │  GRNs            │  │  RETURNS         │ │
│  │──────────────────│  │──────────────────│  │──────────────────│ │
│  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │ │
│  │ poNumber         │  │ grnNumber        │  │ returnNumber     │ │
│  │ supplierId       │  │ purchaseOrderId  │  │ originalInvoiceId│ │
│  │ locationId       │  │ supplierId       │  │ originalPoId     │ │
│  │ status           │  │ locationId       │  │ customerId       │ │
│  │ createdByUserId  │  │ items            │  │ supplierId       │ │
│  │ approvedByUserId │  │ totalQty         │  │ refundAmount     │ │
│  │ sourceDocumentId │  │ totalAmount      │  │ items            │ │
│  │ updatedAt        │  │ status           │  │ subtotal         │ │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘ │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  PO_LINE_ITEMS   │  │  STOCK_LEDGER    │  │  STOCK_TRANSFERS │ │
│  │──────────────────│  │──────────────────│  │──────────────────│ │
│  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │ │
│  │ poId (FK)        │  │ productId (FK)   │  │ transferNumber   │ │
│  │ productId        │  │ locationId       │  │ fromLocationId   │ │
│  │ quantityOrdered  │  │ movementType     │  │ toLocationId     │ │
│  │ quantityReceived │  │ quantity         │  │ productId        │ │
│  │ unitCost         │  │ referenceType    │  │ quantity         │ │
│  │ lineTotal        │  │ referenceId      │  │ status           │ │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘ │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  DOCUMENTS       │  │  APPROVALS       │  │  AUDIT_LOG       │ │
│  │──────────────────│  │──────────────────│  │──────────────────│ │
│  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │ │
│  │ businessId       │  │ requestType      │  │ businessId       │ │
│  │ documentType     │  │ entityId         │  │ userId           │ │
│  │ fileName         │  │ entityType       │  │ userName         │ │
│  │ filePath         │  │ requestedByUserId│  │ action           │ │
│  │ ocrRawText       │  │ currentApprover  │  │ entityType       │ │
│  │ extractionStatus │  │ approvalChain    │  │ entityId         │ │
│  │ linkedRecordType │  │ status           │  │ oldValueJson     │ │
│  │ linkedRecordId   │  │ comments         │  │ newValueJson     │ │
│  │ scannedByUserId  │  │ createdAt        │  │ deviceId         │ │
│  │ locationId       │  │ resolvedAt       │  │ ipAddress        │ │
│  │ confidence       │  │ syncStatus       │  │ timestamp        │ │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘ │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  INVOICES        │  │  PAYMENTS        │  │  CUSTOMERS       │ │
│  │──────────────────│  │──────────────────│  │──────────────────│ │
│  │ id (PK)          │  │ id (PK)          │  │ id (PK)          │ │
│  │ invoiceNumber    │  │ invoiceId (FK)   │  │ name             │ │
│  │ customerId       │  │ customerId (FK)  │  │ phone            │ │
│  │ subtotal         │  │ supplierId (FK)  │  │ creditBalance    │ │
│  │ taxAmount        │  │ amount           │  │ updatedAt        │ │
│  │ total            │  │ method           │  │ syncStatus       │ │
│  │ paymentStatus    │  │ reference        │  └──────────────────┘ │
│  └──────────────────┘  └──────────────────┘                        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📊 Enterprise Feature Matrix

```
┌─────────────────────────────────────────────────────────────────────┐
│                     ENTERPRISE FEATURES                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │  📷 SCAN HUB    │  │  📋 SCAN REVIEW │  │  🗄️ DOC VAULT   │    │
│  │─────────────────│  │─────────────────│  │─────────────────│    │
│  │ Camera capture  │  │ Side-by-side    │  │ Searchable      │    │
│  │ Auto-crop       │  │ original + OCR  │  │ archive         │    │
│  │ Type selector   │  │ Edit fields     │  │ Filter by type  │    │
│  │ 6 doc types     │  │ before save     │  │ date, linked    │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│                                                                     │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │  📝 PURCHASE    │  │  ✅ APPROVAL     │  │  🏭 MULTI-LOC   │    │
│  │─────────────────│  │─────────────────│  │─────────────────│    │
│  │ Create PO       │  │ Pending queue   │  │ Stock levels    │    │
│  │ Track status    │  │ Approve/Reject  │  │ across warehouses│   │
│  │ Convert to GRN  │  │ with comments   │  │ Transfer reqs   │    │
│  │ Line items      │  │ Role-based      │  │ Location mgmt   │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│                                                                     │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │  🔍 AUDIT TRAIL │  │  💰 SUPPLIER    │  │  📦 STOCK       │    │
│  │─────────────────│  │─────────────────│  │  MOVEMENT       │    │
│  │ Searchable log  │  │ Payment terms   │  │ IN/OUT/TRANSFER │    │
│  │ Admin-only      │  │ Outstanding bal │  │ ADJUSTMENT      │    │
│  │ Change history  │  │ Aging report    │  │ Per location    │    │
│  │ old/new values  │  │ Credit tracking │  │ Reference trace │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                      DATA FLOW                                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐     │
│  │  Camera   │───▶│  ML Kit  │───▶│  OCR     │───▶│  Review  │     │
│  │  Capture  │    │  Detect  │    │  Extract │    │  Edit    │     │
│  └──────────┘    └──────────┘    └──────────┘    └────┬─────┘     │
│                                                        │           │
│                                                        ▼           │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐     │
│  │  Stock   │◀───│  GRN     │◀───│  PO      │◀───│  Supplier│     │
│  │  Update  │    │  Receive │    │  Create  │    │  Select  │     │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘     │
│        │                                                         │
│        ▼                                                         │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐     │
│  │  Audit   │    │  Sync    │    │  Supabase│    │  Offline │     │
│  │  Log     │    │  Engine  │    │  Cloud   │    │  First   │     │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘     │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Technology Stack

```
┌─────────────────────────────────────────────────────────────────────┐
│                       STACK                                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │
│  │  Kotlin      │  │  Jetpack    │  │  Room       │                │
│  │  Android     │  │  Compose    │  │  Database   │                │
│  └─────────────┘  └─────────────┘  └─────────────┘                │
│                                                                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │
│  │  Hilt (DI)   │  │  Navigation │  │  WorkManager│                │
│  │  Dependency  │  │  Component  │  │  Background │                │
│  │  Injection   │  │  Graph      │  │  Sync       │                │
│  └─────────────┘  └─────────────┘  └─────────────┘                │
│                                                                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │
│  │  ML Kit      │  │  CameraX    │  │  Supabase   │                │
│  │  Barcode +   │  │  Camera     │  │  Cloud Sync │                │
│  │  OCR         │  │  Capture    │  │  Auth + RT  │                │
│  └─────────────┘  └─────────────┘  └─────────────┘                │
│                                                                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │
│  │  Vico Charts │  │  Material 3 │  │  RTL/Urdu   │                │
│  │  Visualization│ │  Theme      │  │  Bilingual  │                │
│  └─────────────┘  └─────────────┘  └─────────────┘                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
aimrpos/
├── app/
│   ├── src/main/
│   │   ├── java/com/aimr/aimrpos/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── entity/          # Room entities (33 files)
│   │   │   │   │   ├── dao/             # Data Access Objects (17 files)
│   │   │   │   │   └── AimrPosDatabase.kt
│   │   │   │   ├── repository/          # Repository implementations
│   │   │   │   ├── qr/                  # QR scanning
│   │   │   │   ├── scanning/            # Document scanning pipeline
│   │   │   │   └── sync/                # Background sync worker
│   │   │   ├── di/                      # Hilt dependency injection
│   │   │   ├── domain/
│   │   │   │   ├── model/               # Domain models (22 files)
│   │   │   │   ├── repository/          # Repository interfaces (16)
│   │   │   │   └── usecase/             # Use cases
│   │   │   ├── presentation/
│   │   │   │   ├── login/               # PIN login screen
│   │   │   │   ├── dashboard/           # Home dashboard
│   │   │   │   ├── inventory/           # Product management
│   │   │   │   ├── invoice/             # Invoice creation/preview
│   │   │   │   ├── customer/            # Customer ledger
│   │   │   │   ├── purchase/            # Purchase orders
│   │   │   │   ├── grn/                 # Goods received notes
│   │   │   │   ├── returninvoice/       # Returns & refunds
│   │   │   │   ├── approval/            # Approval queue
│   │   │   │   ├── audit/               # Audit trail viewer
│   │   │   │   ├── vault/               # Document vault
│   │   │   │   ├── scan/                # Document scanner + review
│   │   │   │   ├── scanhub/             # Scan hub (NEW)
│   │   │   │   ├── warehouse/           # Multi-location dashboard
│   │   │   │   ├── transfer/            # Stock transfers
│   │   │   │   ├── aging/               # Aging report
│   │   │   │   ├── supplierledger/      # Supplier ledger (NEW)
│   │   │   │   ├── reports/             # Sales/profit/stock reports
│   │   │   │   ├── qr/                  # QR scanner screen
│   │   │   │   └── settings/            # App settings
│   │   │   ├── navigation/              # NavGraph + Screen routes
│   │   │   ├── ecosystem/               # Feature registry
│   │   │   └── ui/
│   │   │       ├── bottombar/           # Cinematic bottom nav
│   │   │       ├── components/          # Reusable UI components
│   │   │       ├── display/             # Enhanced displays
│   │   │       └── theme/               # Material 3 theme
│   │   └── res/
│   │       ├── values/                  # English strings
│   │       ├── values-ur/               # Urdu strings (RTL)
│   │       └── values-night/            # Dark theme
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## 🚀 Setup & Build

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17+
- Android SDK 34
- Gradle 8.x

### Build Instructions
```bash
# Clone the repository
git clone <repo-url>

# Open in Android Studio
# Or build from command line:
./gradlew assembleDebug

# Run tests
./gradlew test
```

### Key Configuration
- **minSdk**: 26
- **targetSdk**: 34
- **compileSdk**: 34
- **Compose BOM**: 2024.02.00
- **Room**: 2.6.1
- **Navigation**: 2.7.6
- **Hilt**: 2.50

---

## 📋 Feature Checklist

```
✅  Authentication (PIN + Supabase)
✅  Product Management (CRUD, barcode, categories)
✅  Invoice Creation & Export (PDF, WhatsApp, Print)
✅  Customer Ledger (Credit tracking, udhaar)
✅  QR Scanning (ML Kit + payload parsing)
✅  Barcode Scanning (ML Kit on-device)
✅  Document Scanning (CameraX + OCR)
✅  Scan Hub (NEW - camera capture, auto-crop, type selector)
✅  Scan Review (side-by-side edit)
✅  Document Vault (searchable archive)
✅  Purchase Orders (create, track, convert to GRN)
✅  Goods Received Notes (GRN workflow)
✅  Returns & Refunds
✅  Approval Inbox (approve/reject with comments)
✅  Multi-Location Dashboard (stock levels, transfers)
✅  Audit Trail Viewer (searchable, admin-only)
✅  Supplier Ledger (payment terms, aging report)
✅  Stock Ledger (IN/OUT/TRANSFER/ADJUSTMENT)
✅  Stock Transfer Workflow
✅  Aging Report (0-30, 31-60, 61-90, 90+ days)
✅  Bilingual Support (English + Urdu RTL)
✅  Material 3 Theme with high contrast
✅  Offline-first with delta sync
✅  Supabase cloud sync
```

---

## 📄 License

MIT License — see LICENSE file for details.

---

*Built with ❤️ by AIMR Connect*
