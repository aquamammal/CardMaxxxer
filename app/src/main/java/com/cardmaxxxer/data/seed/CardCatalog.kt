package com.cardmaxxxer.data.seed

import com.cardmaxxxer.domain.model.CardNetwork

/**
 * Catalog of known credit cards with their specs.
 * Used to auto-fill card details when adding a new card.
 */
object CardCatalog {

    data class CardSpec(
        val issuer: String,
        val productName: String,
        val network: CardNetwork,
        val annualFeeCents: Long = 0L,
        val foreignTransactionFeePercent: Double? = null,
    )

    val cards: List<CardSpec> = listOf(
        // Amex
        CardSpec("American Express", "The Platinum Card® from American Express", CardNetwork.AMEX, 69500L, 0.0),
        CardSpec("American Express", "Blue Cash Preferred® Card from American Express", CardNetwork.AMEX, 9500L, 2.7),
        CardSpec("American Express", "Blue Cash Everyday® Card from American Express", CardNetwork.AMEX, 0L, 2.7),
        CardSpec("American Express", "American Express® Gold Card", CardNetwork.AMEX, 32500L, 0.0),
        CardSpec("American Express", "American Express® Green Card", CardNetwork.AMEX, 15000L, 0.0),
        CardSpec("American Express", "Delta SkyMiles® Gold American Express Card", CardNetwork.AMEX, 0L, 0.0),
        CardSpec("American Express", "Delta SkyMiles® Platinum American Express Card", CardNetwork.AMEX, 35000L, 0.0),
        CardSpec("American Express", "Marriott Bonvoy American Express® Card", CardNetwork.AMEX, 9500L, 0.0),
        CardSpec("American Express", "Hilton Honors American Express Card", CardNetwork.AMEX, 0L, 0.0),
        CardSpec("American Express", "Hilton Honors American Express Surpass® Card", CardNetwork.AMEX, 15000L, 0.0),

        // Chase
        CardSpec("Chase", "Chase Sapphire Preferred®", CardNetwork.VISA, 9500L, 0.0),
        CardSpec("Chase", "Chase Sapphire Reserve®", CardNetwork.VISA, 55000L, 0.0),
        CardSpec("Chase", "Chase Freedom Unlimited®", CardNetwork.VISA, 0L, 3.0),
        CardSpec("Chase", "Chase Freedom Flex℠", CardNetwork.VISA, 0L, 3.0),
        CardSpec("Chase", "Chase Freedom®", CardNetwork.VISA, 0L, 3.0),
        CardSpec("Chase", "Chase Amazon Prime Rewards Visa Signature®", CardNetwork.VISA, 0L, 0.0),
        CardSpec("Chase", "Chase United℠ Explorer Card", CardNetwork.VISA, 0L, 0.0),
        CardSpec("Chase", "Chase Ink Business Preferred®", CardNetwork.VISA, 9500L, 0.0),
        CardSpec("Chase", "Chase Southwest Rapid Rewards® Plus", CardNetwork.VISA, 6900L, 0.0),

        // Citi
        CardSpec("Citi", "Citi Costco Anywhere Visa® Card", CardNetwork.VISA, 0L, 0.0),
        CardSpec("Citi", "Citi Double Cash® Card", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("Citi", "Citi Custom Cash℠ Card", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("Citi", "Citi Premier® Card", CardNetwork.MASTERCARD, 9500L, 0.0),
        CardSpec("Citi", "Citi Rewards+® Card", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("Citi", "Citi® / AAdvantage® Platinum Select®", CardNetwork.MASTERCARD, 9900L, 0.0),

        // Capital One
        CardSpec("Capital One", "Capital One VentureOne Rewards Credit Card", CardNetwork.MASTERCARD, 0L, 0.0),
        CardSpec("Capital One", "Capital One Venture Rewards Credit Card", CardNetwork.MASTERCARD, 9500L, 0.0),
        CardSpec("Capital One", "Capital One Venture X Rewards Credit Card", CardNetwork.MASTERCARD, 39500L, 0.0),
        CardSpec("Capital One", "Capital One SavorOne Rewards Credit Card", CardNetwork.MASTERCARD, 0L, 0.0),
        CardSpec("Capital One", "Capital One Quicksilver Rewards Credit Card", CardNetwork.MASTERCARD, 0L, 0.0),
        CardSpec("Capital One", "Capital One Spark Cash Plus", CardNetwork.MASTERCARD, 0L, 0.0),

        // Discover
        CardSpec("Discover", "Discover it® Cash Back", CardNetwork.DISCOVER, 0L, 0.0),
        CardSpec("Discover", "Discover it® Miles", CardNetwork.DISCOVER, 0L, 0.0),
        CardSpec("Discover", "Discover it® Chrome", CardNetwork.DISCOVER, 0L, 0.0),

        // Wells Fargo
        CardSpec("Wells Fargo", "Wells Fargo Active Cash℠ Card", CardNetwork.VISA, 0L, 3.0),
        CardSpec("Wells Fargo", "Wells Fargo Autograph℠ Card", CardNetwork.VISA, 0L, 0.0),
        CardSpec("Wells Fargo", "Wells Fargo Autograph℠ Journey Visa℠ Card", CardNetwork.VISA, 9500L, 0.0),
        CardSpec("Wells Fargo", "Bilt Mastercard®", CardNetwork.MASTERCARD, 0L, 0.0),

        // Bank of America
        CardSpec("Bank of America", "Bank of America® Customized Cash Rewards", CardNetwork.VISA, 0L, 3.0),
        CardSpec("Bank of America", "Bank of America® Premium Rewards®", CardNetwork.VISA, 9500L, 0.0),
        CardSpec("Bank of America", "Bank of America® Travel Rewards", CardNetwork.VISA, 0L, 3.0),

        // Barclays
        CardSpec("Barclays", "AARP® Essential Rewards Mastercard®", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("Barclays", "Barclays View Mastercard®", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("Barclays", "JetBlue Card", CardNetwork.MASTERCARD, 9900L, 0.0),
        CardSpec("Barclays", "Hawaiian Airlines® World Elite Mastercard®", CardNetwork.MASTERCARD, 9900L, 0.0),

        // US Bank
        CardSpec("US Bank", "U.S. Bank Altitude® Reserve Visa Infinite®", CardNetwork.VISA, 40000L, 0.0),
        CardSpec("US Bank", "U.S. Bank Altitude® Connect Visa Signature®", CardNetwork.VISA, 9500L, 0.0),
        CardSpec("US Bank", "U.S. Bank Cash+® Visa Signature®", CardNetwork.VISA, 0L, 3.0),

        // Synchrony store cards
        CardSpec("Synchrony Bank", "Amazon Store Card", CardNetwork.OTHER, 0L, 0.0),
        CardSpec("Synchrony Bank", "Lowe's Credit Card", CardNetwork.OTHER, 0L, 0.0),
        CardSpec("Synchrony Bank", "Care Credit", CardNetwork.OTHER, 0L, 0.0),
        CardSpec("Synchrony Bank", "PayPal Credit Card", CardNetwork.OTHER, 0L, 0.0),
        CardSpec("Synchrony Bank", "Sam's Club® Mastercard®", CardNetwork.MASTERCARD, 0L, 0.0),
        CardSpec("Synchrony Bank", "Walmart Credit Card", CardNetwork.OTHER, 0L, 0.0),

        // Other
        CardSpec("Citizens Bank", "Home Depot Consumer Credit Card", CardNetwork.OTHER, 0L, 0.0),
        CardSpec("HSBC", "HSBC Cash Rewards Mastercard®", CardNetwork.MASTERCARD, 0L, 3.0),
        CardSpec("PNC", "PNC Cash Rewards® Visa Signature®", CardNetwork.VISA, 0L, 3.0),
        CardSpec("TD Bank", "TD Cash Credit Card", CardNetwork.VISA, 0L, 3.0),
    )
}
