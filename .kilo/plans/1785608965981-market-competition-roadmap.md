# AIMR POS — Market Competition Roadmap

> Strategic plan to compete with grocery POS leaders (LOC SMS, IT Retail, KORONA, Loyverse, Vyapar, PosBytz)
> Integrates competitive analysis findings with the existing AIMR POS codebase.

---

## 1. Reality Check — Current State vs. Competitors

### 1.1 Codebase Reality
The git history (`c681198`) shows this is a **spec/README-only repository** — no `app/` module, no source code, no Gradle files. All `✅` items in the README are specifications, not shipped code.

### 1.2 Competitive Position vs. Shipping Products

| Feature | AIMR Status | Loyverse | Vyapar | PosBytz | LOC SMS | IT Retail | KORONA |
|---------|-------------|----------|--------|---------|---------|-----------|--------|
| Offline-first | spec | ✅ | ✅ | ✅ | ✅ | ✅ | ❌ |
| Barcode/QR scanning | spec | ✅ | ✅ | ✅ | ✅ | ✅ | ❌ |
| Multi-location | spec | Paid | ✅ | ✅ | ✅ | ✅ | ✅ |
| PO → GRN workflow | spec | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| Approval workflow | spec | ❌ | Partial | Partial | ✅ | ✅ | ✅ |
| Audit trail (old/new) | spec | Basic | Basic | Basic | ✅ | ✅ | ✅ |
| Supplier ledger + aging | spec | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Document scanning/OCR | spec | Paid | Limited | Partial | ✅ | ✅ | ✅ |
| Bilingual RTL (Urdu) | spec | ❌ | ✅ (Hindi) | ❌ | ❌ | ❌ | ❌ |
| **Tiered/bulk pricing** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Bulk price update (CSV)** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Promotions engine** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Weighted/scale items** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Expiry/batch tracking** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Cost-linked repricing** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Shelf label printing** | ❌ | ❌ | ❌ | ❌ | ✅ | ✅ | ✅ |
| **Loyalty/rewards** | ❌ | ✅ (free) | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Self-checkout/kiosk** | ❌ | ✅ | ❌ | ❌ | ✅ | ✅ | ✅ |

### 1.3 Key Insight
- **Strength**: Back-office/workflow layer (PO→GRN, approvals, audit trail, OCR document capture) is ahead of SMB Android POS apps.
- **Weakness**: Selling price management for supermarkets is unaddressed — this is the #1 grocery buyer requirement.
- **Opportunity**: Bilingual Urdu RTL support is a unique differentiator in the Pakistani/South Asian market.

---

## 2. Strategic Feature Plan

### Tier 1 — Required for "Supermarket Price Management" Positioning

#### 2.1 Tiered/Bulk Pricing
**Priority: Highest**

**Data Model Addition:**
- New entity: `PriceTierEntity`
  - `id`, `productId` (FK), `minQty`, `price`, `customerType`, `effectiveFrom`, `effectiveTo`
- New domain model: `PriceTier`
- New DAO: `PriceTierDao` with queries:
  - `getByProduct(productId)` — get all tiers sorted by minQty
  - `getByProductAndQuantity(productId, qty, customerType)` — resolve the applicable tier
  - `insertAll()`, `deleteForProduct(productId)`

**UI Implementation:**
- New screen: `PriceTierScreen` (Product detail → Pricing tab → Manage Tiers)
- New ViewModel: `PriceTierViewModel`
- Use `LazyColumn` with swipe-to-delete, `+ Add Tier` button
- Each tier row: minQty, price, customerType (Retail/Wholesale), date range

**Navigation:** `Screen PriceTiers("price_tiers/{productId}")`

**Invoice Integration:**
- In `NewInvoiceViewModel`, when adding a line item:
  - Look up product's price tiers
  - If quantity exceeds a tier's minQty, use the tier price
  - Support customer-type-based pricing (Wholesale customer gets wholesale tiers)

