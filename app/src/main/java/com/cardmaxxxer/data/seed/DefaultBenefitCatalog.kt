package com.cardmaxxxer.data.seed

import com.cardmaxxxer.domain.model.BenefitCadence
import com.cardmaxxxer.domain.model.BenefitCategory
import com.cardmaxxxer.domain.model.BenefitValueKind
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.MerchantCategory
import com.cardmaxxxer.domain.model.SpendingContext
import java.util.UUID

object DefaultBenefitCatalog {

    fun getTemplatesForCard(cardId: String, issuer: String, productName: String): List<CardBenefit> {
        val key = "${issuer.lowercase()}|${productName.lowercase()}"
        return when {
            // Amex
            key.contains("american express") && key.contains("platinum") -> amexPlatinumBenefits(cardId)
            key.contains("american express") && key.contains("blue cash preferred") -> amexBlueCashPreferredBenefits(cardId)
            key.contains("american express") && key.contains("gold") -> amexGoldBenefits(cardId)

            // Chase
            key.contains("chase") && key.contains("amazon prime") -> chaseAmazonPrimeBenefits(cardId)
            key.contains("chase") && key.contains("freedom unlimited") -> chaseFreedomUnlimitedBenefits(cardId)
            key.contains("chase") && key.contains("freedom flex") -> chaseFreedomFlexBenefits(cardId)
            key.contains("chase") && key.contains("freedom") && !key.contains("unlimited") && !key.contains("flex") -> chaseFreedomBenefits(cardId)
            key.contains("chase") && key.contains("sapphire") -> chaseSapphireBenefits(cardId)

            // Citi
            key.contains("citi") && key.contains("costco") -> citiCostcoBenefits(cardId)
            key.contains("citi") && key.contains("double cash") -> citiDoubleCashBenefits(cardId)
            key.contains("citi") && key.contains("custom cash") -> citiCustomCashBenefits(cardId)

            // Capital One
            key.contains("capital one") && key.contains("bass pro") -> capitalOneBassProBenefits(cardId)
            key.contains("capital one") && key.contains("ventureone") -> capitalOneVentureOneBenefits(cardId)
            key.contains("capital one") && key.contains("savorone") -> capitalOneSavorOneBenefits(cardId)
            key.contains("capital one") && key.contains("venture") -> capitalOneVentureXBenefits(cardId)

            // Discover
            key.contains("discover") -> discoverItBenefits(cardId)

            // Wells Fargo
            key.contains("wells fargo") && key.contains("active cash") -> wellsFargoActiveCashBenefits(cardId)
            key.contains("wells fargo") && key.contains("autograph") -> wellsFargoAutographBenefits(cardId)
            key.contains("wells fargo") && key.contains("bilt") -> biltBlueBenefits(cardId)

            // Bank of America
            key.contains("bank of america") && key.contains("customized cash") -> boaCustomizedCashBenefits(cardId)

            // Barclays
            key.contains("barclays") && key.contains("aarp") -> barclaysAarpBenefits(cardId)

            // Synchrony store cards
            key.contains("synchrony") && key.contains("amazon") -> amazonStoreCardBenefits(cardId)
            key.contains("synchrony") && key.contains("lowe") -> lowesCardBenefits(cardId)
            key.contains("synchrony") && key.contains("care credit") -> careCreditBenefits(cardId)

            // Other
            key.contains("home depot") -> homeDepotCardBenefits(cardId)
            key.contains("hsbc") -> hsbcCashRewardsBenefits(cardId)

            else -> emptyList()
        }
    }

    // ==================== AMEX ====================

