# AIMRAN POS — Implementation Plan

## Decisions Log

| Decision | Value | Rationale |
|---|---|---|
| Backend | Supabase | Managed Postgres + Auth + Realtime; faster to ship; free tier sufficient for MVP |
| Auth model | PIN local unlock + Supabase sync layer | PIN is local-only (no network call on login); Supabase handles cloud identity and sync |
| App name | AIMRAN POS | As specified by the user |
| PDF generation | Android PdfDocument API | No external dependency; sufficient for MVP invoice PDFs |
| Chart library | Vico | Compose-native, lightweight, modern API |
| Barcode scanning | ML Kit Barcode Scanning | On-device, free, no API key needed |
| Sync strategy | Delta sync with last-write-wins + manual conflict screen | Matches blueprint; timestamps on every row |
| Localization | English + Urdu (RTL) | strings.xml + strings-ur.xml; Noto Nastaliq Urdu font |

---

## 1. Project Structure

```
aimrpos/
├── app/
│   ├── src/main/
│   │   ├── java/com/aimr/aimrpos/
│   │   │   ├── AimrPosApplication.kt          # Hilt Application
│   │   │   ├── MainActivity.kt                # Single Activity, Compose
│   │   │   ├── navigation/
│   │   │   │   ├── NavGraph.kt               # NavHost with all 9 screens
│   │   │   │   └── Screen.kt                 # Route definitions
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── entity/               # Room entities
│   │   │   │   │   ├── dao/                  # Room DAOs
│   │   │   │   │   └── AimrPosDatabase.kt    # Room database class
│   │   │   │   ├── remote/
│   │   │   │   │   ├── SupabaseClient.kt     # Supabase HTTP client
│   │   │   │   │   └── SyncWorker.kt         # WorkManager sync worker
│   │   │   │   └── repository/
│   │   │   │       ├── ProductRepository.kt
│   │   │   │       ├── InvoiceRepository.kt
│   │   │   │       ├── CustomerRepository.kt
│   │   │   │       └── SyncRepository.kt
│   │   │   ├── domain/
│   │   │   │   ├── model/                    # Domain models (clean, no Room annotations)
│   │   │   │   ├── usecase/                  # Use cases
│   │   │   │   └── repository/               # Repository interfaces
│   │   │   └── presentation/
│   │   │       ├── ui/
│   │   │       │   ├── theme/                # Material 3 theme, colors, typography
│   │   │       │   ├── components/           # Reusable composables
│   │   │       │   ├── login/                # PIN login screen
│   │   │       │   ├── dashboard/            # Dashboard screen
│   │   │       │   ├── inventory/            # Inventory list + add/edit
│   │   │       │   ├── invoice/              # New invoice + preview
│   │   │       │   ├── customer/             # Customer ledger
│   │   │       │   ├── reports/              # Reports + charts
│   │   │       │   └── settings/             # Settings screen
│   │   │       └── viewmodel/               # ViewModels for each screen
│   │   ├── res/
│   │   │   ├── values/strings.xml           # English strings
│   │   │   ├── values-ur/strings.xml        # Urdu strings
│   │   │   ├── font/                        # Noto Nastaliq Urdu font
│   │   │   └── xml/                         # Backup rules, etc.
│   │   └── assets/
│   │       └── fonts/NotoNastaliqUrdu-Regular.ttf
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts                          # Project-level
├── settings.gradle.kts
└── gradle.properties
```

---

## 2. Gradle Dependencies

