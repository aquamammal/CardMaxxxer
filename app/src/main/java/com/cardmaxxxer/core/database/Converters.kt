package com.cardmaxxxer.core.database

import androidx.room.TypeConverter
import com.cardmaxxxer.domain.model.BenefitCadence
import com.cardmaxxxer.domain.model.BenefitCategory
import com.cardmaxxxer.domain.model.BenefitValueKind
import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.MerchantCategory
import com.cardmaxxxer.domain.model.RedemptionStatus
import com.cardmaxxxer.domain.model.SpendingContext

class Converters {

    // CardNetwork
    @TypeConverter
    fun fromCardNetwork(value: CardNetwork): String = value.name

    @TypeConverter
    fun toCardNetwork(value: String): CardNetwork = CardNetwork.valueOf(value)

    // BenefitCategory
    @TypeConverter
    fun fromBenefitCategory(value: BenefitCategory): String = value.name

    @TypeConverter
    fun toBenefitCategory(value: String): BenefitCategory = BenefitCategory.valueOf(value)

    // BenefitCadence
    @TypeConverter
    fun fromBenefitCadence(value: BenefitCadence): String = value.name

    @TypeConverter
    fun toBenefitCadence(value: String): BenefitCadence = BenefitCadence.valueOf(value)

    // BenefitValueKind
    @TypeConverter
    fun fromBenefitValueKind(value: BenefitValueKind): String = value.name

    @TypeConverter
    fun toBenefitValueKind(value: String): BenefitValueKind = BenefitValueKind.valueOf(value)

    // RedemptionStatus
    @TypeConverter
    fun fromRedemptionStatus(value: RedemptionStatus): String = value.name

    @TypeConverter
    fun toRedemptionStatus(value: String): RedemptionStatus = RedemptionStatus.valueOf(value)

    // List<SpendingContext> as CSV
    @TypeConverter
    fun fromSpendingContextList(value: List<SpendingContext>): String =
        value.joinToString(",") { it.name }

    @TypeConverter
    fun toSpendingContextList(value: String): List<SpendingContext> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { SpendingContext.valueOf(it) }

    // List<MerchantCategory> as CSV
    @TypeConverter
    fun fromMerchantCategoryList(value: List<MerchantCategory>): String =
        value.joinToString(",") { it.name }

    @TypeConverter
    fun toMerchantCategoryList(value: String): List<MerchantCategory> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { MerchantCategory.valueOf(it) }
}