**Files to Create:**
- `data/local/entity/PriceTierEntity.kt`
- `data/local/dao/PriceTierDao.kt`
- `domain/model/PriceTier.kt` (add to EnterpriseModels.kt)
- `domain/repository/PriceTierRepository.kt` (add to RepositoryInterfaces.kt)
- `data/repository/PriceTierRepositoryImpl.kt` (add to RepositoryImpl.kt)
- `di/AppModule.kt` — add PriceTierDao provider
- `presentation/pricing/PriceTierScreen.kt`
- `presentation/pricing/PriceTierViewModel.kt`
- `navigation/Screen.kt` — add PriceTiers route
- `navigation/NavGraph.kt` — add NavGraph destination
- Update `AimrPosDatabase.kt` — add entity + DAO
- Update `Presentation/Invoice/NewInvoiceViewModel.kt` — tiered pricing resolution

#### 2.2 Bulk Price Update Tool (CSV Import/Export)
**Priority: High**

**Data Model:** No new entities — operates on ProductEntity directly.

**Implementation:**
- New screen: `BulkPriceUpdateScreen`
- New ViewModel: `BulkPriceUpdateViewModel`
- CSV import: parse CSV → display preview table → confirm before applying
- CSV export: export current product prices by category/supplier
- Preview-before-commit step (critical for user trust)

**CSV Format:**
```csv
sku,newSalePrice,newCostPrice,category,supplierId
SKU001,49.99,25.00,Beverages,SUPP001
SKU002,129.99,65.00,Snacks,SUPP002
```

**Files to Create:**
- `presentation/pricing/BulkPriceUpdateScreen.kt`
- `presentation/pricing/BulkPriceUpdateViewModel.kt`
- `utils/CsvPriceImportUtil.kt` — parse/preview/apply logic
- `navigation/Screen.kt` — add BulkPriceUpdate route

#### 2.3 Promotions Engine
**Priority: High**

**Data Model Addition:**
- New entity: `PromotionEntity`
  - `id`, `businessId`, `name`, `type` (BOGO/MIX_MATCH/PERCENT_OFF/FIXED_OFF),
  - `ruleJson` (flexible rule structure), `applicableProductIds` (JSON list),
  - `applicableCategoryIds` (JSON list), `startDate`, `endDate`, `isActive`

**Domain Model:** `Promotion` with type enum.

**DAO:** `PromotionDao` with queries for:
- Active promotions in date range
- Promotions applicable to a product
- Promotions applicable to a cart of products

**Engine Logic (in ViewModel/UseCase):**
- Given a cart (list of InvoiceItems), check applicable promotions
- Sort by priority/specificity (product-specific > category > global)
- Compute best discount
- Return updated line totals

**Promotion Types:**
1. **BOGO** — Buy X, get Y free (same or different product)
2. **MIX_MATCH** — Buy from category, get discount on another
3. **PERCENT_OFF** — X% off total or specific products
4. **FIXED_OFF** — $X off total above threshold
5. **BUY_X_GET_Y** — Buy X units, get Y% off

**UI Implementation:**
- New screen: `PromotionManagementScreen` (admin list/create/edit)
- New screen: `PromotionCheckoutEffectScreen` (shows active promotions at checkout)
- New ViewModel: `PromotionManagementViewModel`

**Files to Create:**
- `data/local/entity/PromotionEntity.kt`
- `data/local/dao/PromotionDao.kt`
- `domain/model/Promotion.kt` (add to EnterpriseModels.kt)
- `domain/repository/PromotionRepository.kt`
- `data/repository/PromotionRepositoryImpl.kt`
- `domain/usecase/PromotionEngine.kt`
- `presentation/promotion/PromotionManagementScreen.kt`
- `presentation/promotion/PromotionManagementViewModel.kt`
- `presentation/checkout/PromotionBanner.kt` (component for checkout)
- Update database, DI, navigation

#### 2.4 Price History Log
**Priority: High**

**Data Model Addition:**
- New entity: `PriceHistoryEntity`
  - `id`, `productId` (FK), `oldPrice`, `newPrice`, `changedByUserId`, `reason`,
  - `changedAt`, `locationId` (FK)

**Separation from AuditLog:** Price-specific, fast query, indexed on productId + changedAt.

**DAO:** `PriceHistoryDao` with queries:
- `getForProduct(productId)` — chronological price changes
- `getRecent(limit)` — recent changes across all products
- `getByDateRange(start, end)` — for dispute resolution

**UI Implementation:**
- New screen: `PriceHistoryScreen` (Product detail → Pricing tab → History)
- New ViewModel: `PriceHistoryViewModel`
- Show as a timeline/list: date, user, old→new price, reason
- Filter by date range

