package com.cardmaxxxer.ui.editcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.repository.CreditCardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCardViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {

    private val _card = MutableStateFlow<CreditCard?>(null)
    val card: StateFlow<CreditCard?> = _card.asStateFlow()

    fun loadCard(cardId: String) {
        viewModelScope.launch {
            creditCardRepository.observeById(cardId).collect { card ->
                _card.value = card
            }
        }
    }

    fun updateCard(
        cardId: String,
        issuer: String,
        productName: String,
        network: String,
        lastFour: String?,
        nickname: String?,
        annualFeeCents: Long,
        balanceCents: Long,
        foreignTransactionFeePercent: Double?,
    ) {
        viewModelScope.launch {
            val existing = creditCardRepository.getById(cardId) ?: return@launch
            val updated = existing.copy(
                issuer = issuer,
                productName = productName,
                network = com.cardmaxxxer.domain.model.CardNetwork.valueOf(network),
                lastFour = lastFour,
                nickname = nickname,
                annualFeeCents = annualFeeCents,
                balanceCents = balanceCents,
                foreignTransactionFeePercent = foreignTransactionFeePercent,
                updatedAt = System.currentTimeMillis(),
            )
            creditCardRepository.update(updated)
        }
    }
}
