package com.aimr.aimrpos.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.User
import com.aimr.aimrpos.security.SecureStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val secureStorage: SecureStorage
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun verifyPin(pin: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val storedHash = secureStorage.getPinHash()
                if (storedHash == null) {
                    val hash = hashPin(pin)
                    secureStorage.savePinHash(hash)
                    _loginState.value = LoginState.Success("PIN created")
                    onResult(true, "PIN created successfully")
                    return@launch
                }

                val inputHash = hashPin(pin)
                if (inputHash == storedHash) {
                    _loginState.value = LoginState.Success("PIN verified")
                    onResult(true, "Login successful")
                } else {
                    _loginState.value = LoginState.Error("Invalid PIN")
                    onResult(false, "Invalid PIN")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Authentication error: ${e.message}")
                onResult(false, "Authentication error: ${e.message}")
            }
        }
    }

    fun handleOAuthLogin(
        provider: String,
        userId: String,
        email: String,
        name: String,
        token: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val user = User(
                    id = userId.ifBlank { "oauth_${provider.lowercase()}_${System.currentTimeMillis()}" },
                    name = name.ifBlank { email.substringBefore("@") },
                    role = "STAFF",
                    phone = null,
                    pinHash = "",
                    updatedAt = System.currentTimeMillis()
                )

                secureStorage.saveUserSession(user, provider)
                secureStorage.saveOAuthToken(provider, token)

                _loginState.value = LoginState.Success("OAuth login successful")
                onResult(true, "Welcome, ${user.name}!")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("OAuth login failed: ${e.message}")
                onResult(false, "OAuth login failed: ${e.message}")
            }
        }
    }

    fun biometricSuccess(onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = secureStorage.getCurrentUser()
                if (user != null) {
                    _loginState.value = LoginState.Success("Biometric login successful")
                    onResult(true, "Welcome back, ${user.name}!")
                } else {
                    _loginState.value = LoginState.Error("No user session found")
                    onResult(false, "No user session found. Please login with PIN first.")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Biometric login failed: ${e.message}")
                onResult(false, "Biometric login failed: ${e.message}")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            secureStorage.clearSession()
            _loginState.value = LoginState.Idle
        }
    }

    private fun hashPin(pin: String): String {
        val salt = "aimr_pos_salt_2024".toByteArray()
        val pinBytes = pin.toByteArray()
        val combined = pinBytes + salt
        val digest = java.security.MessageDigest.getInstance("SHA-256").digest(combined)
        return android.util.Base64.encodeToString(digest, android.util.Base64.NO_WRAP)
    }
}

sealed class LoginState {
    object Idle : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
    object Loading : LoginState()
}