**Integration Points:**
- When price is changed in `ProductFormScreen`, auto-log to PriceHistoryEntity
- Add "View Price History" button on ProductDetailScreen

**Files to Create:**
- `data/local/entity/PriceHistoryEntity.kt`
- `data/local/dao/PriceHistoryDao.kt`
- `domain/model/PriceHistory.kt` (add to EnterpriseModels.kt)
- `domain/repository/PriceHistoryRepository.kt`
- `data/repository/PriceHistoryRepositoryImpl.kt`
- `presentation/pricing/PriceHistoryScreen.kt`
- `presentation/pricing/PriceHistoryViewModel.kt`
- Update `ProductFormViewModel.kt` — log price changes
- Update database, DI, navigation

#### 2.5 Weighted/Scale Item Support
**Priority: High**

**Data Model Addition:**
- New entity: `ScaleItemEntity`
  - `id`, `productId` (FK, unique), `pricePerUnit`, `unit` (kg/lb), `tareWeight`, `plu`

**Domain Model:** `ScaleItem` with unit enum.

**DAO:** `ScaleItemDao` with queries:
- `getByProduct(productId)`
- `getAllActive()`
- `getByPlu(plu)`

**UI Implementation:**
- Extend `ProductFormScreen` — add "Is Scale Item" checkbox
  - When checked, shows scale item fields (pricePerUnit, unit, tareWeight, PLU)
- New screen: `ScaleItemScanScreen` — barcode/PLU input → weight input → add to cart
- New ViewModel: `ScaleItemScanViewModel`

**Checkout Integration:**
- In `NewInvoiceViewModel`, if product is a scale item:
  - Prompt for weight
  - Calculate line total: quantity (weight) × pricePerUnit
  - Use tare weight if container weighs something

**Hardware Integration (Future):**
- Bluetooth scale support via `BluetoothSocket`
- Auto-read weight when scale is connected

**Files to Create:**
- `data/local/entity/ScaleItemEntity.kt`
- `data/local/dao/ScaleItemDao.kt`
- `domain/model/ScaleItem.kt` (add to EnterpriseModels.kt)
- `domain/repository/ScaleItemRepository.kt`
- `data/repository/ScaleItemRepositoryImpl.kt`
- `presentation/scale/ScaleItemScanScreen.kt`
- `presentation/scale/ScaleItemScanViewModel.kt`
- Update `ProductFormScreen.kt` and `ProductFormViewModel.kt`
- Update `NewInvoiceScreen.kt` and `NewInvoiceViewModel.kt`
- Update database, DI, navigation

---

### Tier 2 — Competitive Parity

#### 2.6 Expiry/Batch Tracking
**Priority: Medium-High**

**Data Model Addition:**
- New entity: `ProductBatchEntity`
  - `id`, `productId` (FK), `batchNo`, `expiryDate`, `quantity`, `receivedDate`,
  - `locationId` (FK), `grnId` (FK)

**Domain Model:** `ProductBatch` with expiry status enum.

**DAO:** `ProductBatchDao` with queries:
- `getByProduct(productId)` — all batches for a product
- `getExpiringSoon(days)` — FIFO-aware expiry alerts
- `getByLocation(locationId)` — batches at a location
- `updateQuantity(batchId, newQty)`

**Stock Ledger Extension:**
- Add `EXPIRY_WRITE_OFF` as a new movement type in `StockLedgerEntity`
- When product expires, create a ledger entry and reduce batch quantity

**FIFO Allocation:**
- When selling product, allocate from oldest non-expired batch first
- Track which batch was sold in `InvoiceItemEntity` (add `batchId` field)

**Reorder/Markdown Alerts:**
- Daily check: products expiring within 7 days → create Notification
- Auto-markdown suggestion for products expiring within 3 days

**UI Implementation:**
- New screen: `ProductBatchScreen` (Product detail → Batches tab)
- New screen: `ExpiryAlertScreen` (Notifications → Expiry alerts)
- Add `batchId` selector to `NewInvoiceScreen` line items
- Add expiry date column to `InventoryListScreen`

