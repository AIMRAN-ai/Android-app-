package com.aimr.aimrpos.security

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.aimr.aimrpos.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricAuthManager @Inject constructor(
    private val context: Context
) {
    private val TAG = "BiometricAuth"
    private val _authState = MutableStateFlow<BiometricState>(BiometricState.Idle)
    val authState: Flow<BiometricState> = _authState.asStateFlow()

    fun isBiometricAvailable(): BiometricAvailability {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.Available
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAvailability.NotAvailable("No biometric hardware")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailability.NotAvailable("Hardware unavailable")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailability.NotEnrolled("No biometrics enrolled")
            else -> BiometricAvailability.NotAvailable("Unknown error")
        }
    }

    fun authenticate(activity: FragmentActivity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    _authState.value = BiometricState.Success
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    _authState.value = BiometricState.Failed
                    onError("Biometric authentication failed")
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    _authState.value = BiometricState.Error(errString.toString())
                    onError(errString.toString())
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("AIMRAN POS Authentication")
            .setSubtitle("Authenticate to access the app")
            .setNegativeButtonText("Use PIN")
            .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        try {
            biometricPrompt.authenticate(promptInfo)
        } catch (e: Exception) {
            Log.e(TAG, "Biometric authentication failed", e)
            onError("Biometric authentication not available")
        }
    }

    fun resetState() {
        _authState.value = BiometricState.Idle
    }
}

sealed class BiometricState {
    object Idle : BiometricState()
    object Success : BiometricState()
    object Failed : BiometricState()
    data class Error(val message: String) : BiometricState()
}

sealed class BiometricAvailability {
    object Available : BiometricAvailability()
    data class NotAvailable(val reason: String) : BiometricAvailability()
    data class NotEnrolled(val reason: String) : BiometricAvailability()
}