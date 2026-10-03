package com.cardmaxxxer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "perk_redemption_logs",
    foreignKeys = [ForeignKey(
        entity = CardBenefitEntity::class,
        parentColumns = ["id"],
        childColumns = ["benefitId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PerkRedemptionLogEntity(
    @PrimaryKey val id: String,
    val benefitId: String,
    val cardId: String,
    val status: String,
    val redeemedAtEpochDay: Long,
    val amountSavedCents: Long,
    val note: String?,
    val periodStartEpochDay: Long?,
    val createdAt: Long,
)
