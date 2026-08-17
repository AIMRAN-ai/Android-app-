package com.aimr.aimrpos.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biometric
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.aimr.aimrpos.security.BiometricAuthManager
import com.aimr.aimrpos.security.OAuthManager
import com.aimr.aimrpos.security.SecurityUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel(),
    biometricManager: BiometricAuthManager = hiltViewModel(),
    oauthManager: OAuthManager = hiltViewModel()
) {
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val securityStatus by remember { mutableStateOf(SecurityUtils.getSecurityStatus(context)) }
    val oauthState by oauthManager.authState.collectAsState()

    LaunchedEffect(Unit) {
        oauthManager.initializeGoogleSignIn(context as androidx.fragment.app.FragmentActivity)
    }

    LaunchedEffect(oauthState) {
        when (oauthState) {
            is OAuthState.Success -> {
                val oauth = oauthState as OAuthState.Success
                viewModel.handleOAuthLogin(
                    provider = oauth.provider,
                    userId = oauth.userId,
                    email = oauth.email,
                    name = oauth.name,
                    token = oauth.token
                ) { success, message ->
                    if (success) {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        error = message
                    }
                }
            }
            is OAuthState.Error -> {
                error = (oauthState as OAuthState.Error).message
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AIMRAN POS",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontSize = 36.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enterprise Edition",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Secure Login",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (securityStatus.isSecure) {
                    Text(
                        text = "✓ Device security verified",
                        color = Color(0xFF10B981),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    Text(
                        text = "⚠ Security warnings detected",
                        color = Color(0xFFF59E0B),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it.take(6) },
                    label = { Text("PIN", color = Color(0xFF94A3B8)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color(0xFF0F172A),
                        unfocusedContainerColor = Color(0xFF0F172A)
                    )
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error!!,
                        color = Color(0xFFEF4444),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (pin.length >= 4) {
                            isLoading = true
                            viewModel.verifyPin(pin) { success, message ->
                                isLoading = false
                                if (success) {
                                    navController.navigate("dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    error = message
                                }
                            }
                        } else {
                            error = "Enter at least 4 digits"
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("LOGIN", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val biometricAvailability = biometricManager.isBiometricAvailable()
                        if (biometricAvailability is BiometricAuthManager.BiometricAvailability.Available) {
                            biometricManager.authenticate(
                                activity = context as androidx.fragment.app.FragmentActivity,
                                onSuccess = {
                                    viewModel.biometricSuccess { success, message ->
                                        if (success) {
                                            navController.navigate("dashboard") {
                                                popUpTo("login") { inclusive = true }
                                            }
                                        } else {
                                            error = message
                                        }
                                    }
                                },
                                onError = { error = it }
                            )
                        } else {
                            error = "Biometric authentication not available"
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Biometric, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("BIOMETRIC LOGIN")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Or continue with",
                    color = Color(0xFF64748B),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { oauthManager.signInWithGoogle() },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        Text("Google", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = {
                            oauthManager.signInWithGitHub(context as androidx.fragment.app.FragmentActivity)
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        Text("GitHub", color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (securityStatus.isSecure) {
            Text(
                text = "🔒 Secure Connection • Encrypted Storage • Anti-Tampering Active",
                color = Color(0xFF10B981),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            val warnings = securityStatus.getWarnings()
            warnings.forEach { warning ->
                Text(
                    text = "⚠ $warning",
                    color = Color(0xFFF59E0B),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}