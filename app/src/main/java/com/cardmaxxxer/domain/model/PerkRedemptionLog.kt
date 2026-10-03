package com.cardmaxxxer.domain.model

/**
 * Domain model for a single perk redemption event.
 *
 * @param id Unique identifier.
 * @param benefitId The benefit that was redeemed.
 * @param cardId The card the benefit belongs to.
 * @param status Redemption status at time of logging.
 * @param redeemedAtEpochDay Epoch day the redemption occurred.
 * @param amountSavedCents Amount saved in cents.
 * @param note Optional user note.
 * @param periodStartEpochDay Start of the benefit period this redemption applies to.
 * @param createdAt Epoch millis when the log was created.
 */
data class PerkRedemptionLog(
    val id: String,
    val benefitId: String,
    val cardId: String,
    val status: RedemptionStatus,
    val redeemedAtEpochDay: Long,
    val amountSavedCents: Long = 0L,
    val note: String? = null,
    val periodStartEpochDay: Long? = null,
    val createdAt: Long,
)
