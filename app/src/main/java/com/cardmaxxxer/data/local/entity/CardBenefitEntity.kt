package com.cardmaxxxer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "card_benefits",
    foreignKeys = [ForeignKey(
        entity = CreditCardEntity::class,
        parentColumns = ["id"],
        childColumns = ["cardId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cardId"), Index("expiresAtEpochDay")]
)
data class CardBenefitEntity(
    @PrimaryKey val id: String,
    val cardId: String,
    val title: String,
    val description: String?,
    val category: String,
    val valueCents: Long?,
    val valueKind: String,
    val rewardRatePercent: Double?,
    val quarterStartDate: Long?,
    val quarterEndDate: Long?,
    val nextQuarterCategories: String?,
    val cadence: String,
    val contextTags: String,
    val merchantCategories: String,
    val termsUrl: String?,
    val isRecurring: Boolean,
    val periodStartEpochDay: Long?,
    val expiresAtEpochDay: Long?,
    val reminderLeadDays: Int,
)