**Files to Create:**
- `data/local/entity/ProductBatchEntity.kt`
- `data/local/dao/ProductBatchDao.kt`
- `domain/model/ProductBatch.kt` (add to EnterpriseModels.kt)
- `domain/repository/ProductBatchRepository.kt`
- `data/repository/ProductBatchRepositoryImpl.kt`
- `presentation/batch/ProductBatchScreen.kt`
- `presentation/batch/ProductBatchViewModel.kt`
- `presentation/batch/ExpiryAlertScreen.kt`
- `presentation/batch/ExpiryAlertViewModel.kt`
- Update `StockLedgerEntity.kt` — add EXPIRY_WRITE_OFF movement type
- Update `InvoiceItemEntity.kt` — add batchId field
- Update database (increment to v6), DI, navigation

#### 2.7 Loyalty/Rewards Program
**Priority: Medium**

**Data Model Additions:**
- New entity: `LoyaltyProgramEntity`
  - `id`, `businessId`, `name`, `pointsPerCurrency`, `tierRulesJson`, `isActive`
- New entity: `LoyaltyAccountEntity`
  - `id`, `customerId` (FK), `programId` (FK), `pointsBalance`, `tierId`, `createdAt`
- New entity: `LoyaltyTransactionEntity`
  - `id`, `accountId` (FK), `points`, `earnedFromAmount`, `type` (EARN/REDEEM), `referenceId`, `createdAt`

**Domain Models:** `LoyaltyProgram`, `LoyaltyAccount`, `LoyaltyTransaction`

**Engine Logic:**
- On invoice payment: calculate points earned = total × pointsPerCurrency
- On redemption: check if account has enough points, apply discount
- Tier progression: points thresholds → VIP/Silver/Gold tiers

**UI Implementation:**
- New screen: `LoyaltyProgramScreen` (Settings → Loyalty)
- New screen: `LoyaltyAccountScreen` (Customer detail → Loyalty tab)
- Integrate into `NewInvoiceScreen`:
  - Customer lookup shows loyalty points balance
  - "Redeem Points" checkbox at checkout
  - Points earned shown on invoice preview

**Files to Create:**
- `data/local/entity/LoyaltyProgramEntity.kt`
- `data/local/entity/LoyaltyAccountEntity.kt`
- `data/local/entity/LoyaltyTransactionEntity.kt`
- `data/local/dao/LoyaltyDao.kt`
- Domain models in EnterpriseModels.kt
- `domain/repository/LoyaltyRepository.kt`
- `data/repository/LoyaltyRepositoryImpl.kt`
- `domain/usecase/LoyaltyEngine.kt`
- `presentation/loyalty/LoyaltyProgramScreen.kt`
- `presentation/loyalty/LoyaltyProgramViewModel.kt`
- `presentation/loyalty/LoyaltyAccountScreen.kt`
- Update database (v7), DI, navigation, invoice screens

#### 2.8 Shelf Label / Barcode Label Printing
**Priority: Medium**

**Approach:** Use `androidx.print` + ESC/POS printer integration.

**Data Model:** No new entities — print templates stored as JSON in AppSettings.

**Template Engine:**
- Label template: `{productName}\n{SKU}\nPrice: {salePrice}\n{PKR symbol}`
- Support A4, 3-up, 2-up page layouts
- Barcode: Code 128 from SKU using `com.google.mlkit:barcode-scanning` (already in deps)

**UI Implementation:**
- New screen: `LabelPrintingScreen`
- New ViewModel: `LabelPrintingViewModel`
- Filter products by category, supplier, or individual selection
- Generate printable PDF with labels
- Print button → `Android PrintManager`

**Files to Create:**
- `presentation/reporting/LabelPrintingScreen.kt`
- `presentation/reporting/LabelPrintingViewModel.kt`
- `utils/LabelTemplateEngine.kt`
- `utils/BarcodeGenerator.kt` (generate Code 128 bitmaps)
- Update navigation

#### 2.9 Customer-Facing Display for Checkout
**Priority: Medium**

**Approach:** Mirror checkout screen to a secondary display via `MediaRouter` or Presentation API.

**Implementation:**
- New `CustomerDisplayPresentation` extending `Presentation`
- Cast current invoice items, total, and "Thank You" message
- Update in real-time as cashier adds items
- Show QR code for digital receipts

**UI Implementation:**
- New component: `CustomerDisplayView.kt`
- New ViewModel: `CustomerDisplayViewModel`
- Integrate into `NewInvoiceScreen` — toggle "Customer Display" button
- Handle presentation lifecycle in `MainActivity`

