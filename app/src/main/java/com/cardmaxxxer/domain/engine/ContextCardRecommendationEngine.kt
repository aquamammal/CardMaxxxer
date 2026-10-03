package com.cardmaxxxer.domain.engine

import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.CardRecommendation
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.model.PerkRedemptionLog
import com.cardmaxxxer.domain.model.RecommendationConfidence
import com.cardmaxxxer.domain.model.SpendingContext
import com.cardmaxxxer.domain.model.UserCardCrossReference
import kotlin.math.max
import kotlin.math.min

data class ScoringWeights(
    val contextMatch: Double = 45.0,
    val categoryMatch: Double = 15.0,
    val unusedValue: Double = 20.0,
    val expiringSoon: Double = 15.0,
    val userPriority: Double = 10.0,
    val historicalUsage: Double = 8.0,
    val annualFeePenalty: Double = 6.0,
    val expiringSoonWindowDays: Int = 14,
)

data class RecommendationInput(
    val context: SpendingContext,
    val todayEpochDay: Long,
    val cards: List<CreditCard>,
    val benefits: List<CardBenefit>,
    val redemptions: List<PerkRedemptionLog>,
    val merchantCategory: com.cardmaxxxer.domain.model.MerchantCategory? = null,
    val amountCents: Long? = null,
)

