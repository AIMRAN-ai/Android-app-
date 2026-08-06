package com.aimr.aimrpos.presentation.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.PriceTier
import com.aimr.aimrpos.domain.repository.PriceTierRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PriceTierState(
    val tiers: List<PriceTier> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PriceTierViewModel(
    private val priceTierRepository: PriceTierRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PriceTierState())
    val state: StateFlow<PriceTierState> = _state.asStateFlow()

    fun loadTiers(productId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            priceTierRepository.getByProduct(productId).collect { tiers ->
                _state.value = _state.value.copy(tiers = tiers, isLoading = false)
            }
        }
    }

    fun addTier(productId: String, minQty: Double, price: Double, customerType: String) {
        viewModelScope.launch {
            try {
                val tier = PriceTier(
                    productId = productId,
                    minQty = minQty,
                    price = price,
                    customerType = customerType
                )
                priceTierRepository.upsert(tier)
                loadTiers(productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun deleteTier(tierId: String, productId: String) {
        viewModelScope.launch {
            try {
                priceTierRepository.deleteById(tierId)
                loadTiers(productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
