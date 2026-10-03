package com.cardmaxxxer.ui.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.WalletCard
import com.cardmaxxxer.domain.repository.RecommendationRepository
import com.cardmaxxxer.domain.repository.UserCardCrossReferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletFilter(
    val issuer: String? = null,
    val hasAnnualFee: Boolean? = null,
    val hasForeignTransactionFee: Boolean? = null,
    val network: String? = null,
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    recommendationRepository: RecommendationRepository,
    private val userCardCrossReferenceRepository: UserCardCrossReferenceRepository,
) : ViewModel() {

    private val _filter = MutableStateFlow(WalletFilter())
    val filter: StateFlow<WalletFilter> = _filter

    val walletCards: StateFlow<List<WalletCard>> = recommendationRepository.observeWalletCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredCards: StateFlow<List<WalletCard>> = combine(walletCards, _filter) { cards, filter ->
        cards.filter { walletCard ->
            val card = walletCard.card
            (filter.issuer == null || card.issuer.contains(filter.issuer, ignoreCase = true)) &&
            (filter.hasAnnualFee == null || (filter.hasAnnualFee == (card.annualFeeCents > 0))) &&
            (filter.hasForeignTransactionFee == null || (filter.hasForeignTransactionFee == (card.foreignTransactionFeePercent != null && card.foreignTransactionFeePercent > 0))) &&
            (filter.network == null || card.network.name == filter.network)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalUnusedValueCents: StateFlow<Long> = filteredCards
        .map { cards -> cards.sumOf { it.unusedValueCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalExpiringSoon: StateFlow<Int> = filteredCards
        .map { cards -> cards.sumOf { it.expiringSoonCount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val availableIssuers: StateFlow<List<String>> = walletCards
        .map { cards -> cards.map { it.card.issuer }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateFilter(filter: WalletFilter) {
        _filter.value = filter
    }

    fun moveCard(cardId: String, direction: Int) {
        viewModelScope.launch {
            val cards = walletCards.value.toMutableList()
            val index = cards.indexOfFirst { it.card.id == cardId }
            if (index < 0) return@launch
            val newIndex = index + direction
            if (newIndex < 0 || newIndex >= cards.size) return@launch
            val removed = cards.removeAt(index)
            cards.add(newIndex, removed)
            // Update priority ranks
            cards.forEachIndexed { i, walletCard ->
                val crossRef = walletCard.crossReference ?: return@forEachIndexed
                userCardCrossReferenceRepository.update(
                    crossRef.copy(priorityRank = i)
                )
            }
        }
    }
}
