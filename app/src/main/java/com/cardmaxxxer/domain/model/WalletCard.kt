package com.cardmaxxxer.domain.model

/**
 * Domain model combining a credit card with its user cross-reference and aggregated benefit data.
 *
 * @param card The credit card.
 * @param crossReference User-specific cross-reference, null if not linked to a user.
 * @param benefits List of benefits associated with this card.
 * @param unusedValueCents Total unused benefit value in cents.
 * @param expiringSoonCount Number of benefits expiring within the reminder window.
 */
data class WalletCard(
    val card: CreditCard,
    val crossReference: UserCardCrossReference? = null,
    val benefits: List<CardBenefit> = emptyList(),
    val unusedValueCents: Long = 0L,
    val expiringSoonCount: Int = 0,
)
