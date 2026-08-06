package com.aimr.aimrpos.presentation.scale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.ScaleItem
import com.aimr.aimrpos.domain.repository.ScaleItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScaleItemState(
    val scaleItems: List<ScaleItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ScaleItemViewModel(
    private val scaleItemRepository: ScaleItemRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ScaleItemState())
    val state: StateFlow<ScaleItemState> = _state.asStateFlow()

    fun loadScaleItems() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            scaleItemRepository.getAllActive().collect { scaleItems ->
                _state.value = _state.value.copy(scaleItems = scaleItems, isLoading = false)
            }
        }
    }

    fun addScaleItem(scaleItem: ScaleItem) {
        viewModelScope.launch {
            try {
                scaleItemRepository.upsert(scaleItem)
                loadScaleItems()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun deleteScaleItem(scaleItemId: String) {
        viewModelScope.launch {
            try {
                scaleItemRepository.deleteById(scaleItemId)
                loadScaleItems()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