**Files to Create:**
- `presentation/checkout/CustomerDisplayView.kt`
- `presentation/checkout/CustomerDisplayPresentation.kt`
- Update `MainActivity.kt` — manage presentation lifecycle
- Update `NewInvoiceScreen.kt` — integrate customer display

---

### Tier 3 — Differentiation (Later)

#### 2.10 Cost-Linked Dynamic Repricing
**Priority: Low (v2)**

**Logic:**
- When cost price changes, suggest new sale price based on configurable margin
- Margin floor: don't drop below X% margin
- Historical trend analysis for seasonal adjustments

**Data Model:**
- New entity: `RepricingRuleEntity`
  - `id`, `productId` (FK), `minMarginPercent`, `targetMarginPercent`, `maxPriceChangePercent`, `isActive`

**UI Implementation:**
- New screen: `RepricingRulesScreen`
- Inline suggestion in `ProductFormScreen` when cost changed
- "Apply Suggested Price" button with confirmation

#### 2.11 Self-Checkout / Kiosk Mode
**Priority: Low (v2)**

**Approach:** Lock down device into kiosk mode using `DevicePolicyManager`.

**UI Implementation:**
- New screen: `SelfCheckoutScreen` — full-screen, simplified UI
- Barcode scanning for item entry
- Payment selection (Cash/Card)
- Bagging confirmation
- Receipt option (print/email/QR)

#### 2.12 E-commerce / Online Ordering Sync
**Priority: Low (v2)**

**Approach:** Simple REST API integration with Shopify/WooCommerce.

**Data Model:**
- New entity: `OnlineOrderEntity` — sync orders from web store
- New entity: `IntegrationConfigEntity` — API keys, store URLs

#### 2.13 Demand Forecasting / Auto-Reorder
**Priority: Medium (v2)**

**Logic:**
- Use existing `SalesForecastEntity` for predictions
- When stock falls below (forecast × days of supply), suggest reorder
- Send notification to manager

**Data Model:**
- Extend `ProductEntity` — add `reorderPoint`, `leadTimeDays`, `economicOrderQty`
- New entity: `ReorderSuggestionEntity` — calculated suggestions

---

## 3. Phased Implementation Plan

### Phase 1: Tiered Pricing Engine (Week 1-2)
- [ ] `PriceTierEntity` + DAO + Repository + Domain Model
- [ ] `PriceTierScreen` + ViewModel
- [ ] Integrate tiered pricing into `NewInvoiceViewModel`
- [ ] Add "Manage Tiers" action on `ProductFormScreen`
- [ ] Update database to v6

### Phase 2: Price History + Bulk Update (Week 2-3)
- [ ] `PriceHistoryEntity` + DAO + Repository + Domain Model
- [ ] `PriceHistoryScreen` + ViewModel
- [ ] Auto-log price changes in `ProductFormViewModel`
- [ ] `BulkPriceUpdateScreen` + ViewModel + CSV utilities
- [ ] Update database to v7

### Phase 3: Promotions Engine (Week 3-4)
- [ ] `PromotionEntity` + DAO + Repository + Domain Model
- [ ] `PromotionEngine` use case
- [ ] `PromotionManagementScreen` + ViewModel
- [ ] Promotion banner component for checkout
- [ ] Integrate promotions into `NewInvoiceViewModel`
- [ ] Update database to v8

### Phase 4: Weighted Items (Week 4-5)
- [ ] `ScaleItemEntity` + DAO + Repository + Domain Model
- [ ] Extend `ProductFormScreen` for scale item fields
- [ ] `ScaleItemScanScreen` + ViewModel
- [ ] Integrate weight input into invoice line items
- [ ] Update database to v9

### Phase 5: Expiry/Batch Tracking (Week 5-6)
- [ ] `ProductBatchEntity` + DAO + Repository + Domain Model
- [ ] `ProductBatchScreen` + ViewModel
- [ ] `ExpiryAlertScreen` + ViewModel
- [ ] Add EXPIRY_WRITE_OFF to StockLedgerEntity
- [ ] FIFO allocation in invoice flow
- [ ] Update database to v10