    private fun amexPlatinumBenefits(cardId: String): List<CardBenefit> = listOf(
        // Statement Credits
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Uber Cash", description = "$200 annual Uber Cash ($15/mo, $20 in Dec). No foreign transaction fees.",
            category = BenefitCategory.RIDE_SHARE, valueCents = 20000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.RIDE_SHARE, SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RIDE_SHARE),
            termsUrl = "https://www.americanexpress.com/uber", isRecurring = true,
            reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Digital Entertainment Credit", description = "$240 annual digital entertainment credit ($20/mo). Covers Disney+, Hulu, ESPN+, etc.",
            category = BenefitCategory.STREAMING, valueCents = 24000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.STREAMING),
            merchantCategories = listOf(MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Hotel Credit", description = "$200 annual hotel credit on prepaid bookings through Amex FINE HOTELS & RESORTS or The Hotel Collection",
            category = BenefitCategory.HOTEL, valueCents = 20000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.HOTEL),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Saks Fifth Avenue Credit", description = "$100 annual Saks credit ($50 semi-annually)",
            category = BenefitCategory.SHOPPING, valueCents = 10000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.SEMI_ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Airline Fee Credit", description = "$200 annual airline fee credit for incidentals (baggage, seat selection, etc.) with selected airline",
            category = BenefitCategory.TRAVEL, valueCents = 20000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "CLEAR Credit", description = "$189 annual CLEAR membership credit for expedited airport security",
            category = BenefitCategory.TRAVEL, valueCents = 18900, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Global Entry / TSA PreCheck", description = "$100 credit every 4 years for Global Entry or TSA PreCheck application fee",
            category = BenefitCategory.TRAVEL, valueCents = 10000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.CUSTOM,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = false, reminderLeadDays = 30,
        ),
        // Lounge Access
        // Earning Rates
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5x Points on Flights", description = "5x Membership Rewards points on flights booked directly or through Amex Travel (up to $500k/yr)",
            category = BenefitCategory.AIRLINE, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5x Points on Prepaid Hotels", description = "5x Membership Rewards points on prepaid hotels booked through Amex Travel",
            category = BenefitCategory.HOTEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1.5x Points on Eligible Purchases", description = "1.5x points on eligible purchases up to $200k/yr, then 1x. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 1.5,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun amexGoldBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Uber Cash", description = "$10 monthly Uber Cash for rides or Uber Eats",
            category = BenefitCategory.RIDE_SHARE, valueCents = 1000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.MONTHLY,
            contextTags = listOf(SpendingContext.RIDE_SHARE, SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RIDE_SHARE),
            termsUrl = "https://www.americanexpress.com/uber", isRecurring = true, reminderLeadDays = 3,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Dining Credit", description = "$10 monthly dining credit at select restaurants",
            category = BenefitCategory.DINING, valueCents = 1000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.MONTHLY,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT),
            isRecurring = true, reminderLeadDays = 3,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Dunkin' Credit", description = "$7 monthly Dunkin' credit",
            category = BenefitCategory.DINING, valueCents = 700, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.MONTHLY,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 3,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Resy Credit", description = "$100 annual Resy credit",
            category = BenefitCategory.DINING, valueCents = 10000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "4x Points on Dining", description = "4x Membership Rewards points at restaurants worldwide",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 4.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "4x Points on Groceries", description = "4x Membership Rewards points at U.S. supermarkets (up to $25k/yr)",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 4.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Flights", description = "3x Membership Rewards points on flights booked directly or through Amex Travel",
            category = BenefitCategory.AIRLINE, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun amexBlueCashPreferredBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "6% Cash Back at Supermarkets", description = "6% cash back at U.S. supermarkets (up to $6,000/yr). 2.7% foreign transaction fee.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 6.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back at Gas Stations", description = "3% cash back at U.S. gas stations. 2.7% foreign transaction fee.",
            category = BenefitCategory.GAS, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Streaming", description = "3% cash back on select streaming services. 2.7% foreign transaction fee.",
            category = BenefitCategory.STREAMING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.STREAMING),
            merchantCategories = listOf(MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. 2.7% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== CHASE ====================

    private fun chaseSapphireBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "$50 Annual Hotel Credit", description = "$50 annual hotel credit on stays booked through Chase Travel",
            category = BenefitCategory.HOTEL, valueCents = 5000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.HOTEL),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "DoorDash DashPass", description = "Complimentary DoorDash DashPass membership ($9.99/mo value)",
            category = BenefitCategory.DINING, valueCents = 10800, valueKind = BenefitValueKind.FREE_GOODS,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = false, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5x Points on Travel (Chase)", description = "5x points on travel purchased through Chase Travel. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Dining", description = "3x points on dining purchases including delivery and takeout. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Streaming", description = "3x points on select streaming services",
            category = BenefitCategory.STREAMING, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.STREAMING),
            merchantCategories = listOf(MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Online Grocery", description = "3x points on online grocery purchases (excluding Target and Walmart)",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2x Points on Other Travel", description = "2x points on all other travel purchases",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1x Points on Everything Else", description = "1x points on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "25% Points Bonus on Travel Redemptions", description = "Points worth 25% more when redeemed for travel through Chase",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun chaseAmazonPrimeBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Back at Amazon", description = "5% back on Amazon.com purchases. No foreign transaction fees.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Back at Whole Foods", description = "5% back at Whole Foods Market. No foreign transaction fees.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Back at Restaurants", description = "2% back at restaurants. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Back at Gas Stations", description = "2% back at gas stations. No foreign transaction fees.",
            category = BenefitCategory.GAS, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Back at Grocery Stores", description = "2% back at grocery stores. No foreign transaction fees.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Back on Everything Else", description = "1% back on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun chaseFreedomUnlimitedBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1.5% Cash Back on Everything", description = "1.5% cash back on all purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.5,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back on dining purchases. 3% foreign transaction fee.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Drugstores", description = "3% cash back at drugstores. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.OTHER),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Cash Back on Travel (Chase)", description = "5% cash back on travel purchased through Chase Travel. 3% foreign transaction fee.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun chaseFreedomFlexBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Rotating Categories (Quarterly)", description = "5% cash back on quarterly rotating categories (up to $1,500/quarter). Categories change every quarter and require activation. Past categories include: grocery stores, gas stations, restaurants, Amazon, wholesale clubs, streaming services, home improvement, travel, fitness clubs, select streaming. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            quarterStartDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).toEpochDay(),
            quarterEndDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).plusMonths(3).minusDays(1).toEpochDay(),
            nextQuarterCategories = "Check Chase app for next quarter categories",
            cadence = BenefitCadence.QUARTERLY,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.GROCERY, SpendingContext.GAS, SpendingContext.DINING, SpendingContext.STREAMING, SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION, MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.STREAMING_SERVICE, MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back on dining purchases. 3% foreign transaction fee.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Drugstores", description = "3% cash back at drugstores. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.OTHER),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Cash Back on Travel (Chase)", description = "5% cash back on travel purchased through Chase Travel. 3% foreign transaction fee.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun chaseFreedomBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Rotating Categories (Quarterly)", description = "5% cash back on quarterly rotating categories (up to $1,500/quarter). Categories change every quarter and require activation. Past categories include: grocery stores, gas stations, restaurants, Amazon, wholesale clubs, streaming services, home improvement, travel, fitness clubs, select streaming. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            quarterStartDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).toEpochDay(),
            quarterEndDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).plusMonths(3).minusDays(1).toEpochDay(),
            nextQuarterCategories = "Check Chase app for next quarter categories",
            cadence = BenefitCadence.QUARTERLY,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.GROCERY, SpendingContext.GAS, SpendingContext.DINING, SpendingContext.STREAMING, SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION, MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.STREAMING_SERVICE, MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back on dining purchases. 3% foreign transaction fee.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Drugstores", description = "3% cash back at drugstores. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.OTHER),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Cash Back on Travel (Chase)", description = "5% cash back on travel purchased through Chase Travel. 3% foreign transaction fee.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== CITI ====================

    private fun citiCostcoBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "4% Cash Back on Gas", description = "4% cash back on eligible gas (up to $7,000/yr). No foreign transaction fees.",
            category = BenefitCategory.GAS, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 4.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back at restaurants. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Travel", description = "3% cash back on travel purchases. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Cash Back at Costco", description = "2% cash back at Costco. No foreign transaction fees.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY, SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun citiDoubleCashBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Cash Back on Everything", description = "2% cash back on all purchases (1% when you buy, 1% when you pay). 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun citiCustomCashBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Cash Back on Top Category (Auto)", description = "5% cash back on your top eligible spending category each month (up to $500/mo). Automatically applies to your highest spending category. Eligible categories: restaurants, gas stations, grocery stores, select travel, select transit, select streaming, drugstores, home improvement, fitness clubs, live entertainment. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.MONTHLY,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS, SpendingContext.TRAVEL, SpendingContext.STREAMING, SpendingContext.ENTERTAINMENT),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION, MerchantCategory.AIRLINE, MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== CAPITAL ONE ====================

    private fun capitalOneVentureXBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Travel Portal Credit", description = "$300 annual credit for bookings through Capital One Travel. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = 30000, valueKind = BenefitValueKind.STATEMENT_CREDIT,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = false, reminderLeadDays = 14,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Anniversary Bonus Miles", description = "10,000 bonus miles each account anniversary. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = 10000, valueKind = BenefitValueKind.POINTS_MULTIPLIER,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE),
            isRecurring = false, reminderLeadDays = 30,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2x Miles on Everything", description = "2x miles on all purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun capitalOneVentureOneBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1.25x Miles on Everything", description = "1.25x miles on all purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 1.25,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun capitalOneSavorOneBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back on dining purchases. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Entertainment", description = "3% cash back on entertainment purchases. No foreign transaction fees.",
            category = BenefitCategory.ENTERTAINMENT, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.ENTERTAINMENT, SpendingContext.STREAMING),
            merchantCategories = listOf(MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Streaming", description = "3% cash back on streaming services. No foreign transaction fees.",
            category = BenefitCategory.STREAMING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.STREAMING),
            merchantCategories = listOf(MerchantCategory.STREAMING_SERVICE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Cash Back on Grocery", description = "5% cash back at grocery stores. No foreign transaction fees.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun capitalOneBassProBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Back at Bass Pro Shops", description = "5% back at Bass Pro Shops and Cabela's. No foreign transaction fees.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Back on Gas", description = "3% back on gas purchases. No foreign transaction fees.",
            category = BenefitCategory.GAS, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== DISCOVER ====================

    private fun discoverItBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Rotating Categories (Quarterly)", description = "5% cash back on quarterly rotating categories (up to $1,500/quarter). Categories change every quarter and require activation. Past categories include: grocery stores, gas stations, restaurants, Amazon, wholesale clubs, streaming services, home improvement, travel, fitness clubs, select streaming. No foreign transaction fees.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            quarterStartDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).toEpochDay(),
            quarterEndDate = java.time.LocalDate.now().withDayOfMonth(1).withMonth((java.time.LocalDate.now().monthValue - 1) / 3 * 3 + 1).plusMonths(3).minusDays(1).toEpochDay(),
            nextQuarterCategories = "Check Discover app for next quarter categories",
            cadence = BenefitCadence.QUARTERLY,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.GROCERY, SpendingContext.GAS, SpendingContext.DINING, SpendingContext.STREAMING, SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION, MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.STREAMING_SERVICE, MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 7,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== WELLS FARGO ====================

    private fun wellsFargoActiveCashBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Cash Back on Everything", description = "2% cash back on all purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun wellsFargoAutographBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Dining", description = "3x points on dining purchases. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Travel", description = "3x points on travel purchases. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Gas", description = "3x points on gas purchases. No foreign transaction fees.",
            category = BenefitCategory.GAS, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1x Points on Everything Else", description = "1x points on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    private fun biltBlueBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3x Points on Dining", description = "3x points on dining purchases. No foreign transaction fees.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2x Points on Travel", description = "2x points on travel purchases. No foreign transaction fees.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1x Points on Everything Else", description = "1x points on all other purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.POINTS_MULTIPLIER, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== BANK OF AMERICA ====================

    private fun boaCustomizedCashBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Choice Category", description = "3% cash back on your choice of category (gas, online shopping, dining, travel, drugstores, or home improvement). Can change monthly. 3% foreign transaction fee.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.GAS, SpendingContext.DINING, SpendingContext.TRAVEL, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.GAS_STATION, MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.AIRLINE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Cash Back at Grocery Stores", description = "2% cash back at grocery stores and wholesale clubs. 3% foreign transaction fee.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== BARCLAYS ====================

    private fun barclaysAarpBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Dining", description = "3% cash back at restaurants. 3% foreign transaction fee.",
            category = BenefitCategory.DINING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.DINING),
            merchantCategories = listOf(MerchantCategory.RESTAURANT, MerchantCategory.FAST_FOOD),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "3% Cash Back on Travel", description = "3% cash back on travel purchases. 3% foreign transaction fee.",
            category = BenefitCategory.TRAVEL, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 3.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.TRAVEL),
            merchantCategories = listOf(MerchantCategory.AIRLINE, MerchantCategory.HOTEL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "2% Cash Back at Grocery Stores", description = "2% cash back at grocery stores. 3% foreign transaction fee.",
            category = BenefitCategory.GROCERY, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 2.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.GROCERY),
            merchantCategories = listOf(MerchantCategory.GROCERY_STORE),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1% Cash Back on Everything Else", description = "1% cash back on all other purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )

    // ==================== SYNCHRONY STORE CARDS ====================

    private fun amazonStoreCardBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Back at Amazon", description = "5% back on Amazon.com purchases. No foreign transaction fees.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Special Financing", description = "Special financing on qualifying purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.DISCOUNT,
            cadence = BenefitCadence.CUSTOM,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = false, reminderLeadDays = 0,
        ),
    )

    private fun lowesCardBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "5% Back at Lowe's", description = "5% back at Lowe's stores. No foreign transaction fees.",
            category = BenefitCategory.SHOPPING, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 5.0,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = true, reminderLeadDays = 0,
        ),
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Special Financing", description = "Special financing on qualifying purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.DISCOUNT,
            cadence = BenefitCadence.CUSTOM,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = false, reminderLeadDays = 0,
        ),
    )

    private fun careCreditBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Special Financing", description = "Special financing on healthcare purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.DISCOUNT,
            cadence = BenefitCadence.CUSTOM,
            contextTags = listOf(SpendingContext.OTHER),
            merchantCategories = listOf(MerchantCategory.OTHER),
            isRecurring = false, reminderLeadDays = 0,
        ),
    )

    // ==================== OTHER ====================

    private fun homeDepotCardBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "Special Financing", description = "Special financing on qualifying purchases. No foreign transaction fees.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.DISCOUNT,
            cadence = BenefitCadence.CUSTOM,
            contextTags = listOf(SpendingContext.SHOPPING),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL),
            isRecurring = false, reminderLeadDays = 0,
        ),
    )

    private fun hsbcCashRewardsBenefits(cardId: String): List<CardBenefit> = listOf(
        CardBenefit(
            id = UUID.randomUUID().toString(), cardId = cardId,
            title = "1.5% Cash Back on Everything", description = "1.5% cash back on all purchases. 3% foreign transaction fee.",
            category = BenefitCategory.OTHER, valueCents = null, valueKind = BenefitValueKind.CASH_BACK, rewardRatePercent = 1.5,
            cadence = BenefitCadence.ANNUAL,
            contextTags = listOf(SpendingContext.SHOPPING, SpendingContext.ONLINE_SHOPPING, SpendingContext.DINING, SpendingContext.GROCERY, SpendingContext.GAS),
            merchantCategories = listOf(MerchantCategory.ONLINE_RETAIL, MerchantCategory.RESTAURANT, MerchantCategory.GROCERY_STORE, MerchantCategory.GAS_STATION),
            isRecurring = true, reminderLeadDays = 0,
        ),
    )
}
