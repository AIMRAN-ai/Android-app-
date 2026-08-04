package com.aimr.aimrpos.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aimr.aimrpos.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(
    private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "aimr_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState: Flow<AuthState> = _authState.asStateFlow()

    suspend fun saveUserSession(user: User, provider: String = "PIN") {
        encryptedPrefs.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_role", user.role)
            .putString("auth_provider", provider)
            .putLong("login_time", System.currentTimeMillis())
            .apply()
        _authState.value = AuthState.LoggedIn(user, provider)
    }

    suspend fun clearSession() {
        encryptedPrefs.edit().clear().apply()
        _authState.value = AuthState.LoggedOut
    }

    fun getCurrentUser(): User? {
        val id = encryptedPrefs.getString("user_id", null) ?: return null
        val name = encryptedPrefs.getString("user_name", null) ?: return null
        val role = encryptedPrefs.getString("user_role", null) ?: "STAFF"
        return User(id = id, name = name, role = role)
    }

    fun getAuthProvider(): String {
        return encryptedPrefs.getString("auth_provider", "PIN") ?: "PIN"
    }

    fun isLoggedIn(): Boolean {
        return encryptedPrefs.contains("user_id") && _authState.value is AuthState.LoggedIn
    }

    fun savePinHash(pinHash: String) {
        encryptedPrefs.edit().putString("pin_hash", pinHash).apply()
    }

    fun getPinHash(): String? {
        return encryptedPrefs.getString("pin_hash", null)
    }

    fun saveOAuthToken(provider: String, token: String) {
        encryptedPrefs.edit().putString("${provider}_token", token).apply()
    }

    fun getOAuthToken(provider: String): String? {
        return encryptedPrefs.getString("${provider}_token", null)
    }

    fun removeOAuthToken(provider: String) {
        encryptedPrefs.edit().remove("${provider}_token").apply()
    }

    fun saveRefreshToken(refreshToken: String) {
        encryptedPrefs.edit().putString("refresh_token", refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return encryptedPrefs.getString("refresh_token", null)
    }
}

sealed class AuthState {
    object LoggedOut : AuthState()
    data class LoggedIn(val user: User, val provider: String) : AuthState()
    data class Error(val message: String) : AuthState()
}