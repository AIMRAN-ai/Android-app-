package com.aimr.aimrpos.security

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.scottyab.rootbeer.RootBeer
import java.io.File

object SecurityUtils {

    fun isDeviceRooted(context: Context): Boolean {
        return try {
            val rootBeer = RootBeer(context)
            rootBeer.isRooted || hasRootedFiles() || hasRootedProcesses()
        } catch (e: Exception) {
            Log.e("Security", "Root check failed", e)
            hasRootedFiles() || hasRootedProcesses()
        }
    }

    fun isDebuggerAttached(): Boolean {
        return try {
            android.os.Debug.isDebuggerConnected() ||
            android.os.Debug.waitForDebugger() == false ||
            hasDebuggerProcesses()
        } catch (e: Exception) {
            Log.e("Security", "Debugger check failed", e)
            false
        }
    }

    fun isDeveloperOptionsEnabled(context: Context): Boolean {
        return try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ADB_ENABLED, 0) == 1
        } catch (e: Exception) {
            false
        }
    }

    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic") ||
        Build.FINGERPRINT.startsWith("unknown") ||
        Build.MODEL.contains("google_sdk") ||
        Build.MODEL.contains("Emulator") ||
        Build.MODEL.contains("Android SDK built for x86") ||
        Build.MODEL.contains("VirtualBox") ||
        Build.MODEL.contains("VMware") ||
        Build.MODEL.contains("Bluestacks")
    }

    fun hasRootedFiles(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/xbin/daemonsu",
            "/system/etc/init.d/99SuperSUDaemon",
            "/system/etc/init.d/SuperSUDaemon",
            "/system/app/SuperSU",
            "/system/app/SuperSU.apk",
            "/system/app/Superuser",
            "/system/app/Superuser.apk",
            "/system/app/Vphone",
            "/system/app/Vphone.apk",
            "/system/app/KingRoot",
            "/system/app/KingRoot.apk"
        )
        return paths.any { File(it).exists() }
    }

    private fun hasRootedProcesses(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("su")
            process.outputStream.use { it.write("exit\n".toByteArray()) }
            process.waitFor() == 0
        } catch (e: Exception) {
            false
        }
    }

    private fun hasDebuggerProcesses(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("ps")
            val output = process.inputStream.bufferedReader().readText()
            output.contains("gdbserver") || output.contains("jdb") || output.contains("debuggerd")
        } catch (e: Exception) {
            false
        }
    }

    fun isScreenshotAllowed(context: Context): Boolean {
        return try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.SCREENSHOT_ALLOWED, 1) == 1
        } catch (e: Exception) {
            true
        }
    }

    fun getSecurityStatus(context: Context): SecurityStatus {
        return SecurityStatus(
            isRooted = isDeviceRooted(context),
            isDebuggerAttached = isDebuggerAttached(),
            isDeveloperOptionsEnabled = isDeveloperOptionsEnabled(context),
            isEmulator = isEmulator(),
            isScreenshotAllowed = isScreenshotAllowed(context)
        )
    }

    data class SecurityStatus(
        val isRooted: Boolean = false,
        val isDebuggerAttached: Boolean = false,
        val isDeveloperOptionsEnabled: Boolean = false,
        val isEmulator: Boolean = false,
        val isScreenshotAllowed: Boolean = true
    ) {
        fun isSecure(): Boolean {
            return !isRooted && !isDebuggerAttached && !isEmulator
        }

        fun getWarnings(): List<String> {
            val warnings = mutableListOf<String>()
            if (isRooted) warnings.add("Device is rooted")
            if (isDebuggerAttached) warnings.add("Debugger is attached")
            if (isDeveloperOptionsEnabled) warnings.add("Developer options enabled")
            if (isEmulator) warnings.add("Running on emulator")
            return warnings
        }
    }
}