### Phase 6: Loyalty Program (Week 6-7)
- [ ] `LoyaltyProgramEntity` + DAO + Repository + Domain Models
- [ ] `LoyaltyEngine` use case
- [ ] `LoyaltyProgramScreen` + ViewModel
- [ ] `LoyaltyAccountScreen` + ViewModel
- [ ] Integrate loyalty into invoice checkout
- [ ] Update database to v11

### Phase 7: Label Printing + Customer Display (Week 8)
- [ ] `LabelPrintingScreen` + ViewModel + utilities
- [ ] `CustomerDisplayView` + Presentation
- [ ] Integrate into MainActivity

### Phase 8: Polish + Validation (Week 9)
- [ ] All screens styled with Material 3 dynamic color
- [ ] App shortcuts for new features
- [ ] Haptic feedback for all new actions
- [ ] Bilingual EN/UR string resources
- [ ] Testing on phone/tablet/foldable

---

## 4. Data Model Changes Summary

### New Entities (9 total)
| Entity | Purpose | FK Relationships |
|--------|---------|-----------------|
| `PriceTierEntity` | Quantity-break pricing | → ProductEntity |
| `PriceHistoryEntity` | Price change log | → ProductEntity, → UserEntity, → LocationEntity |
| `PromotionEntity` | BOGO/mix-match/percent discounts | → BusinessEntity (no direct FK to items) |
| `ScaleItemEntity` | Price-per-weight config | → ProductEntity (unique) |
| `ProductBatchEntity` | Batch + expiry tracking | → ProductEntity, → LocationEntity, → GRNEntity |
| `LoyaltyProgramEntity` | Loyalty point rules | → BusinessEntity |
| `LoyaltyAccountEntity` | Customer point balance | → CustomerEntity, → LoyaltyProgramEntity |
| `LoyaltyTransactionEntity` | Points earned/redeemed | → LoyaltyAccountEntity |
| `ReorderSuggestionEntity` | Auto-reorder alerts | → ProductEntity |

### Modified Entities
| Entity | Change |
|--------|--------|
| `ProductEntity` | Add `priceTierGroupId` (FK for bulk management) |
| `InvoiceItemEntity` | Add `batchId` (FK to ProductBatchEntity), `isScaleItem`, `weight` |
| `StockLedgerEntity` | Add `EXPIRY_WRITE_OFF` as valid movementType |
| `AimrPosDatabase` | Increment to version 10, add 9 new DAOs |

---

## 5. Integration Points

### 5.1 Invoice Flow (NewInvoiceScreen) — Most Impact
- Tiered pricing lookup on product selection
- Promotion eligibility check
- Scale item weight input
- Batch selection (FIFO)
- Loyalty points calculation
- Points redemption option

### 5.2 Product Management Flow (ProductFormScreen)
- Price tier management sub-screen
- Price history view
- Scale item configuration toggle
- Batch/expiry management toggle

### 5.3 Dashboard (DashboardScreen)
- Add "Pricing" widget category
- Add "Promotions" widget (active count, upcoming)
- Add "Expiring Soon" widget
- Add "Loyalty" widget (top customers)

### 5.4 Notifications (NotificationEntity)
- Price change alerts (admin)
- Batch expiry alerts (7 days, 3 days, expired)
- Low stock alerts (existing)
- Promotion start/end notifications

---

## 6. File Creation Checklist

### New Entity Files (9)
```
data/local/entity/PriceTierEntity.kt
data/local/entity/PriceHistoryEntity.kt
data/local/entity/PromotionEntity.kt
data/local/entity/ScaleItemEntity.kt
data/local/entity/ProductBatchEntity.kt
data/local/entity/LoyaltyProgramEntity.kt
data/local/entity/LoyaltyAccountEntity.kt
data/local/entity/LoyaltyTransactionEntity.kt
data/local/entity/ReorderSuggestionEntity.kt
```

### New DAO Files (9)
```
data/local/dao/PriceTierDao.kt
data/local/dao/PriceHistoryDao.kt
data/local/dao/PromotionDao.kt
data/local/dao/ScaleItemDao.kt
data/local/dao/ProductBatchDao.kt
data/local/dao/LoyaltyProgramDao.kt
data/local/dao/LoyaltyAccountDao.kt
data/local/dao/LoyaltyTransactionDao.kt
data/local/dao/ReorderSuggestionDao.kt
```

