package com.cardmaxxxer.domain.model

/**
 * Domain model for a single card benefit / perk.
 *
 * @param id Unique identifier.
 * @param cardId Parent card ID.
 * @param title Short benefit title (e.g. "$300 Travel Credit").
 * @param description Optional longer description.
 * @param category Benefit category.
 * @param valueCents Monetary value in cents, null for non-monetary perks.
 * @param valueKind How the value is delivered.
 * @param cadence How often the benefit resets.
 * @param contextTags Spending contexts this benefit applies to.
 * @param merchantCategories Merchant categories this benefit applies to.
 * @param termsUrl Optional URL to benefit terms.
 * @param isRecurring Whether the benefit recurs on its cadence.
 * @param periodStartEpochDay Start of current benefit period, null if not period-based.
 * @param expiresAtEpochDay When the benefit expires, null if no expiration.
 * @param reminderLeadDays Days before expiration to trigger a reminder.
 */
data class CardBenefit(
    val id: String,
    val cardId: String,
    val title: String,
    val description: String? = null,
    val category: BenefitCategory,
    val valueCents: Long? = null,
    val valueKind: BenefitValueKind,
    val rewardRatePercent: Double? = null,
    val quarterStartDate: Long? = null,
    val quarterEndDate: Long? = null,
    val nextQuarterCategories: String? = null,
    val cadence: BenefitCadence,
    val contextTags: List<SpendingContext> = emptyList(),
    val merchantCategories: List<MerchantCategory> = emptyList(),
    val termsUrl: String? = null,
    val isRecurring: Boolean = true,
    val periodStartEpochDay: Long? = null,
    val expiresAtEpochDay: Long? = null,
    val reminderLeadDays: Int = 7,
)
