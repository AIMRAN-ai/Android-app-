package com.aimr.aimrpos.presentation.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.PriceHistory
import com.aimr.aimrpos.domain.repository.PriceHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

data class PriceHistoryState(
    val histories: List<PriceHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PriceHistoryViewModel @Inject constructor(
    private val priceHistoryRepository: PriceHistoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PriceHistoryState())
    val state: StateFlow<PriceHistoryState> = _state.asStateFlow()

    fun loadHistory(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            priceHistoryRepository.getByProduct(productId).collect { histories ->
                _state.value = _state.value.copy(histories = histories, isLoading = false)
            }
        }
    }

    fun addHistory(history: PriceHistory) {
        viewModelScope.launch {
            try {
                priceHistoryRepository.insert(history)
                loadHistory(history.productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun deleteForProduct(productId: String) {
        viewModelScope.launch {
            try {
                priceHistoryRepository.deleteForProduct(productId)
                loadHistory(productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
