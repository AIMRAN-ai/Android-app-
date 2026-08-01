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

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
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
    }
}