package com.cardmaxxxer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "credit_cards")
data class CreditCardEntity(
    @PrimaryKey val id: String,
    val issuer: String,
    val productName: String,
    val network: String,
    val lastFour: String?,
    val nickname: String?,
    val cardArtColor: Long,
    val annualFeeCents: Long,
    val balanceCents: Long,
    val foreignTransactionFeePercent: Double?,
    val isActive: Boolean,
    val openedAtEpochDay: Long?,
    val createdAt: Long,
    val updatedAt: Long,
)
