package com.aimr.aimrpos.security

import android.content.Context
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(
    private val context: Context
) {
    private val TAG = "ErrorHandler"
    private val _errors = MutableStateFlow<List<AppError>>(emptyList())
    val errors: StateFlow<List<AppError>> = _errors.asStateFlow()

    fun handleError(error: Throwable, context: String = "") {
        val appError = AppError(
            message = error.message ?: "Unknown error",
            context = context,
            timestamp = System.currentTimeMillis(),
            stackTrace = error.stackTraceToString()
        )
        _errors.value = _errors.value + appError
        Log.e(TAG, "Error in $context: ${error.message}", error)

        FirebaseCrashlytics.getInstance().recordException(error)
    }

    fun handleError(message: String, context: String = "") {
        val appError = AppError(
            message = message,
            context = context,
            timestamp = System.currentTimeMillis(),
            stackTrace = ""
        )
        _errors.value = _errors.value + appError
        Log.e(TAG, "Error in $context: $message")
    }

    fun clearErrors() {
        _errors.value = emptyList()
    }

    fun getRecentErrors(limit: Int = 10): List<AppError> {
        return _errors.value.takeLast(limit)
    }
}

data class AppError(
    val message: String,
    val context: String,
    val timestamp: Long,
    val stackTrace: String
)

object NetworkSecurity {
    const val CERTIFICATE_PINNING_HOST = "api.aimrpos.com"
    const val CERTIFICATE_PINNING_PIN = "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="

    fun isNetworkSecure(context: Context): Boolean {
        return try {
            val connectivityManager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
            caps != null && caps.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            false
        }
    }

    fun getNetworkSecurityConfig(context: Context): android.content.res.XmlResourceParser? {
        return try {
            context.resources.getXml(android.R.xml.network_security_config)
        } catch (e: Exception) {
            null
        }
    }
}