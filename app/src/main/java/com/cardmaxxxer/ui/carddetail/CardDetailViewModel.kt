package com.cardmaxxxer.ui.carddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.WalletCard
import com.cardmaxxxer.domain.repository.CreditCardRepository
import com.cardmaxxxer.domain.repository.RecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    recommendationRepository: RecommendationRepository,
    private val creditCardRepository: CreditCardRepository,
) : ViewModel() {

    val walletCards: StateFlow<List<WalletCard>> = recommendationRepository.observeWalletCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            creditCardRepository.deleteById(cardId)
        }
    }
}
