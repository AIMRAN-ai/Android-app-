package com.aimr.aimrpos.presentation.batch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.ProductBatch
import com.aimr.aimrpos.domain.repository.ProductBatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

data class ProductBatchState(
    val batches: List<ProductBatch> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductBatchViewModel @Inject constructor(
    private val productBatchRepository: ProductBatchRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProductBatchState())
    val state: StateFlow<ProductBatchState> = _state.asStateFlow()

    fun loadBatches(productId: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            val flow = if (productId != null && productId.isNotEmpty()) {
                productBatchRepository.getByProduct(productId)
            } else {
                productBatchRepository.getAll()
            }
            flow.collect { batches ->
                _state.value = _state.value.copy(batches = batches, isLoading = false)
            }
        }
    }

    fun addBatch(batch: ProductBatch) {
        viewModelScope.launch {
            try {
                productBatchRepository.upsert(batch)
                loadBatches(batch.productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun deleteBatch(batchId: String, productId: String?) {
        viewModelScope.launch {
            try {
                productBatchRepository.deleteById(batchId)
                loadBatches(productId)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