class ContextCardRecommendationEngine(
    private val weights: ScoringWeights,
) {

    fun recommend(input: RecommendationInput): List<CardRecommendation> {
        val benefitsByCard = input.benefits.groupBy { it.cardId }
        val redemptionsByBenefit = input.redemptions.groupBy { it.benefitId }

        val recommendations = input.cards.mapNotNull { card ->
            val cardBenefits = benefitsByCard[card.id].orEmpty()
            if (cardBenefits.isEmpty()) return@mapNotNull null

            val crossRef = null // populated by repository layer
            val (score, confidence, reasons, matched, expiring) = scoreCard(
                card, cardBenefits, redemptionsByBenefit, input
            )

            CardRecommendation(
                card = card,
                crossReference = crossRef,
                score = score,
                confidence = confidence,
                unusedValueCents = computeUnusedValue(cardBenefits, redemptionsByBenefit),
                expiringBenefits = expiring,
                matchedBenefits = matched,
                reasons = reasons,
                rank = 0, // set after sorting
            )
        }

        return recommendations
            .sortedByDescending { it.score }
            .mapIndexed { index, rec -> rec.copy(rank = index + 1) }
    }

    private fun scoreCard(
        card: CreditCard,
        benefits: List<CardBenefit>,
        redemptionsByBenefit: Map<String, List<PerkRedemptionLog>>,
        input: RecommendationInput,
    ): ScoringResult {
        var score = 0.0
        val reasons = mutableListOf<String>()
        val matched = mutableListOf<CardBenefit>()
        val expiring = mutableListOf<CardBenefit>()

        // Context match
        val contextMatches = benefits.filter { it.contextTags.contains(input.context) }
        if (contextMatches.isNotEmpty()) {
            score += weights.contextMatch
            reasons.add("Matches ${input.context.name.lowercase().replace("_", " ")} context")
            matched.addAll(contextMatches)
        }

        // Category match
        val categoryMatches = benefits.filter { benefit ->
            benefit.category.name.equals(input.context.name, ignoreCase = true) ||
                categoryMatchesContext(benefit.category, input.context)
        }
        if (categoryMatches.isNotEmpty()) {
            score += weights.categoryMatch
            reasons.add("Benefit category aligns with spending")
            matched.addAll(categoryMatches.filter { it !in matched })
        }

        // Unused value
        val unusedValue = computeUnusedValue(benefits, redemptionsByBenefit)
        if (unusedValue > 0) {
            val maxPossible = benefits.sumOf { it.valueCents ?: 0 }
            if (maxPossible > 0) {
                val unusedRatio = unusedValue.toDouble() / maxPossible.toDouble()
                score += weights.unusedValue * unusedRatio
                reasons.add("$${unusedValue / 100.0} unused value available")
            }
        }

        // Expiring soon
        val expiringBenefits = benefits.filter { benefit ->
            benefit.expiresAtEpochDay?.let { expiry ->
                val daysUntil = expiry - input.todayEpochDay
                daysUntil in 0..weights.expiringSoonWindowDays
            } == true
        }
        if (expiringBenefits.isNotEmpty()) {
            score += weights.expiringSoon
            reasons.add("${expiringBenefits.size} benefit(s) expiring soon")
            expiring.addAll(expiringBenefits)
        }

        // User priority (placeholder — crossRef not available here)
        // Historical usage
        val totalRedeemed = redemptionsByBenefit.filter { (benefitId, _) ->
            benefits.any { it.id == benefitId }
        }.flatMap { it.value }.sumOf { it.amountSavedCents }
        if (totalRedeemed > 0) {
            score += weights.historicalUsage
            reasons.add("You've redeemed value on this card before")
        }

        // Annual fee penalty
        if (card.annualFeeCents > 0) {
            val penaltyRatio = min(card.annualFeeCents / 55000.0, 1.0) // normalize to $550
            score -= weights.annualFeePenalty * penaltyRatio
        }

        // Merchant category bonus
        if (input.merchantCategory != null) {
            val merchantMatches = benefits.filter { it.merchantCategories.contains(input.merchantCategory) }
            if (merchantMatches.isNotEmpty()) {
                score += 10.0
                reasons.add("Matches specific merchant type")
                matched.addAll(merchantMatches.filter { it !in matched })
            }
        }

        // Confidence
        val confidence = when {
            score >= 70.0 -> RecommendationConfidence.HIGH
            score >= 40.0 -> RecommendationConfidence.MEDIUM
            else -> RecommendationConfidence.LOW
        }

        return ScoringResult(score, confidence, reasons, matched, expiring)
    }

    private fun computeUnusedValue(
        benefits: List<CardBenefit>,
        redemptionsByBenefit: Map<String, List<PerkRedemptionLog>>,
    ): Long {
        return benefits.sumOf { benefit ->
            // Only count redeemable benefits (statement credits, free goods) as unused value.
            // Insurance coverage amounts and point multipliers are not redeemable credits.
            if (benefit.valueKind != com.cardmaxxxer.domain.model.BenefitValueKind.STATEMENT_CREDIT &&
                benefit.valueKind != com.cardmaxxxer.domain.model.BenefitValueKind.FREE_GOODS
            ) return@sumOf 0L
            val total = benefit.valueCents ?: 0L
            val redeemed = redemptionsByBenefit[benefit.id]
                ?.filter { it.status == com.cardmaxxxer.domain.model.RedemptionStatus.FULLY_USED }
                ?.sumOf { it.amountSavedCents } ?: 0L
            maxOf(0L, total - redeemed)
        }
    }

    private fun categoryMatchesContext(
        category: com.cardmaxxxer.domain.model.BenefitCategory,
        context: SpendingContext,
    ): Boolean = when (context) {
        SpendingContext.DINING -> category == com.cardmaxxxer.domain.model.BenefitCategory.DINING
        SpendingContext.GROCERY -> category == com.cardmaxxxer.domain.model.BenefitCategory.GROCERY
        SpendingContext.GAS -> category == com.cardmaxxxer.domain.model.BenefitCategory.GAS
        SpendingContext.TRAVEL -> category in listOf(
            com.cardmaxxxer.domain.model.BenefitCategory.TRAVEL,
            com.cardmaxxxer.domain.model.BenefitCategory.HOTEL,
            com.cardmaxxxer.domain.model.BenefitCategory.AIRLINE,
        )
        SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING ->
            category == com.cardmaxxxer.domain.model.BenefitCategory.SHOPPING
        SpendingContext.RIDE_SHARE -> category == com.cardmaxxxer.domain.model.BenefitCategory.RIDE_SHARE
        SpendingContext.STREAMING -> category == com.cardmaxxxer.domain.model.BenefitCategory.STREAMING
        SpendingContext.ENTERTAINMENT -> category == com.cardmaxxxer.domain.model.BenefitCategory.ENTERTAINMENT
        SpendingContext.OTHER -> false
    }

    private data class ScoringResult(
        val score: Double,
        val confidence: RecommendationConfidence,
        val reasons: List<String>,
        val matched: List<CardBenefit>,
        val expiring: List<CardBenefit>,
    )
}
