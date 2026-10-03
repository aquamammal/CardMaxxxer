package com.cardmaxxxer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_card_cross_reference",
    primaryKeys = ["userId", "cardId"],
    foreignKeys = [ForeignKey(
        entity = CreditCardEntity::class,
        parentColumns = ["id"],
        childColumns = ["cardId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserCardCrossReferenceEntity(
    val userId: String,
    val cardId: String,
    val userNickname: String?,
    val priorityRank: Int,
    val totalSavingsCents: Long,
    val lastUsedAt: Long?,
    val isArchived: Boolean,
    val addedAt: Long,
)
