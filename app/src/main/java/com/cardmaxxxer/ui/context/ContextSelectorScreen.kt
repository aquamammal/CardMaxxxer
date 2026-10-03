package com.cardmaxxxer.ui.context

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cardmaxxxer.domain.model.CardRecommendation
import com.cardmaxxxer.domain.model.RecommendationConfidence
import com.cardmaxxxer.domain.model.SpendingContext
import com.cardmaxxxer.ui.components.formatDollars

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextSelectorScreen(
    cardId: String?,
    onBack: () -> Unit,
    onPerkClick: (String, String) -> Unit,
    viewModel: ContextSelectorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(cardId) {
        if (cardId == null) {
            viewModel.selectContext(SpendingContext.OTHER)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("What are you spending on?") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleSortByPoints() }) {
                        Icon(
                            if (uiState.sortByPoints) Icons.Default.Star else Icons.Default.SwapVert,
                            contentDescription = if (uiState.sortByPoints) "Sorted by points" else "Sort by points",
                            tint = if (uiState.sortByPoints) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            ContextGrid(
                selectedContext = uiState.selectedContext,
                onContextSelected = { viewModel.selectContext(it, cardId) },
            )

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.recommendations.isNotEmpty()) {
                Text(
                    text = "Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(uiState.recommendations, key = { it.card.id }) { rec ->
                        RecommendationCard(
                            recommendation = rec,
                            onClick = {
                                rec.matchedBenefits.firstOrNull()?.let { benefit ->
                                    onPerkClick(benefit.id, benefit.cardId)
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContextGrid(
    selectedContext: SpendingContext?,
    onContextSelected: (SpendingContext) -> Unit,
) {
    val contexts = listOf(
        SpendingContext.DINING to Triple("Dining", Icons.Default.Fastfood, "🍽️"),
        SpendingContext.GROCERY to Triple("Grocery", Icons.Default.LocalGroceryStore, "🛒"),
        SpendingContext.GAS to Triple("Gas", Icons.Default.DirectionsCar, "⛽"),
        SpendingContext.TRAVEL to Triple("Travel", Icons.Default.Flight, "✈️"),
        SpendingContext.SHOPPING to Triple("Shopping", Icons.Default.ShoppingCart, "🛍️"),
        SpendingContext.ONLINE_SHOPPING to Triple("Online", Icons.Default.ShoppingCart, "💻"),
        SpendingContext.RIDE_SHARE to Triple("Ride", Icons.Default.DirectionsCar, "🚗"),
        SpendingContext.STREAMING to Triple("Streaming", Icons.Default.Movie, "🎬"),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(220.dp),
    ) {
        items(contexts) { (context, info) ->
            val (label, icon, _) = info
            val isSelected = selectedContext == context
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onContextSelected(context) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        icon,
                        contentDescription = label,
                        modifier = Modifier.size(28.dp),
                        tint = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(recommendation: CardRecommendation, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        when (recommendation.rank) {
                            1 -> MaterialTheme.colorScheme.primary
                            2 -> MaterialTheme.colorScheme.secondary
                            3 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "#${recommendation.rank}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (recommendation.rank <= 3) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recommendation.card.nickname ?: recommendation.card.productName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = recommendation.card.issuer,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                // Show reward rate (cash back % or points multiplier) - PROMINENT
                val rewardRate = recommendation.matchedBenefits
                    .mapNotNull { it.rewardRatePercent }
                    .maxOrNull()
                if (rewardRate != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (rewardRate >= 2.0) {
                            "${rewardRate.toInt()}x points"
                        } else {
                            "${rewardRate}% back"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                if (recommendation.reasons.isNotEmpty()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = recommendation.reasons.first(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                // Show quarter dates for rotating categories
                val rotatingBenefit = recommendation.matchedBenefits.firstOrNull {
                    it.quarterStartDate != null && it.quarterEndDate != null
                }
                if (rotatingBenefit != null) {
                    Spacer(Modifier.height(2.dp))
                    val startDate = java.time.LocalDate.ofEpochDay(rotatingBenefit.quarterStartDate!!)
                    val endDate = java.time.LocalDate.ofEpochDay(rotatingBenefit.quarterEndDate!!)
                    Text(
                        text = "Q${(startDate.monthValue - 1) / 3 + 1}: ${startDate.month}/${startDate.dayOfMonth} - ${endDate.month}/${endDate.dayOfMonth}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    rotatingBenefit.nextQuarterCategories?.let { nextQ ->
                        Text(
                            text = "Next: $nextQ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (recommendation.unusedValueCents > 0) {
                    Text(
                        text = formatDollars(recommendation.unusedValueCents),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "unused",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                ConfidenceBadge(recommendation.confidence)
            }
        }
    }
}

@Composable
private fun ConfidenceBadge(confidence: RecommendationConfidence) {
    val color = when (confidence) {
        RecommendationConfidence.HIGH -> MaterialTheme.colorScheme.tertiary
        RecommendationConfidence.MEDIUM -> MaterialTheme.colorScheme.secondary
        RecommendationConfidence.LOW -> MaterialTheme.colorScheme.outline
    }
    Box(
        modifier = Modifier
            .padding(top = 4.dp)
            .size(width = 60.dp, height = 20.dp)
            .let { mod ->
                mod.padding(0.dp)
            },
    ) {
        Text(
            text = confidence.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelSmall,
            color = color,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}
