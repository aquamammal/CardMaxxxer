package com.cardmaxxxer.domain.model

/**
 * Domain model representing a credit card product.
 *
 * @param id Unique identifier (UUID).
 * @param issuer Card issuer name (e.g. "Chase", "Amex").
 * @param productName Product name (e.g. "Sapphire Preferred").
 * @param network Payment network.
 * @param lastFour Last four digits of the card number, null if not set.
 * @param nickname User-assigned nickname.
 * @param cardArtColor ARGB color for card art rendering.
 * @param annualFeeCents Annual fee in cents (0 if no fee).
 * @param isActive Whether the card is currently active.
 * @param openedAtEpochDay Epoch day the card was opened, null if unknown.
 * @param createdAt Epoch millis when the record was created.
 * @param updatedAt Epoch millis when the record was last updated.
 */
data class CreditCard(
    val id: String,
    val issuer: String,
    val productName: String,
    val network: CardNetwork,
    val lastFour: String? = null,
    val nickname: String? = null,
    val cardArtColor: Long = 0xFF1A73E8,
    val annualFeeCents: Long = 0L,
    val foreignTransactionFeePercent: Double? = null,
    val isActive: Boolean = true,
    val openedAtEpochDay: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