### Project-level `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
}
```

### App-level `build.gradle.kts`

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.aimr.aimrpos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aimr.aimrpos"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Jetpack Compose + Material 3
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // ML Kit Barcode Scanning
    implementation("com.google.mlkit:barcode-scanning:17.2.0")

    // Supabase (sync + auth)
    implementation("io.github.jan-tennert.supabase:supabase-kt:2.5.0")
    implementation("io.github.jan-tennert.supabase:postgrest:2.5.0")
    implementation("io.github.jan-tennert.supabase:auth:2.5.0")
    implementation("io.github.jan-tennert.supabase:storage:2.5.0")

    // Vico (charts)
    implementation("com.patrykandpatrick.vico:compose-m3:2.1.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // DataStore (preferences)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation(platform("androidx.compose.compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

---

## 3. Data Layer — Room Entities

### 3.1 Sync Metadata Columns (every table)

Every entity includes:
- `id: String` (UUID, primary key)
- `updatedAt: Long` (Unix timestamp, milliseconds)
- `isDeleted: Boolean` (soft delete)
- `syncStatus: String` (enum: `PENDING`, `SYNCED`, `CONFLICT`)

### 3.2 Entities

**Product** (`ProductEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| name | String | English name |
| nameUr | String? | Urdu name |
| sku | String | Unique stock-keeping unit |
| barcode | String? | Barcode value |
| categoryId | String | Foreign key to Category |
| costPrice | Double | Purchase cost |
| salePrice | Double | Selling price |
| stockQty | Double | Current stock quantity |
| unit | String | Unit of measure (pcs, kg, liter, etc.) |
| lowStockThreshold | Double | Alert when stock falls below this |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

