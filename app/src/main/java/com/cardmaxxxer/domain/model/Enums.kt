package com.cardmaxxxer.domain.model

/**
 * Card payment network brands.
 */
enum class CardNetwork {
    VISA,
    MASTERCARD,
    AMEX,
    DISCOVER,
    OTHER,
}

/**
 * High-level category a benefit belongs to.
 */
enum class BenefitCategory {
    DINING,
    TRAVEL,
    GROCERY,
    GAS,
    SHOPPING,
    ENTERTAINMENT,
    RIDE_SHARE,
    STREAMING,
    HOTEL,
    AIRLINE,
    OTHER,
}

/**
 * How often a benefit resets or can be used.
 */
enum class BenefitCadence {
    MONTHLY,
    QUARTERLY,
    SEMI_ANNUAL,
    ANNUAL,
    CUSTOM,
}

/**
 * The form in which a benefit's value is delivered.
 */
enum class BenefitValueKind {
    STATEMENT_CREDIT,
    POINTS_MULTIPLIER,
    CASH_BACK,
    FREE_GOODS,
    DISCOUNT,
}

/**
 * Redemption state of a benefit for the current period.
 */
enum class RedemptionStatus {
    UNUSED,
    PARTIALLY_USED,
    FULLY_USED,
}

/**
 * Spending context used for recommendation matching.
 */
enum class SpendingContext {
    DINING,
    GROCERY,
    GAS,
    TRAVEL,
    SHOPPING,
    ONLINE_SHOPPING,
    RIDE_SHARE,
    STREAMING,
    ENTERTAINMENT,
    OTHER,
}

/**
 * Merchant category codes (MCC-level grouping).
 */
enum class MerchantCategory {
    RESTAURANT,
    FAST_FOOD,
    GROCERY_STORE,
    GAS_STATION,
    AIRLINE,
    HOTEL,
    ONLINE_RETAIL,
    STREAMING_SERVICE,
    RIDE_SHARE,
    OTHER,
}

/**
 * Confidence level for a card recommendation.
 */
enum class RecommendationConfidence {
    HIGH,
    MEDIUM,
    LOW,
}
