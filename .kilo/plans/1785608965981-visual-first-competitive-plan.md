# AIMR POS — Advanced Competitive Plan (Visual-First)
> Implementation-ready plan for next-generation enterprise POS with visual-first UI innovations.
> All features use native Android libraries or free services — no paid AI APIs, no API keys required.

---

## 1. Current State Summary

| Layer | Status |
|-------|--------|
| DB Schema | v5, 30 entities |
| Screens | 22+ screens with ViewModels |
| Auth | PIN + OAuth (Google/GitHub) + Biometric |
| Security | Root/debugger/emulator detection, EncryptedSharedPreferences, network security config |
| Scanning | CamScanner-like folders, 3 OCR modes (SIMPLE/ENHANCED/SUPER_MAGIC), CSV/Excel export |
| Enterprise | RBAC, workflow automation, multi-currency, analytics snapshots, notifications |
| Sync | WorkManager delta sync with constraints |
| UI | Material 3 dark theme, bilingual EN/UR |

---

## 2. Competitive Gap Analysis (Visual-First Focus)

| Competitor Feature | Current Status | Gap |
|--------------------|----------------|-----|
| Offline-first POS | ✅ | — |
| Barcode/QR scanning | ✅ | — |
| OCR scanning | ✅ | — |
| Multi-location | ✅ | — |
| Material 3 dark theme | ✅ | — |
| **Dynamic color / Monet theming** | ❌ | Missing |
| **Foldable / dual-screen support** | ❌ | Missing |
| **Customizable dashboard widgets** | ❌ | Missing |
| **Micro-interactions & haptics** | ❌ | Missing |
| **AR-style product cards** | ❌ | Missing |
| **Gesture-based navigation** | ❌ | Missing |
| **Adaptive layouts (phone/tablet/foldable)** | ❌ | Missing |
| **Shared element transitions** | ❌ | Missing |
| **Haptic feedback patterns** | ❌ | Missing |
| **Accessibility-first UI** | Partial | Needs enhancement |
| **Quick tiles / app shortcuts** | ❌ | Missing |
| **Split-screen / multi-window** | ❌ | Missing |
| **AI-powered predictions** | ❌ | Use on-device ML only |
| **Real-time collaboration UI** | ❌ | Missing |
| **Chatbot assistant UI** | ❌ | Use local NLP only |
| **Plugin marketplace UI** | ❌ | Missing |

---

## 3. Visual-First Feature Plan

### 3.1 Dynamic & Adaptive UI

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Material 3 Dynamic Color** | Extract wallpaper colors, apply Monet theming | Competitors use static themes only | `androidx.compose.material3:material3-dynamic-color` |
| **Adaptive Layouts** | Phone/tablet/foldable-aware UI with WindowSizeClass | Competitors ignore foldables | `androidx.window:window` |
| **Foldable Dual-Screen** | Span content across both screens, drag-and-drop | Industry-first for POS | `androidx.window:window` |
| **Split-Screen Support** | Native multi-window mode with state preservation | Competitors disable split-screen | Android manifest + `onMultiWindowModeChanged` |

### 3.2 Micro-Interactions & Motion

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Haptic Feedback Patterns** | Custom haptics for scan, success, error, navigation | Competitors use default haptics only | `android.os.Vibrator` + `HapticFeedbackConstants` |
| **Shared Element Transitions** | Smooth hero transitions between screens | Competitors use basic navigation | Compose `AnimatedContent` + `updateTransition` |
| **Physics-based Animations** | Spring animations for cards, buttons, lists | Competitors use linear/ease animations | Compose `spring()` + `AnimationSpec` |
| **Gesture Navigation** | Swipe between screens, pinch-to-zoom, long-press actions | Competitors rely on buttons only | Compose `detectTapGestures`, `detectDragGestures` |
| **Ripple Effects** | Material 3 ripple with custom colors | Standard Material 3 | Built-in Compose |

### 3.3 Advanced Dashboard & Widgets

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Customizable Dashboard** | Drag-and-drop widgets, resize, hide/show | Competitors have fixed dashboards | Compose `DraggableItem` + `LazyVerticalGrid` |
| **Widget Types** | Sales chart, low stock alerts, quick actions, credit summary | 10+ widget types | Vico charts + custom Composables |
| **Widget Persistence** | Save/load widget layout from Room | Competitors don’t persist layout | Room entity `DashboardWidgetEntity` |
| **Quick Actions** | Edge panel, app shortcuts, quick tiles | Competitors have static shortcuts | Android `ShortcutManager`, `TileService` |