**Invoice** (`InvoiceEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| invoiceNumber | String | Human-readable, e.g., INV-20260801-001 |
| customerId | String? | Foreign key to Customer |
| subtotal | Double | Sum of line totals before tax/discount |
| taxAmount | Double | Calculated tax |
| discount | Double | Applied discount |
| total | Double | Final amount |
| paymentStatus | String | PENDING / PAID / PARTIAL |
| paymentMethod | String? | CASH / BANK / UPI / CREDIT |
| createdByUserId | String | Foreign key to User |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

**InvoiceItem** (`InvoiceItemEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| invoiceId | String | Foreign key to Invoice |
| productId | String | Foreign key to Product |
| quantity | Double | Quantity sold |
| unitPrice | Double | Price at time of sale |
| taxRate | Double | Tax rate percentage |
| lineTotal | Double | quantity * unitPrice |

**Customer** (`CustomerEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| name | String | Customer name |
| phone | String? | Contact number |
| address | String? | Physical address |
| creditBalance | Double | Outstanding udhaar amount |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

**User** (`UserEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| businessId | String | Foreign key to Business |
| name | String | User name |
| role | String | OWNER / STAFF |
| phone | String? | Contact |
| pinHash | String | Hashed PIN for local auth |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

**Business** (`BusinessEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| name | String | Business name |
| address | String? | Business address |
| taxNumber | String? | GST/sales tax registration |
| currency | String | PKR (default) |
| subscriptionTier | String | FREE / PRO |
| subscriptionExpiry | Long? | Timestamp |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

**Category** (`CategoryEntity.kt`)

| Column | Type | Notes |
|---|---|---|
| id | String (UUID) | Primary key |
| name | String | Category name |
| nameUr | String? | Urdu name |
| updatedAt | Long | Timestamp |
| isDeleted | Boolean | Soft delete |
| syncStatus | String | PENDING / SYNCED / CONFLICT |

### 3.3 DAOs

Each DAO provides:
- Insert/update/delete (with sync_status set to PENDING)
- Query methods for each screen's needs
- A `getUnsyncedRows()` method for the sync engine

**ProductDao**
- `upsert(product: ProductEntity)`
- `getById(id: String): ProductEntity?`
- `getAll(): Flow<List<ProductEntity>>`
- `search(query: String): Flow<List<ProductEntity>>`
- `getByCategory(categoryId: String): Flow<List<ProductEntity>>`
- `getLowStock(): Flow<List<ProductEntity>>`
- `getUnsynced(): Flow<List<ProductEntity>>`

**InvoiceDao**
- `upsert(invoice: InvoiceEntity)`
- `getById(id: String): InvoiceEntity?`
- `getAll(): Flow<List<InvoiceEntity>>`
- `getToday(): Flow<List<InvoiceEntity>>`
- `getByCustomer(customerId: String): Flow<List<InvoiceEntity>>`
- `getUnsynced(): Flow<List<InvoiceEntity>>`

**InvoiceItemDao**
- `insert(item: InvoiceItemEntity)`
- `getByInvoice(invoiceId: String): Flow<List<InvoiceItemEntity>>`
- `deleteByInvoice(invoiceId: String)`

**CustomerDao**
- `upsert(customer: CustomerEntity)`
- `getById(id: String): CustomerEntity?`
- `getAll(): Flow<List<CustomerEntity>>`
- `getWithCredit(): Flow<List<CustomerEntity>>`
- `getUnsynced(): Flow<List<CustomerEntity>>`

**UserDao**
- `upsert(user: UserEntity)`
- `getById(id: String): UserEntity?`
- `getByBusiness(businessId: String): Flow<List<UserEntity>>`
- `getActive(): Flow<UserEntity?>` (current logged-in user)

**BusinessDao**
- `upsert(business: BusinessEntity)`
- `getActive(): Flow<BusinessEntity?>`

**CategoryDao**
- `upsert(category: CategoryEntity)`
- `getAll(): Flow<List<CategoryEntity>>`

### 3.4 Room Database

`AimrPosDatabase.kt` — single `@Database` class listing all entities, version 1, with `fallbackToDestructiveMigration()` for MVP. Provides DAO accessors.

---

## 4. Domain Layer

### 4.1 Repository Interfaces

- `ProductRepository` — CRUD + search + low-stock queries
- `InvoiceRepository` — CRUD + invoice number generation + daily sales aggregation
- `CustomerRepository` — CRUD + credit balance tracking
- `SyncRepository` — push/pull unsynced rows, update sync_status
- `AuthRepository` — PIN verification (local hash comparison), Supabase session management

### 4.2 Use Cases

- `GenerateInvoiceNumberUseCase` — produces `INV-YYYYMMDD-NNN` format
- `CalculateInvoiceTotalsUseCase` — subtotal, tax, discount, total from line items
- `CheckLowStockUseCase` — returns products below threshold
- `GetDailySalesUseCase` — aggregates today's invoice totals
- `GetStockValuationUseCase` — sum of (stockQty * costPrice) per product

---

## 5. Presentation Layer

### 5.1 Navigation

`Screen.kt` defines 9 routes:

| Route | Composable |
|---|---|
| `login` | LoginScreen |
| `dashboard` | DashboardScreen |
| `inventory` | InventoryListScreen |
| `inventory_add_edit` | ProductFormScreen |
| `invoice_new` | NewInvoiceScreen |
| `invoice_preview/{invoiceId}` | InvoicePreviewScreen |
| `customers` | CustomerLedgerScreen |
| `reports` | ReportsScreen |
| `settings` | SettingsScreen |

`NavGraph.kt` wires all routes with `composable()` calls and `rememberNavController()`.

### 5.2 Theme

Material 3 theme with:
- Primary: Deep blue (`#1565C0`) — high contrast, accessible
- Secondary: Amber (`#FF8F00`) — for CTAs and alerts
- Surface: White (`#FFFFFF`)
- Error: Red (`#D32F2F`)
- Typography: Large body text (16sp+), clear hierarchy
- RTL support via `layoutDirection = LayoutDirection.Rtl` when Urdu is active

### 5.3 Reusable Components

- `PrimaryButton` — large tap target (min 48dp), full-width option
- `OutlinedTextField` — with clear labels and error states
- `StockBadge` — green (in stock), yellow (low), red (out of stock)
- `TopBar` — title + back button + action icon
- `EmptyState` — illustration + message for empty lists
- `LoadingSpinner` — centered progress indicator

---

## 6. Screen Implementation Details

### 6.1 PIN Login / Business Setup

- 4-digit PIN input (custom keyboard, large digits)
- On first launch: business setup wizard (name, address, tax number, currency)
- PIN verified locally against `pinHash` in Room (SHA-256)
- On successful login: navigate to Dashboard
- Supabase anonymous auth session created in background

### 6.2 Dashboard

- Top section: Today's date, business name
- KPI cards: Today's Sales (Rs), Total Invoices, Low Stock Items, Outstanding Credit
- Quick action buttons: New Invoice, Add Product, View Customers
- Low stock alert list (if any products below threshold)
- Sample data populates KPIs and alert list

### 6.3 Inventory List

- Search bar (text input)
- Category filter dropdown
- Stock status filter (All / In Stock / Low / Out of Stock)
- Product list with: name, SKU, stock qty + badge, sale price
- Low-stock items highlighted with red badge
- FAB to add new product
- Tap item navigates to Add/Edit product screen

### 6.4 Add/Edit Product

- Form fields: name, name_ur, sku, barcode, category, cost_price, sale_price, stock_qty, unit, low_stock_threshold
- Barcode scan button (opens ML Kit scanner, populates barcode field)
- Save button persists to Room (sync_status = PENDING)
- Validation: required fields, positive prices, unique SKU

### 6.5 New Invoice

- Customer search/select (existing or new)
- Product search/select with auto-complete
- Add item row: product, quantity, unit_price, tax_rate (auto-calculated line_total)
- Running total display: subtotal, tax, discount, total
- Payment method selector (CASH / BANK / UPI / CREDIT)
- Save button: creates Invoice + InvoiceItems in Room (sync_status = PENDING)
- Navigate to Invoice Preview on save

### 6.6 Invoice Preview

- Full invoice layout (company name, customer info, itemized table, totals)
- Print button (Android PrintManager)
- PDF export button (Android PdfDocument API)
- WhatsApp share button (ACTION_SEND with PDF attachment URI)
- Back to Dashboard

### 6.7 Customer Ledger

- Customer list with credit balance highlighted
- Tap customer: detail screen with payment history (invoices with that customer)
- Add payment button: reduces credit_balance, creates payment record
- Credit/udhaar tracking: outstanding balance shown prominently

### 6.8 Reports

- Time range selector: Daily / Weekly / Monthly
- Sales summary card: total sales, total orders, avg order value
- Profit margin card: revenue - COGS
- Stock valuation card: total inventory value
- Bar chart: daily sales (Vico)
- Line chart: weekly revenue trend (Vico)

### 6.9 Settings

- Language toggle (English / Urdu) — persists via DataStore
- Business info editing
- Tax rate configuration (default 17% Pakistani sales tax)
- User management (add/edit staff, assign PIN + role)
- Sync status indicator (last sync time, pending changes count)

---

## 7. Sync Strategy

### 7.1 Sync Worker (WorkManager)

`SyncWorker.kt` runs periodically (every 15 minutes, or on network connectivity change):

1. **Push:** Query all rows with `sync_status = PENDING` from each table. POST to Supabase REST API. On success, update local `sync_status = SYNCED`.
2. **Pull:** Query Supabase for rows where `updated_at > lastSyncedAt`. Insert/update local Room rows. Set `sync_status = SYNCED`.
3. **Conflict detection:** If a pulled row has `updated_at` newer than local row with same `id`, and local `sync_status = PENDING`, mark as `CONFLICT`.
4. **Update `lastSyncedAt`** timestamp in DataStore.

### 7.2 Conflict Resolution

- Last-write-wins for automatic resolution (timestamp comparison)
- Manual resolution screen for true conflicts (same record edited by two users offline): show both versions, let owner choose which to keep.

### 7.3 Delta Sync

- Only rows with `sync_status = PENDING` are pushed
- Only rows with `updated_at > lastSyncedAt` are pulled
- `lastSyncedAt` stored in DataStore

---

## 8. Localization

### 8.1 String Resources

- `res/values/strings.xml` — English strings
- `res/values-ur/strings.xml` — Urdu strings (translated)

### 8.2 RTL Support

- `android:supportsRtl="true"` in AndroidManifest
- Compose `LayoutDirection` toggled based on selected language
- Urdu text rendered with Noto Nastaliq font loaded from assets

### 8.3 Font

- Bundle `NotoNastaliqUrdu-Regular.ttf` in `assets/fonts/`
- Apply via `FontFamily` in Compose `TextStyle` when Urdu is active

---

## 9. Sample Data

Populated on first launch (only if database is empty):

### Sample Business
- Name: "AIMRAN General Store"
- Address: "Main Bazaar, Lahore"
- Tax Number: "0123456-7"
- Currency: PKR
- Subscription Tier: FREE

### Sample Users
- Owner: PIN `1234`, role OWNER
- Staff: PIN `5678`, role STAFF

### Sample Categories
- Groceries, Beverages, Snacks, Household, Stationery

### Sample Products (10 items)

| Name | SKU | Barcode | Category | Cost | Sale | Stock | Unit | Threshold |
|---|---|---|---|---|---|---|---|---|
| Atta 5kg | ATTA-001 | 8901234567890 | Groceries | 350.00 | 420.00 | 12 | pcs | 5 |
| Sugar 1kg | SUGR-001 | 8901234567891 | Groceries | 220.00 | 250.00 | 3 | kg | 5 |
| Cooking Oil 1L | OIL-001 | 8901234567892 | Groceries | 180.00 | 210.00 | 8 | liter | 3 |
| Biscuits 400g | BISC-001 | 8901234567893 | Snacks | 80.00 | 100.00 | 2 | pcs | 5 |
| Shampoo 200ml | SHMP-001 | 8901234567894 | Household | 60.00 | 85.00 | 0 | pcs | 3 |
| Soap Bar | SOAP-001 | 8901234567895 | Household | 25.00 | 35.00 | 25 | pcs | 10 |
| Pen 12pk | PEN-001 | 8901234567896 | Stationery | 120.00 | 150.00 | 5 | pcs | 3 |
| Notebook A4 | NBK-001 | 8901234567897 | Stationery | 45.00 | 60.00 | 15 | pcs | 5 |
| Rice 10kg | RICE-001 | 8901234567898 | Groceries | 850.00 | 950.00 | 7 | kg | 3 |
| Tea 500g | TEA-001 | 8901234567899 | Groceries | 300.00 | 350.00 | 1 | kg | 5 |

### Sample Customers
- Ali Ahmed — Phone: 0300-1234567, Credit: 0
- Fatima Khan — Phone: 0321-9876543, Credit: 2500
- Usman Malik — Phone: 0345-5551234, Credit: 1800

### Sample Invoices
- INV-20260801-001: Fatima Khan, 2 items, total Rs 560, PAID
- INV-20260801-002: Usman Malik, 3 items, total Rs 1240, CREDIT (udhaar)
- INV-20260801-003: Ali Ahmed, 1 item, total Rs 150, PAID

---

## 10. Implementation Order

1. **Project scaffolding** — Gradle config, Hilt Application, Room database with all entities/DAOs, navigation graph with empty composables
2. **PIN Login + Business Setup** — Login screen, local PIN verification, first-launch wizard
3. **Dashboard** — KPI cards, quick actions, low-stock alerts, sample data population
4. **Inventory List** — Search, filter, stock badges, FAB to add product
5. **Add/Edit Product** — Form with barcode scan, validation, Room persistence
6. **New Invoice** — Customer picker, product picker, item rows, tax calc, save
7. **Invoice Preview** — PDF export, WhatsApp share, print
8. **Customer Ledger** — Customer list, credit tracking, payment history
9. **Reports** — Charts, sales summaries, stock valuation
10. **Settings** — Language toggle, business info, tax config, user management
11. **Sync Engine** — WorkManager sync worker, delta sync, conflict handling
12. **Supabase backend setup** — Supabase project config, table schemas, RLS policies

---

## 11. Supabase Schema (Backend)

Tables mirror the Room entities exactly. Each table has the same columns including `id` (UUID), `updated_at` (timestamp), `is_deleted` (boolean), `sync_status` (text). Row-Level Security (RLS) enabled: users can only read/write rows belonging to their `business_id`.

---

## 12. Non-Functional Requirements

- **Offline-first:** All screens read/write Room. No loading spinners on network operations.
- **Performance:** Room queries on background coroutines. Compose recomposition optimized with `remember` and `derivedStateOf`.
- **Accessibility:** Minimum touch target 48dp. High-contrast colors. Large text (16sp body). Content descriptions on all icons.
- **Security:** PIN hashed with SHA-256 + salt stored in DataStore. No plaintext PINs. Supabase API keys stored in `local.properties` (not in version control).
- **Testing:** Unit tests for use cases and repository logic. Instrumentation tests for critical UI flows (login, invoice creation).
