package com.cardmaxxxer.domain.model

/**
 * Domain model linking a user to a card with user-specific metadata.
 *
 * @param userId User identifier.
 * @param cardId Card identifier.
 * @param userNickname User's personal nickname for this card.
 * @param priorityRank User's priority ranking (lower = higher priority).
 * @param totalSavingsCents Total savings attributed to this card in cents.
 * @param lastUsedAt Epoch millis when the card was last used, null if never.
 * @param isArchived Whether the card is archived (soft-deleted).
 * @param addedAt Epoch millis when the card was added to the user's wallet.
 */
data class UserCardCrossReference(
    val userId: String,
    val cardId: String,
    val userNickname: String? = null,
    val priorityRank: Int = 0,
    val totalSavingsCents: Long = 0L,
    val lastUsedAt: Long? = null,
    val isArchived: Boolean = false,
    val addedAt: Long,
)
