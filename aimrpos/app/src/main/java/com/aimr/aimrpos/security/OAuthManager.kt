package com.aimr.aimrpos.security

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.aimr.aimrpos.domain.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OAuthManager @Inject constructor(
    private val context: Context
) {
    private val TAG = "OAuthManager"
    private val _authState = MutableStateFlow<OAuthState>(OAuthState.Idle)
    val authState: Flow<OAuthState> = _authState.asStateFlow()

    private var googleSignInLauncher: ActivityResultLauncher<Intent>? = null

    fun initializeGoogleSignIn(activity: FragmentActivity) {
        googleSignInLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleGoogleSignInResult(result.data)
        }
    }

    fun getGoogleSignInIntent(): Intent {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(com.google.android.gms.auth.api.signin.R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        return googleSignInClient.signInIntent
    }

    fun signInWithGoogle() {
        try {
            googleSignInLauncher?.launch(getGoogleSignInIntent())
                ?: _authState.value = OAuthState.Error("Google Sign-In not initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Google sign-in failed", e)
            _authState.value = OAuthState.Error("Google sign-in failed: ${e.message}")
        }
    }

    private fun handleGoogleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            _authState.value = OAuthState.Success(
                provider = "GOOGLE",
                userId = account?.id ?: "",
                email = account?.email ?: "",
                name = account?.displayName ?: "",
                photoUrl = account?.photoUrl?.toString(),
                token = account?.idToken ?: ""
            )
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign-in failed", e)
            _authState.value = OAuthState.Error("Google sign-in failed: ${e.statusCode}")
        } catch (e: Exception) {
            Log.e(TAG, "Google sign-in error", e)
            _authState.value = OAuthState.Error("Google sign-in error: ${e.message}")
        }
    }

    fun signInWithGitHub(activity: FragmentActivity) {
        val authUrl = Uri.parse("https://github.com/login/oauth/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", getGitHubClientId())
            .appendQueryParameter("redirect_uri", getGitHubRedirectUri())
            .appendQueryParameter("scope", "read:user user:email")
            .appendQueryParameter("state", generateState())
            .build()

        val intent = Intent(Intent.ACTION_VIEW, authUrl)
        try {
            activity.startActivity(intent)
            _authState.value = OAuthState.InProgress("GITHUB")
        } catch (e: Exception) {
            Log.e(TAG, "GitHub sign-in failed", e)
            _authState.value = OAuthState.Error("GitHub sign-in failed: ${e.message}")
        }
    }

    fun handleGitHubCallback(uri: Uri?): OAuthState {
        if (uri == null) {
            return OAuthState.Error("No callback URI received")
        }

        val code = uri.getQueryParameter("code")
        val state = uri.getQueryParameter("state")
        val error = uri.getQueryParameter("error")

        return if (error != null) {
            OAuthState.Error("GitHub auth error: $error")
        } else if (code != null) {
            _authState.value = OAuthState.Success(
                provider = "GITHUB",
                userId = "",
                email = "",
                name = "",
                photoUrl = null,
                token = code
            )
            OAuthState.Success(
                provider = "GITHUB",
                userId = "",
                email = "",
                name = "",
                photoUrl = null,
                token = code
            )
        } else {
            OAuthState.Error("Invalid GitHub callback")
        }
    }

    fun signOut() {
        GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut()
        _authState.value = OAuthState.Idle
    }

    private fun getGitHubClientId(): String {
        return try {
            context.packageManager.getApplicationInfo(context.packageName, android.content.pm.PackageManager.GET_META_DATA)
                .metaData.getString("GITHUB_CLIENT_ID") ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun getGitHubRedirectUri(): String {
        return "aimrpos://oauth/github/callback"
    }

    private fun generateState(): String {
        return java.util.UUID.randomUUID().toString()
    }
}

sealed class OAuthState {
    object Idle : OAuthState()
    data class InProgress(val provider: String) : OAuthState()
    data class Success(
        val provider: String,
        val userId: String,
        val email: String,
        val name: String,
        val photoUrl: String?,
        val token: String
    ) : OAuthState()
    data class Error(val message: String) : OAuthState()
}