package com.cardmaxxxer.ui.perkdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.PerkRedemptionLog
import com.cardmaxxxer.domain.model.RedemptionStatus
import com.cardmaxxxer.domain.repository.CardBenefitRepository
import com.cardmaxxxer.domain.repository.PerkRedemptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class PerkDetailUiState(
    val benefit: CardBenefit? = null,
    val redemptionHistory: List<PerkRedemptionLog> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
)

@HiltViewModel
class PerkDetailViewModel @Inject constructor(
    private val cardBenefitRepository: CardBenefitRepository,
    private val perkRedemptionRepository: PerkRedemptionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerkDetailUiState())
    val uiState: StateFlow<PerkDetailUiState> = _uiState.asStateFlow()

    fun loadBenefit(benefitId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val benefit = cardBenefitRepository.getById(benefitId)
                if (benefit == null) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Benefit not found")
                    return@launch
                }
                perkRedemptionRepository.observeByBenefitId(benefitId).collect { history ->
                    _uiState.value = _uiState.value.copy(
                        benefit = benefit,
                        redemptionHistory = history,
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun markAsRedeemed(benefitId: String, cardId: String, amountCents: Long) {
        viewModelScope.launch {
            val today = LocalDate.now().toEpochDay()
            val log = PerkRedemptionLog(
                id = UUID.randomUUID().toString(),
                benefitId = benefitId,
                cardId = cardId,
                status = RedemptionStatus.FULLY_USED,
                redeemedAtEpochDay = today,
                amountSavedCents = amountCents,
                periodStartEpochDay = today,
                createdAt = System.currentTimeMillis(),
            )
            perkRedemptionRepository.insert(log)
        }
    }
}
