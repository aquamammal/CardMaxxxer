package com.cardmaxxxer.data.seed

import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.CreditCard
import java.util.UUID

/**
 * Seed data for the user's card wallet.
 * Populated from the user's card list screenshot.
 * Card names match official issuer names.
 */
object SeedData {

    data class SeedCard(
        val issuer: String,
        val productName: String,
        val network: CardNetwork,
        val nickname: String? = null,
        val annualFeeCents: Long = 0L,
        val foreignTransactionFeePercent: Double? = null,
    )

    val cards: List<SeedCard> = listOf(
        // Synchrony / Store Cards
        SeedCard(
            issuer = "Synchrony Bank",
            productName = "Amazon Store Card",
            network = CardNetwork.OTHER,
            nickname = "Amazon Store Card",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Synchrony Bank",
            productName = "Lowe's Credit Card",
            network = CardNetwork.OTHER,
            nickname = "Lowe's",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Synchrony Bank",
            productName = "Care Credit",
            network = CardNetwork.OTHER,
            nickname = "Care Credit",
            foreignTransactionFeePercent = 0.0,
        ),

        // Amex
        SeedCard(
            issuer = "American Express",
            productName = "The Platinum Card® from American Express",
            network = CardNetwork.AMEX,
            nickname = "Amex Platinum",
            annualFeeCents = 69500L,
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "American Express",
            productName = "The Platinum Card® from American Express",
            network = CardNetwork.AMEX,
            nickname = "Amex Platinum (Liz - AU)",
            annualFeeCents = 69500L,
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "American Express",
            productName = "Blue Cash Preferred® Card from American Express",
            network = CardNetwork.AMEX,
            nickname = "Amex Blue Cash Preferred",
            annualFeeCents = 9500L,
            foreignTransactionFeePercent = 2.7,
        ),

        // Capital One
        SeedCard(
            issuer = "Capital One",
            productName = "Bass Pro Shops Club Card",
            network = CardNetwork.OTHER,
            nickname = "Bass Pro",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Capital One",
            productName = "Capital One VentureOne Rewards Credit Card",
            network = CardNetwork.MASTERCARD,
            nickname = "Venture One",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Capital One",
            productName = "Capital One SavorOne Rewards Credit Card",
            network = CardNetwork.MASTERCARD,
            nickname = "Savor One",
            foreignTransactionFeePercent = 0.0,
        ),

        // Chase
        SeedCard(
            issuer = "Chase",
            productName = "Chase Amazon Prime Rewards Visa Signature®",
            network = CardNetwork.VISA,
            nickname = "Chase Amazon Prime",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Chase",
            productName = "Chase Freedom Unlimited®",
            network = CardNetwork.VISA,
            nickname = "Freedom Unlimited",
            foreignTransactionFeePercent = 3.0,
        ),
        SeedCard(
            issuer = "Chase",
            productName = "Chase Sapphire Preferred®",
            network = CardNetwork.VISA,
            nickname = "Sapphire Preferred",
            annualFeeCents = 9500L,
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Chase",
            productName = "Chase Freedom Flex℠",
            network = CardNetwork.VISA,
            nickname = "Freedom Flex",
            foreignTransactionFeePercent = 3.0,
        ),
        SeedCard(
            issuer = "Chase",
            productName = "Chase Freedom®",
            network = CardNetwork.VISA,
            nickname = "Freedom",
            foreignTransactionFeePercent = 3.0,
        ),

        // Citi
        SeedCard(
            issuer = "Citi",
            productName = "Citi Costco Anywhere Visa® Card",
            network = CardNetwork.VISA,
            nickname = "Citi Costco",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "Citi",
            productName = "Citi Double Cash® Card",
            network = CardNetwork.MASTERCARD,
            nickname = "Double Cash",
            foreignTransactionFeePercent = 3.0,
        ),
        SeedCard(
            issuer = "Citi",
            productName = "Citi Custom Cash℠ Card",
            network = CardNetwork.MASTERCARD,
            nickname = "Custom Cash",
            foreignTransactionFeePercent = 3.0,
        ),

        // Discover
        SeedCard(
            issuer = "Discover",
            productName = "Discover it® Cash Back",
            network = CardNetwork.DISCOVER,
            nickname = "Discover it",
            foreignTransactionFeePercent = 0.0,
        ),

        // Retail / Other
        SeedCard(
            issuer = "Citizens Bank",
            productName = "Home Depot Consumer Credit Card",
            network = CardNetwork.OTHER,
            nickname = "Home Depot",
            foreignTransactionFeePercent = 0.0,
        ),
        SeedCard(
            issuer = "HSBC",
            productName = "HSBC Cash Rewards Mastercard®",
            network = CardNetwork.MASTERCARD,
            nickname = "HSBC Cash Rewards",
            foreignTransactionFeePercent = 3.0,
        ),

        // Bilt
        SeedCard(
            issuer = "Wells Fargo",
            productName = "Bilt Mastercard®",
            network = CardNetwork.MASTERCARD,
            nickname = "Bilt Blue",
            foreignTransactionFeePercent = 0.0,
        ),

        // Wells Fargo
        SeedCard(
            issuer = "Wells Fargo",
            productName = "Wells Fargo Active Cash℠ Card",
            network = CardNetwork.VISA,
            nickname = "Active Cash",
            foreignTransactionFeePercent = 3.0,
        ),
        SeedCard(
            issuer = "Wells Fargo",
            productName = "Wells Fargo Autograph℠ Card",
            network = CardNetwork.VISA,
            nickname = "Autograph",
            foreignTransactionFeePercent = 0.0,
        ),

        // Bank of America
        SeedCard(
            issuer = "Bank of America",
            productName = "Bank of America® Customized Cash Rewards",
            network = CardNetwork.VISA,
            nickname = "BoA Customized Cash (03103)",
            foreignTransactionFeePercent = 3.0,
        ),
        SeedCard(
            issuer = "Bank of America",
            productName = "Bank of America® Customized Cash Rewards",
            network = CardNetwork.VISA,
            nickname = "BoA Customized Cash (10019)",
            foreignTransactionFeePercent = 3.0,
        ),

        // Barclays
        SeedCard(
            issuer = "Barclays",
            productName = "AARP® Essential Rewards Mastercard®",
            network = CardNetwork.MASTERCARD,
            nickname = "Barclays AARP",
            foreignTransactionFeePercent = 3.0,
        ),
    )

    fun toCreditCards(): List<CreditCard> {
        val now = System.currentTimeMillis()
        return cards.map { seed ->
            CreditCard(
                id = UUID.randomUUID().toString(),
                issuer = seed.issuer,
                productName = seed.productName,
                network = seed.network,
                nickname = seed.nickname,
                annualFeeCents = seed.annualFeeCents,
                foreignTransactionFeePercent = seed.foreignTransactionFeePercent,
                isActive = true,
                createdAt = now,
                updatedAt = now,
            )
        }
    }
}
