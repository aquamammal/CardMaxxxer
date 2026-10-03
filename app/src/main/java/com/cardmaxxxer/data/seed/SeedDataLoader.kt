package com.cardmaxxxer.data.seed

import android.content.Context
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.core.database.dao.UserCardCrossReferenceDao
import com.cardmaxxxer.data.local.entity.CardBenefitEntity
import com.cardmaxxxer.data.local.entity.CreditCardEntity
import com.cardmaxxxer.data.local.entity.UserCardCrossReferenceEntity
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.model.UserCardCrossReference
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds the database with the user's card wallet on first launch.
 * Idempotent — only seeds if the database is empty.
 */
@Singleton
class SeedDataLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val creditCardDao: CreditCardDao,
    private val userCardCrossReferenceDao: UserCardCrossReferenceDao,
    private val cardBenefitDao: CardBenefitDao,
) {

    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {
        // Check if database is actually empty (not just a flag, since schema
        // migrations with fallbackToDestructiveMigration wipe the data)
        val existingCards = creditCardDao.observeAllActive().first()
        if (existingCards.isEmpty()) {
            val now = System.currentTimeMillis()
            val seedCards = SeedData.toCreditCards()

            seedCards.forEach { seedCard ->
                creditCardDao.insert(seedCard.toEntity())

                val crossRef = UserCardCrossReference(
                    userId = "default_user",
                    cardId = seedCard.id,
                    userNickname = seedCard.nickname,
                    priorityRank = 0,
                    totalSavingsCents = 0L,
                    lastUsedAt = null,
                    isArchived = false,
                    addedAt = now,
                )
                userCardCrossReferenceDao.insert(crossRef.toEntity())

                val benefits = DefaultBenefitCatalog.getTemplatesForCard(
                    seedCard.id,
                    seedCard.issuer,
                    seedCard.productName,
                )
                if (benefits.isNotEmpty()) {
                    cardBenefitDao.insertAll(benefits.map { it.toEntity() })
                }
            }

        }
    }
}

private fun CreditCard.toEntity() = CreditCardEntity(
    id = id,
    issuer = issuer,
    productName = productName,
    network = network.name,
    lastFour = lastFour,
    nickname = nickname,
    cardArtColor = cardArtColor,
    annualFeeCents = annualFeeCents,
    balanceCents = balanceCents,
    foreignTransactionFeePercent = foreignTransactionFeePercent,
    isActive = isActive,
    openedAtEpochDay = openedAtEpochDay,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

private fun UserCardCrossReference.toEntity() = UserCardCrossReferenceEntity(
    userId = userId,
    cardId = cardId,
    userNickname = userNickname,
    priorityRank = priorityRank,
    totalSavingsCents = totalSavingsCents,
    lastUsedAt = lastUsedAt,
    isArchived = isArchived,
    addedAt = addedAt,
)

private fun CardBenefit.toEntity() = CardBenefitEntity(
    id = id,
    cardId = cardId,
    title = title,
    description = description,
    category = category.name,
    valueCents = valueCents,
    valueKind = valueKind.name,
    rewardRatePercent = rewardRatePercent,
    quarterStartDate = quarterStartDate,
    quarterEndDate = quarterEndDate,
    nextQuarterCategories = nextQuarterCategories,
    cadence = cadence.name,
    contextTags = contextTags.joinToString(",") { it.name },
    merchantCategories = merchantCategories.joinToString(",") { it.name },
    termsUrl = termsUrl,
    isRecurring = isRecurring,
    periodStartEpochDay = periodStartEpochDay,
    expiresAtEpochDay = expiresAtEpochDay,
    reminderLeadDays = reminderLeadDays,
)
