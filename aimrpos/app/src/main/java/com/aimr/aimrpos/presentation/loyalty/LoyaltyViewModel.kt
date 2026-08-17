package com.aimr.aimrpos.presentation.loyalty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aimr.aimrpos.domain.model.LoyaltyCustomer
import com.aimr.aimrpos.domain.model.LoyaltyRule
import com.aimr.aimrpos.domain.model.LoyaltyTransaction
import com.aimr.aimrpos.domain.repository.LoyaltyCustomerRepository
import com.aimr.aimrpos.domain.repository.LoyaltyRuleRepository
import com.aimr.aimrpos.domain.repository.LoyaltyTransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoyaltyState(
    val customers: List<LoyaltyCustomer> = emptyList(),
    val rules: List<LoyaltyRule> = emptyList(),
    val transactions: List<LoyaltyTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class LoyaltyViewModel @Inject constructor(
    private val loyaltyCustomerRepository: LoyaltyCustomerRepository,
    private val loyaltyRuleRepository: LoyaltyRuleRepository,
    private val loyaltyTransactionRepository: LoyaltyTransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoyaltyState())
    val state: StateFlow<LoyaltyState> = _state.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            loyaltyCustomerRepository.getAll().collect { customers ->
                _state.value = _state.value.copy(customers = customers)
            }
        }
        viewModelScope.launch {
            loyaltyRuleRepository.getAllActive().collect { rules ->
                _state.value = _state.value.copy(rules = rules)
            }
        }
        viewModelScope.launch {
            loyaltyTransactionRepository.getBetween(0, System.currentTimeMillis()).collect { transactions ->
                _state.value = _state.value.copy(transactions = transactions, isLoading = false)
            }
        }
    }

    fun addCustomer(customer: LoyaltyCustomer) {
        viewModelScope.launch {
            try {
                loyaltyCustomerRepository.upsert(customer)
                loadData()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun addRule(rule: LoyaltyRule) {
        viewModelScope.launch {
            try {
                loyaltyRuleRepository.upsert(rule)
                loadData()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun addTransaction(transaction: LoyaltyTransaction) {
        viewModelScope.launch {
            try {
                loyaltyTransactionRepository.insert(transaction)
                loadData()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun addPoints(customerId: String, points: Int) {
        viewModelScope.launch {
            try {
                loyaltyCustomerRepository.addPoints(customerId, points)
                loadData()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    fun redeemPoints(customerId: String, points: Int) {
        viewModelScope.launch {
            try {
                loyaltyCustomerRepository.redeemPoints(customerId, points)
                loadData()
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
