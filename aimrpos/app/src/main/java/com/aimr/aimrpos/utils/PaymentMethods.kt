package com.aimr.aimrpos.utils

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

    fun getDisplayName(method: String): String = when (method) {
        CASH -> "Cash"
        BANK -> "Bank Transfer"
        UPI -> "UPI"
        CREDIT_CARD -> "Credit Card"
        DEBIT_CARD -> "Debit Card"
        WALLET -> "Wallet"
        CHECK -> "Check"
        CREDIT -> "Credit (Udhaar)"
        else -> method
    }
}