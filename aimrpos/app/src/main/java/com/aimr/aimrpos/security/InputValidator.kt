package com.aimr.aimrpos.security

import android.util.Patterns
import java.util.regex.Pattern

object InputValidator {

    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Error("Email is required")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult.Error("Invalid email format")
        }
        return ValidationResult.Success
    }

    fun validatePhone(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult.Error("Phone number is required")
        }
        val cleanPhone = phone.replace(Regex("[^\\d+]"), "")
        val phonePattern = Pattern.compile("^\\+?[\\d]{10,15}$")
        if (!phonePattern.matcher(cleanPhone).matches()) {
            return ValidationResult.Error("Invalid phone number format")
        }
        return ValidationResult.Success
    }

    fun validatePin(pin: String): ValidationResult {
        if (pin.isBlank()) {
            return ValidationResult.Error("PIN is required")
        }
        if (pin.length < 4) {
            return ValidationResult.Error("PIN must be at least 4 digits")
        }
        if (!pin.all { it.isDigit() }) {
            return ValidationResult.Error("PIN must contain only digits")
        }
        return ValidationResult.Success
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult.Error("Password is required")
        }
        if (password.length < 8) {
            return ValidationResult.Error("Password must be at least 8 characters")
        }
        if (!password.any { it.isUpperCase() }) {
            return ValidationResult.Error("Password must contain at least one uppercase letter")
        }
        if (!password.any { it.isLowerCase() }) {
            return ValidationResult.Error("Password must contain at least one lowercase letter")
        }
        if (!password.any { it.isDigit() }) {
            return ValidationResult.Error("Password must contain at least one digit")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            return ValidationResult.Error("Password must contain at least one special character")
        }
        return ValidationResult.Success
    }

    fun validateProductName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult.Error("Product name is required")
        }
        if (name.length < 2) {
            return ValidationResult.Error("Product name must be at least 2 characters")
        }
        if (name.length > 100) {
            return ValidationResult.Error("Product name must be less than 100 characters")
        }
        return ValidationResult.Success
    }

    fun validatePrice(price: String): ValidationResult {
        if (price.isBlank()) {
            return ValidationResult.Error("Price is required")
        }
        val priceValue = price.toDoubleOrNull()
        if (priceValue == null) {
            return ValidationResult.Error("Price must be a valid number")
        }
        if (priceValue < 0) {
            return ValidationResult.Error("Price cannot be negative")
        }
        if (priceValue > 999999999.99) {
            return ValidationResult.Error("Price is too large")
        }
        return ValidationResult.Success
    }

    fun validateQuantity(quantity: String): ValidationResult {
        if (quantity.isBlank()) {
            return ValidationResult.Error("Quantity is required")
        }
        val qtyValue = quantity.toDoubleOrNull()
        if (qtyValue == null) {
            return ValidationResult.Error("Quantity must be a valid number")
        }
        if (qtyValue <= 0) {
            return ValidationResult.Error("Quantity must be greater than 0")
        }
        return ValidationResult.Success
    }

    fun validateCustomerName(name: String): ValidationResult {
        if (name.isBlank()) {
            return ValidationResult.Error("Customer name is required")
        }
        if (name.length < 2) {
            return ValidationResult.Error("Customer name must be at least 2 characters")
        }
        return ValidationResult.Success
    }

    fun validateInvoiceNumber(invoiceNumber: String): ValidationResult {
        if (invoiceNumber.isBlank()) {
            return ValidationResult.Error("Invoice number is required")
        }
        val pattern = Pattern.compile("^[A-Z]{2,4}-\\d{8}-\\d{3}$", Pattern.CASE_INSENSITIVE)
        if (!pattern.matcher(invoiceNumber).matches()) {
            return ValidationResult.Error("Invalid invoice number format (e.g., INV-20260804-001)")
        }
        return ValidationResult.Success
    }

    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace(Regex("[<>\"'&]"), "")
            .take(255)
    }

    fun sanitizeHtml(input: String): String {
        return input.replace(Regex("<[^>]*>"), "")
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()

    fun isSuccess(): Boolean = this is Success
    fun errorMessage(): String = (this as Error).message
}