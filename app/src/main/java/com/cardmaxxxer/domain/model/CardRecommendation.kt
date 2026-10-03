package com.cardmaxxxer.domain.model

/**
 * Domain model for a card recommendation produced by the recommendation engine.
 *
 * @param card The recommended credit card.
 * @param crossReference User-specific cross-reference, null if not linked.
 * @param score Composite recommendation score (higher is better).
 * @param confidence Confidence level based on data completeness.
 * @param unusedValueCents Total unused benefit value in cents.
 * @param expiringBenefits Benefits expiring soon on this card.
 * @param matchedBenefits Benefits matching the current spending context.
 * @param reasons Human-readable reasons for the recommendation.
 * @param rank 1-based rank after sorting by score descending.
 */
data class CardRecommendation(
    val card: CreditCard,
    val crossReference: UserCardCrossReference? = null,
    val score: Double,
    val confidence: RecommendationConfidence,
    val unusedValueCents: Long = 0L,
    val expiringBenefits: List<CardBenefit> = emptyList(),
    val matchedBenefits: List<CardBenefit> = emptyList(),
    val reasons: List<String> = emptyList(),
    val rank: Int = 0,
)