### 3.4 Visual Scanning Enhancements

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **AR-style Product Cards** | 3D card flip animation, shimmer loading | Competitors show static images | Compose `rotateY` + `shimmer` |
| **Batch Scanning UI** | Grid view of scanned pages, multi-select | Competitors show one page at a time | Compose `LazyVerticalGrid` + `selection` |
| **Scan Animations** | Scanning laser, corner brackets, auto-crop preview | CamScanner-like but more polished | Compose `Canvas` + `animateFloat` |
| **Document Preview** | Full-screen preview with zoom/pan | Standard but polished | Compose `ZoomableImage` |

### 3.5 On-Device Intelligence (No Cloud AI)

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Demand Forecasting** | Time series prediction using exponential smoothing | Competitors use simple averages | Pure Kotlin math, no ML Kit needed |
| **Smart Recommendations** | Collaborative filtering based on purchase history | Competitors don’t have this | Pure Kotlin, cosine similarity |
| **Anomaly Detection** | Statistical outlier detection for sales/stock | Competitors use manual thresholds | Z-score + IQR methods |
| **Auto-categorization** | Keyword + pattern matching for products | Competitors require manual categorization | Pure Kotlin regex + TF-IDF |
| **NLP Query Parser** | "Show sales last week" → SQL query | Competitors don’t have NLP | Pure Kotlin tokenizer + rule engine |

### 3.6 Advanced Reporting UI

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Drill-down Reports** | Tap chart bar → see details → tap again → transaction | Competitors show static charts | Vico + Compose navigation |
| **Heatmap Calendar** | Sales heatmap by day/hour | Competitors show bar charts only | Compose `Canvas` + color gradient |
| **RFM Segmentation** | Visual customer segments (VIP, Regular, At-risk) | Competitors don’t segment customers | Pure Kotlin RFM scoring |
| **Profit Margin Treemap** | Visual profit distribution by product/category | Competitors use pie charts | Compose `Canvas` + custom layout |

### 3.7 Collaboration UI

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Real-time Activity Feed** | Live feed of team actions with animations | Competitors show static logs | Supabase Realtime + Compose animations |
| **Role-based Dashboards** | Different widgets per role (Admin, Cashier, Manager) | Competitors have one dashboard for all | Role-aware Composable rendering |
| **Comments & Mentions** | Inline comments on invoices/products | Competitors don’t have this | Supabase Realtime + @mention parsing |

### 3.8 Plugin Marketplace UI

| Feature | Description | Innovation | Native/Free Library |
|---------|-------------|------------|---------------------|
| **Plugin Store** | Browse, install, uninstall plugins | Competitors don’t have extensibility | Compose `LazyVerticalGrid` + Card |
| **Plugin Categories** | Scanning, Analytics, Integrations, Themes | Organized discovery | Category chips + search |
| **Plugin Details** | Screenshots, description, permissions, reviews | Trust + transparency | Static data + Room |

---

## 4. Native/Free Library Stack (No Paid APIs)

| Category | Library | Version | Purpose |
|----------|---------|---------|---------|
| UI | Jetpack Compose | BOM 2024.02.00 | All UI |
| Dynamic Color | Material 3 Dynamic Color | 1.1.0 | Monet theming |
| Window Management | AndroidX Window | 1.2.0 | Foldables/adaptive |
| Charts | Vico | 2.1.0 | Charts + heatmaps |
| Scanning | ML Kit Barcode + OCR | 17.2.0 / 16.0.0 | Barcode + text recognition |
| Camera | CameraX | 1.3.0 | Document scanning |
| Database | Room | 2.6.1 | Local storage |
| DI | Hilt | 2.50 | Dependency injection |
| Sync | WorkManager | 2.9.0 | Background sync |
| Realtime | Supabase Realtime | 2.5.0 | Real-time collaboration |
| Auth | Google Sign-In | 21.2.0 | OAuth |
| Security | Security Crypto | 1.1.0-alpha06 | Encrypted storage |
| Security | RootBeer | 0.1.0 | Root detection |
| Biometric | Biometric | 1.1.0 | Fingerprint/face |
| Coroutines | kotlinx-coroutines | 1.7.3 | Async |
| Image Loading | Coil | 2.5.0 | Image loading |
| Pagination | Paging 3 | 3.2.1 | Large lists |

**No paid APIs, no API keys, no cloud AI services.**

---

## 5. Implementation Phases (Visual-First)

### Phase 1: Dynamic & Adaptive UI (Weeks 1-3)
- Material 3 dynamic color extraction from wallpaper
- Adaptive layouts with WindowSizeClass
- Foldable dual-screen support
- Split-screen mode with state preservation
- Quick tiles and app shortcuts