### New Presentation Files (12)
```
presentation/pricing/PriceTierScreen.kt
presentation/pricing/PriceTierViewModel.kt
presentation/pricing/PriceHistoryScreen.kt
presentation/pricing/PriceHistoryViewModel.kt
presentation/pricing/BulkPriceUpdateScreen.kt
presentation/pricing/BulkPriceUpdateViewModel.kt
presentation/promotion/PromotionManagementScreen.kt
presentation/promotion/PromotionManagementViewModel.kt
presentation/scale/ScaleItemScanScreen.kt
presentation/scale/ScaleItemScanViewModel.kt
presentation/batch/ProductBatchScreen.kt
presentation/batch/ProductBatchViewModel.kt
presentation/batch/ExpiryAlertScreen.kt
presentation/batch/ExpiryAlertViewModel.kt
presentation/loyalty/LoyaltyProgramScreen.kt
presentation/loyalty/LoyaltyProgramViewModel.kt
presentation/loyalty/LoyaltyAccountScreen.kt
presentation/loyalty/LoyaltyAccountViewModel.kt
presentation/reporting/LabelPrintingScreen.kt
presentation/reporting/LabelPrintingViewModel.kt
presentation/checkout/CustomerDisplayView.kt
presentation/checkout/CustomerDisplayPresentation.kt
```

### New Domain/Repository Files
```
domain/model/PriceTier.kt (add to EnterpriseModels.kt)
domain/model/PriceHistory.kt (add to EnterpriseModels.kt)
domain/model/Promotion.kt (add to EnterpriseModels.kt)
domain/model/ScaleItem.kt (add to EnterpriseModels.kt)
domain/model/ProductBatch.kt (add to EnterpriseModels.kt)
domain/model/LoyaltyProgram.kt (add to EnterpriseModels.kt)
domain/model/LoyaltyAccount.kt (add to EnterpriseModels.kt)
domain/model/LoyaltyTransaction.kt (add to EnterpriseModels.kt)
domain/model/ReorderSuggestion.kt (add to EnterpriseModels.kt)
domain/repository/PriceTierRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/PriceHistoryRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/PromotionRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/ScaleItemRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/ProductBatchRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/LoyaltyRepository.kt (add to RepositoryInterfaces.kt)
domain/repository/ReorderSuggestionRepository.kt (add to RepositoryInterfaces.kt)
domain/usecase/PromotionEngine.kt
domain/usecase/LoyaltyEngine.kt
domain/usecase/ForecastEngine.kt (extend existing AnalyticsEngine)
utils/CsvPriceImportUtil.kt
utils/LabelTemplateEngine.kt
utils/BarcodeGenerator.kt
utils/CustomerDisplayUtils.kt
```

### Modified Existing Files
```
data/local/AimrPosDatabase.kt — add entities + DAOs, v10
di/AppModule.kt — add DAO providers
navigation/Screen.kt — add routes
navigation/NavGraph.kt — add destinations
presentation/invoice/NewInvoiceViewModel.kt — tiered pricing, promotions, scale items, batch, loyalty
presentation/invoice/NewInvoiceScreen.kt — integrate all pricing features
presentation/inventory/ProductFormScreen.kt — add pricing/batch/scale tabs
presentation/inventory/ProductFormViewModel.kt — auto-log price history
presentation/inventory/InventoryListScreen.kt — add batch/expiry columns
presentation/dashboard/DashboardScreen.kt — add pricing/widgets
presentation/dashboard/DashboardViewModel.kt — add pricing data sources
MainActivity.kt — customer display presentation lifecycle
ui/shortcuts/AppShortcuts.kt — add bulk price update shortcut
```

---

## 7. UI/UX Design Guidelines

### 7.1 Pricing Screens
- Use accordion-style sections: Basic Info → Pricing → Batches → History
- Price tiers in a table format with minQty, price, customer type
- Bulk update with file picker → CSV preview → column mapping → confirm

### 7.2 Promotion Builder
- Wizard-style: Type → Scope → Rules → Schedule → Review
- Visual rule builder with drag-and-drop conditions
- Preview discount on sample cart

### 7.3 Scale Item Integration
- In invoice line item: if product is a scale item, show weight input
- Numeric keypad with decimal → weight in kg/lb
- Auto-calculate: weight × pricePerUnit = line total

