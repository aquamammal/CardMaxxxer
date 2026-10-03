package com.cardmaxxxer.ui.context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.CardRecommendation
import com.cardmaxxxer.domain.model.SpendingContext
import com.cardmaxxxer.domain.repository.RecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ContextSelectorUiState(
    val selectedContext: SpendingContext? = null,
    val recommendations: List<CardRecommendation> = emptyList(),
    val isLoading: Boolean = false,
    val sortByPoints: Boolean = true,
)

@HiltViewModel
class ContextSelectorViewModel @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContextSelectorUiState())
    val uiState: StateFlow<ContextSelectorUiState> = _uiState.asStateFlow()

    fun selectContext(context: SpendingContext, cardId: String? = null) {
        _uiState.value = _uiState.value.copy(selectedContext = context, isLoading = true)
        viewModelScope.launch {
            val today = LocalDate.now().toEpochDay()
            val recs = recommendationRepository.getRecommendations(
                context = context,
                todayEpochDay = today,
            )
            // Sort by points by default
            val sorted = recs.sortedByDescending { rec ->
                val maxRate = rec.matchedBenefits.mapNotNull { it.rewardRatePercent }.maxOrNull() ?: 0.0
                if (maxRate >= 2.0) maxRate * 1.5 else maxRate
            }
            _uiState.value = _uiState.value.copy(
                recommendations = sorted,
                isLoading = false,
            )
        }
    }

    fun toggleSortByPoints() {
        val current = _uiState.value
        val sorted = if (!current.sortByPoints) {
            // Sort by highest reward rate for the selected context
            // Use a scoring function that properly compares percentages and multipliers
            current.recommendations.sortedByDescending { rec ->
                val maxRate = rec.matchedBenefits.mapNotNull { it.rewardRatePercent }.maxOrNull() ?: 0.0
                // Normalize: 5% cash back = 5.0, 3x points = 3.0 * 1.5 (points worth more)
                // But for "most points" we want raw multiplier comparison
                // A 5% card should beat a 3x card if points are worth ~1.5 cents
                // For simplicity: if rate >= 2.0 treat as points multiplier, else as cash back %
                // 5% cash back = 5.0, 3x points = 3.0 * 1.5 = 4.5 equivalent
                if (maxRate >= 2.0) maxRate * 1.5 else maxRate
            }
        } else {
            // Sort by overall score (default)
            current.recommendations.sortedByDescending { it.score }
        }
        _uiState.value = current.copy(
            sortByPoints = !current.sortByPoints,
            recommendations = sorted,
        )
    }
}