### Phase 2: Micro-Interactions & Motion (Weeks 4-6)
- Haptic feedback patterns for all actions
- Shared element transitions between screens
- Physics-based spring animations
- Gesture navigation (swipe, pinch, long-press)
- Shimmer loading effects

### Phase 3: Advanced Dashboard & Widgets (Weeks 7-9)
- Customizable dashboard with drag-and-drop
- 10+ widget types (charts, alerts, actions)
- Widget persistence in Room
- Role-based widget filtering

### Phase 4: Visual Scanning Enhancements (Weeks 10-12)
- AR-style product cards with 3D flip
- Batch scanning grid with multi-select
- Scan laser animation + auto-crop preview
- Full-screen document preview with zoom

### Phase 5: On-Device Intelligence UI (Weeks 13-15)
- Demand forecasting chart widget
- Smart recommendations carousel
- Anomaly detection alerts with color coding
- Auto-categorization with confidence badges
- NLP query bar with autocomplete

### Phase 6: Advanced Reporting UI (Weeks 16-18)
- Drill-down reports with tap-to-detail
- Sales heatmap calendar
- RFM customer segmentation visualization
- Profit margin treemap

### Phase 7: Collaboration UI (Weeks 19-21)
- Real-time activity feed with animations
- Role-based dashboard rendering
- Comments & mentions UI
- Team presence indicators

### Phase 8: Plugin Marketplace UI (Weeks 22-24)
- Plugin store with grid layout
- Category filtering and search
- Plugin detail pages
- Install/uninstall with progress

---

## 6. Key Differentiators from Competitors

1. **Visual-First Commerce** — Dynamic color, adaptive layouts, micro-interactions, haptics
2. **Foldable-Native POS** — First POS app with true dual-screen support
3. **On-Device AI** — All intelligence runs locally, no cloud AI APIs needed
4. **Customizable Everything** — Dashboard widgets, themes, layouts
5. **Gesture-Driven** — Full gesture navigation, swipe-to-action
6. **Accessibility-First** — Haptic feedback, TalkBack, high contrast, voice guidance
7. **Plugin Ecosystem** — Extensible marketplace for third-party extensions
8. **Real-time Collaboration** — Team feed, comments, presence

---

## 7. Technical Risks & Mitigations

| Risk | Mitigation |
|------|------------|
| Dynamic color API level | Use `DynamicColors.applyToActivitiesIfAvailable` with fallback |
| Foldable detection complexity | Use `WindowSizeClass` + `FoldingFeature` with phone fallback |
| Animation performance | Use `animate*AsState`, avoid heavy recompositions, profile with Layout Inspector |
| On-device AI accuracy | Use rule-based fallbacks, confidence thresholds, human-in-loop |
| Plugin security | Sandboxing, permission system, code review, ProGuard |
| Battery drain | Batch animations, respect Doze, optimize recompositions |

---

## 8. Validation Plan

| Feature | Validation |
|---------|-----------|
| Dynamic Color | Test on Android 12+ devices, verify fallback on older versions |
| Adaptive Layouts | Test on phone, tablet, foldable emulators |
| Micro-interactions | Profile with Layout Inspector, target 60fps |
| Dashboard Widgets | User testing with 10+ users, measure customization rate |
| On-Device AI | Compare predictions against actual data for 30 days |
| Collaboration | Test with 10 concurrent users, measure sync latency |

---

## 9. Open Decisions

1. **Dynamic color fallback strategy:** Should we bundle custom themes for older Android versions, or use a single static theme with optional dynamic color?
   - **Recommended:** Bundle 3 static themes (Light/Dark/HighContrast) + dynamic color on Android 12+

2. **Widget persistence format:** Should we use Room with JSON column, or a separate table per widget type?
   - **Recommended:** Room with JSON column for flexibility

3. **Foldable span strategy:** Should content span across screens only for specific screens (dashboard, reports), or globally?
   - **Recommended:** Start with dashboard + reports, expand to other screens later

---

## 10. Next Steps

1. **Finalize this plan** — Save as `.kilo/plans/1785608965981-visual-first-competitive-plan.md`
2. **Phase 1 kickoff** — Start with dynamic color + adaptive layouts
3. **Create design system** — Define haptic patterns, animation curves, color tokens
4. **Set up device testing** — Phone, tablet, foldable emulators

---

*Plan version: 1.0 | Created: 2026-08-05 | Focus: Visual-First UI | Constraint: Native/Free only, no paid APIs*