### 7.4 Batch Selection
- In invoice line item: dropdown of available batches (FIFO sorted)
- Show expiry date, available quantity, batch number
- Color-code: green (fresh), yellow (expiring soon), red (expired)

### 7.5 Loyalty at Checkout
- Customer lookup shows name, phone, loyalty points balance
- "Redeem Points" toggle → apply discount to total
- Points earned shown before completing sale

### 7.6 App Shortcuts
- Long-press app icon: New Invoice, Scan Product, Add Product, Manage Pricing
- Quick tile for "Customer Display Mode"

### 7.7 Dynamic Color & Haptics
- All new screens use `dynamicColor` when available
- Haptics: light click for price changes, success for saving, error for validation failures
- Shimmer loading for all data-fetching scenarios

---

## 8. Validation Plan

| Feature | Validation Method |
|---------|-------------------|
| Tiered Pricing | Test with 5+ tier configurations, verify correct price at different quantities |
| Bulk Price Update | Import 500-row CSV, verify preview accuracy, confirm no data loss |
| Promotions Engine | Test BOGO, MIX_MATCH, PERCENT_OFF with overlapping date ranges |
| Scale Items | Verify weight calculation accuracy ±0.01, tare weight handling |
| Expiry/Batch | FIFO allocation correctness, expiry alerts fire at correct intervals |
| Loyalty | Points calculation accuracy, tier progression after threshold |
| Label Printing | Test on 2 label printers (Bluetooth + WiFi), verify barcode scannable |
| Customer Display | Verify real-time sync, test rotation/sizing on different devices |

---

## 9. Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| Database migration complexity (v5→v10) | Use Room `Migration` object, test migration thoroughly, provide fallback recreate |
| Tiered pricing performance on large catalogs | Index `PriceTierEntity.productId` + `minQty`, cache resolved tiers per session |
| Promotion overlap conflicts | Priority ordering + explicit conflict resolution UI |
| Scale item hardware compatibility | Abstract scale reader, support Bluetooth HID + serial protocols |
| Batch FIFO allocation complexity | Pre-calculate allocations in ViewModel, validate before saving |
| Loyalty fraud (point hoarding) | Expiration policy, max redemption per transaction, audit trail |
| Label printing printer fragmentation | Use platform PrintManager + generic ESC/POS fallback |
| Customer display external display detection | Use MediaRouter callback, handle no-display gracefully |

---

## 10. Key Differentiators vs. Competitors

1. **Tiered + Wholesale Pricing** — Not available in Loyverse/Vyapar/PosBytz free tier; requires paid add-on in LOC SMS
2. **Urdu Bilingual RTL** — Unique to AIMR in the Pakistani market; competitors only support Hindi or no local language
3. **Integrated Workflow** — PO→GRN→Batch→Expiry chain is more complete than any SMB Android POS
4. **On-device Intelligence** — All pricing/promotion logic runs offline, no cloud dependency
5. **Visual-First Pricing** — Dynamic color, haptics, shimmer for pricing screens; competitors use static gray UIs
6. **Foldable-Native** — Dashboard spans both screens on Surface Duo/Chromebook Fold; competitors don't support

---

## 11. Migration Path

### Database Schema Migration (v5 → v10)
```
v5 → v6: Add PriceTierEntity, PriceHistoryEntity
v6 → v7: Add PromotionEntity
v7 → v8: Add ScaleItemEntity
v8 → v9: Add ProductBatchEntity, add EXPIRY_WRITE_OFF to StockLedger, add batchId to InvoiceItem
v9 → v10: Add LoyaltyProgramEntity, LoyaltyAccountEntity, LoyaltyTransactionEntity
```

### Backward Compatibility
- All new features are additive — existing workflows unaffected
- Null-safe everywhere: `batchId` is nullable in InvoiceItemEntity for backward compatibility
- Legacy products without price tiers fall back to `salePrice`
- Existing invoices unaffected by new entities

---

## 12. Next Steps
1. Finalize this plan — choose features for Phase 1 implementation
2. Set up Room database migration infrastructure
3. Create entities + DAOs for Tier 1 features first
4. Implement tiered pricing engine
5. Build UI screens in order of priority (Pricing → Promotions → Scale Items → Batch/Expiry → Loyalty)

---

*Plan version: 1.0 | Created: 2026-08-05 | Based on competitive analysis: AIMR-POS-competitive-analysis.md*
