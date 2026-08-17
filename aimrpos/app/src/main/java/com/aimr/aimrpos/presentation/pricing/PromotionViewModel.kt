package com.aimr.aimrpos.presentation.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.Promotion
import com.aimr.aimrpos.domain.repository.PromotionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

data class PromotionState(
    val promotions: List<Promotion> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PromotionViewModel @Inject constructor(
    private val promotionRepository: PromotionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PromotionState())
    val state: StateFlow<PromotionState> = _state.asStateFlow()

    fun loadPromotions() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            promotionRepository.getAllActive().collect { promotions ->
                _state.value = _state.value.copy(promotions = promotions, isLoading = false)
            }
        }
    }

    fun addPromotion(promotion: Promotion) {
        viewModelScope.launch {
            try {
                promotionRepository.upsert(promotion)
                loadPromotions()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun deletePromotion(promotionId: String) {
        viewModelScope.launch {
            try {
                promotionRepository.deleteById(promotionId)
                loadPromotions()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
