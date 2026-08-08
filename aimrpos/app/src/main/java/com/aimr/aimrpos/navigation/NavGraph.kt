package com.aimr.aimrpos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aimr.aimrpos.presentation.login.LoginScreen
import com.aimr.aimrpos.presentation.dashboard.DashboardScreen
import com.aimr.aimrpos.presentation.inventory.InventoryListScreen
import com.aimr.aimrpos.presentation.inventory.ProductFormScreen
import com.aimr.aimrpos.presentation.invoice.NewInvoiceScreen
import com.aimr.aimrpos.presentation.invoice.InvoicePreviewScreen
import com.aimr.aimrpos.presentation.customer.CustomerLedgerScreen
import com.aimr.aimrpos.presentation.reports.ReportsScreen
import com.aimr.aimrpos.presentation.settings.SettingsScreen
import com.aimr.aimrpos.presentation.scan.DocumentScannerScreen
import com.aimr.aimrpos.presentation.scan.DocumentReviewScreen
import com.aimr.aimrpos.presentation.purchase.PurchaseOrderScreen
import com.aimr.aimrpos.presentation.grn.GRNScreen
import com.aimr.aimrpos.presentation.returninvoice.ReturnInvoiceScreen
import com.aimr.aimrpos.presentation.approval.ApprovalQueueScreen
import com.aimr.aimrpos.presentation.audit.AuditLogScreen
import com.aimr.aimrpos.presentation.vault.DocumentVaultScreen
import com.aimr.aimrpos.presentation.warehouse.WarehouseScreen
import com.aimr.aimrpos.presentation.transfer.StockTransferScreen
import com.aimr.aimrpos.presentation.aging.AgingReportScreen
import com.aimr.aimrpos.presentation.qr.QrScannerScreen
import com.aimr.aimrpos.presentation.scanhub.ScanHubScreen
import com.aimr.aimrpos.presentation.supplierledger.SupplierLedgerScreen
import com.aimr.aimrpos.presentation.pricing.PriceTierScreen
import com.aimr.aimrpos.presentation.pricing.PriceHistoryScreen
import com.aimr.aimrpos.presentation.pricing.PromotionScreen
import com.aimr.aimrpos.presentation.scale.ScaleItemScreen
import com.aimr.aimrpos.presentation.batch.ProductBatchScreen
import com.aimr.aimrpos.presentation.loyalty.LoyaltyScreen
import com.aimr.aimrpos.ui.bottombar.CinematicBottomBar
import com.aimr.aimrpos.ui.bottombar.BottomNavItem
import androidx.compose.ui.graphics.vector.ImageVector

val BottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Dashboard.route,
        label = "Home",
        icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_compass),
        activeIcon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_compass)
    ),
    BottomNavItem(
        route = Screen.InventoryList.route,
        label = "Stock",
        icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_agenda),
        activeIcon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_agenda)
    ),
    BottomNavItem(
        route = Screen.NewInvoice.route,
        label = "Invoice",
        icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_send),
        activeIcon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_send)
    ),
    BottomNavItem(
        route = Screen.CustomerLedger.route,
        label = "Customers",
        icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_manage),
        activeIcon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_manage)
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        label = "Settings",
        icon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_preferences),
        activeIcon = ImageVector.vectorResource(id = android.R.drawable.ic_menu_preferences)
    )
)

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }

        // Main screens with bottom bar
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.InventoryList.route) { InventoryListScreen(navController) }
        composable(Screen.ProductForm.route) { ProductFormScreen(navController) }
        composable(Screen.NewInvoice.route) { NewInvoiceScreen(navController) }
        composable(
            route = Screen.InvoicePreview.route,
            arguments = listOf(navArgument("invoiceId") { type = NavType.StringType })
        ) { backStackEntry ->
            val invoiceId = backStackEntry.arguments?.getString("invoiceId") ?: ""
            InvoicePreviewScreen(navController, invoiceId)
        }
        composable(Screen.CustomerLedger.route) { CustomerLedgerScreen(navController) }
        composable(Screen.Reports.route) { ReportsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }

        // Enterprise screens
        composable(Screen.DocumentScanner.route) { DocumentScannerScreen(navController) }
        composable(
            route = Screen.DocumentReview.route,
            arguments = listOf(navArgument("docId") { type = NavType.StringType })
        ) { backStackEntry ->
            val docId = backStackEntry.arguments?.getString("docId") ?: ""
            DocumentReviewScreen(navController, docId = docId)
        }
        composable(Screen.PurchaseOrder.route) { PurchaseOrderScreen(navController) }
        composable(Screen.GRN.route) { GRNScreen(navController) }
        composable(Screen.ReturnInvoice.route) { ReturnInvoiceScreen(navController) }
        composable(Screen.ApprovalQueue.route) { ApprovalQueueScreen(navController) }
        composable(Screen.AuditLog.route) { AuditLogScreen(navController) }
        composable(Screen.DocumentVault.route) { DocumentVaultScreen(navController) }
        composable(Screen.Warehouse.route) { WarehouseScreen(navController) }
        composable(Screen.StockTransfer.route) { StockTransferScreen(navController) }
        composable(Screen.AgingReport.route) { AgingReportScreen(navController) }
        composable(Screen.QrScanner.route) { QrScannerScreen(navController) }
        composable(Screen.ScanHub.route) { ScanHubScreen(navController) }
        composable(Screen.SupplierLedger.route) { SupplierLedgerScreen(navController) }
        composable(Screen.Promotions.route) { PromotionScreen(navController) }
        composable(Screen.ScaleItems.route) { ScaleItemScreen(navController) }
        composable(Screen.Loyalty.route) { LoyaltyScreen(navController) }
        composable(
            route = Screen.PriceTiers.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            PriceTierScreen(navController, productId)
        }
        composable(
            route = Screen.PriceHistory.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            PriceHistoryScreen(navController, productId)
        }
        composable(
            route = Screen.ProductBatches.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductBatchScreen(navController, productId)
        }
    }
}

@Composable
fun MainNavGraphWithBottomBar() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        // Main screens with bottom bar
        composable(Screen.Dashboard.route) {
            Column {
                DashboardScreen(navController)
                CinematicBottomBar(navController, BottomNavItems)
            }
        }
        composable(Screen.InventoryList.route) {
            Column {
                InventoryListScreen(navController)
                CinematicBottomBar(navController, BottomNavItems)
            }
        }
        composable(Screen.NewInvoice.route) {
            Column {
                NewInvoiceScreen(navController)
                CinematicBottomBar(navController, BottomNavItems)
            }
        }
        composable(Screen.CustomerLedger.route) {
            Column {
                CustomerLedgerScreen(navController)
                CinematicBottomBar(navController, BottomNavItems)
            }
        }
        composable(Screen.Settings.route) {
            Column {
                SettingsScreen(navController)
                CinematicBottomBar(navController, BottomNavItems)
            }
        }
        // Non-main screens without bottom bar
        composable(Screen.ScanHub.route) { ScanHubScreen(navController) }
        composable(Screen.SupplierLedger.route) { SupplierLedgerScreen(navController) }
        composable(Screen.Promotions.route) { PromotionScreen(navController) }
        composable(Screen.ScaleItems.route) { ScaleItemScreen(navController) }
        composable(Screen.Loyalty.route) { LoyaltyScreen(navController) }
        composable(
            route = Screen.PriceTiers.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            PriceTierScreen(navController, productId)
        }
        composable(
            route = Screen.PriceHistory.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            PriceHistoryScreen(navController, productId)
        }
        composable(
            route = Screen.ProductBatches.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductBatchScreen(navController, productId)
        }
    }
}