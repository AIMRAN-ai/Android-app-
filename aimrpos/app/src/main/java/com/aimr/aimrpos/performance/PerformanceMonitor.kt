package com.aimr.aimrpos.performance

import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.reflect.Field
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerformanceMonitor @Inject constructor(
    private val context: Context
) {
    private val TAG = "PerformanceMonitor"
    private val _metrics = MutableStateFlow<PerformanceMetrics>(PerformanceMetrics())
    val metrics: StateFlow<PerformanceMetrics> = _metrics.asStateFlow()

    fun recordQueryTime(queryName: String, executionTimeMs: Long) {
        val current = _metrics.value
        _metrics.value = current.copy(
            lastQueryTime = executionTimeMs,
            averageQueryTime = if (current.totalQueries == 0) {
                executionTimeMs
            } else {
                (current.averageQueryTime * current.totalQueries + executionTimeMs) / (current.totalQueries + 1)
            },
            totalQueries = current.totalQueries + 1,
            slowQueries = if (executionTimeMs > SLOW_QUERY_THRESHOLD_MS) {
                current.slowQueries + 1
            } else {
                current.slowQueries
            }
        )
        if (executionTimeMs > SLOW_QUERY_THRESHOLD_MS) {
            Log.w(TAG, "Slow query detected: $queryName took ${executionTimeMs}ms")
        }
    }

    fun recordMemoryUsage() {
        val runtime = Runtime.getRuntime()
        val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        val maxMemory = runtime.maxMemory() / 1024 / 1024
        val current = _metrics.value
        _metrics.value = current.copy(
            usedMemoryMB = usedMemory,
            maxMemoryMB = maxMemory,
            memoryUsagePercentage = (usedMemory.toFloat() / maxMemory.toFloat() * 100)
        )
    }

    fun getPerformanceReport(): PerformanceReport {
        val current = _metrics.value
        return PerformanceReport(
            totalQueries = current.totalQueries,
            averageQueryTime = current.averageQueryTime,
            slowQueries = current.slowQueries,
            usedMemoryMB = current.usedMemoryMB,
            maxMemoryMB = current.maxMemoryMB,
            memoryUsagePercentage = current.memoryUsagePercentage,
            recommendations = generateRecommendations(current)
        )
    }

    private fun generateRecommendations(metrics: PerformanceMetrics): List<String> {
        val recommendations = mutableListOf<String>()
        if (metrics.slowQueries > 10) {
            recommendations.add("Consider adding database indexes for slow queries")
        }
        if (metrics.memoryUsagePercentage > 80) {
            recommendations.add("Memory usage is high. Consider implementing pagination or lazy loading")
        }
        if (metrics.averageQueryTime > 100) {
            recommendations.add("Average query time is high. Consider optimizing queries or adding caching")
        }
        return recommendations
    }

    fun resetMetrics() {
        _metrics.value = PerformanceMetrics()
    }

    companion object {
        private const val SLOW_QUERY_THRESHOLD_MS = 500L
    }
}

data class PerformanceMetrics(
    val lastQueryTime: Long = 0,
    val averageQueryTime: Long = 0,
    val totalQueries: Int = 0,
    val slowQueries: Int = 0,
    val usedMemoryMB: Int = 0,
    val maxMemoryMB: Int = 0,
    val memoryUsagePercentage: Float = 0f
)

data class PerformanceReport(
    val totalQueries: Int,
    val averageQueryTime: Long,
    val slowQueries: Int,
    val usedMemoryMB: Int,
    val maxMemoryMB: Int,
    val memoryUsagePercentage: Float,
    val recommendations: List<String>
) {
    fun isHealthy(): Boolean {
        return slowQueries < 10 && memoryUsagePercentage < 80 && averageQueryTime < 100
    }
}