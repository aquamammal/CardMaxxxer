package com.cardmaxxxer.data.local.mapper

import com.cardmaxxxer.data.local.entity.CardBenefitEntity
import com.cardmaxxxer.data.local.entity.CreditCardEntity
import com.cardmaxxxer.data.local.entity.PerkRedemptionLogEntity
import com.cardmaxxxer.data.local.entity.UserCardCrossReferenceEntity
import com.cardmaxxxer.domain.model.BenefitCadence
import com.cardmaxxxer.domain.model.BenefitCategory
import com.cardmaxxxer.domain.model.BenefitValueKind
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.model.MerchantCategory
import com.cardmaxxxer.domain.model.PerkRedemptionLog
import com.cardmaxxxer.domain.model.RedemptionStatus
import com.cardmaxxxer.domain.model.SpendingContext
import com.cardmaxxxer.domain.model.UserCardCrossReference

fun CreditCardEntity.toDomain(): CreditCard = CreditCard(
    id = id,
    issuer = issuer,
    productName = productName,
    network = CardNetwork.valueOf(network),
    lastFour = lastFour,
    nickname = nickname,
    cardArtColor = cardArtColor,
    annualFeeCents = annualFeeCents,
    foreignTransactionFeePercent = foreignTransactionFeePercent,
    isActive = isActive,
    openedAtEpochDay = openedAtEpochDay,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun CreditCard.toEntity(): CreditCardEntity = CreditCardEntity(
    id = id,
    issuer = issuer,
    productName = productName,
    network = network.name,
    lastFour = lastFour,
    nickname = nickname,
    cardArtColor = cardArtColor,
    annualFeeCents = annualFeeCents,
    foreignTransactionFeePercent = foreignTransactionFeePercent,
    isActive = isActive,
    openedAtEpochDay = openedAtEpochDay,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun CardBenefitEntity.toDomain(): CardBenefit = CardBenefit(
    id = id,
    cardId = cardId,
    title = title,
    description = description,
    category = BenefitCategory.valueOf(category),
    valueCents = valueCents,
    valueKind = BenefitValueKind.valueOf(valueKind),
    rewardRatePercent = rewardRatePercent,
    quarterStartDate = quarterStartDate,
    quarterEndDate = quarterEndDate,
    nextQuarterCategories = nextQuarterCategories,
    cadence = BenefitCadence.valueOf(cadence),
    contextTags = if (contextTags.isBlank()) emptyList()
    else contextTags.split(",").map { SpendingContext.valueOf(it) },
    merchantCategories = if (merchantCategories.isBlank()) emptyList()
    else merchantCategories.split(",").map { MerchantCategory.valueOf(it) },
    termsUrl = termsUrl,
    isRecurring = isRecurring,
    periodStartEpochDay = periodStartEpochDay,
    expiresAtEpochDay = expiresAtEpochDay,
    reminderLeadDays = reminderLeadDays,
)

fun CardBenefit.toEntity(): CardBenefitEntity = CardBenefitEntity(
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

fun PerkRedemptionLogEntity.toDomain(): PerkRedemptionLog = PerkRedemptionLog(
    id = id,
    benefitId = benefitId,
    cardId = cardId,
    status = RedemptionStatus.valueOf(status),
    redeemedAtEpochDay = redeemedAtEpochDay,
    amountSavedCents = amountSavedCents,
    note = note,
    periodStartEpochDay = periodStartEpochDay,
    createdAt = createdAt,
)

fun PerkRedemptionLog.toEntity(): PerkRedemptionLogEntity = PerkRedemptionLogEntity(
    id = id,
    benefitId = benefitId,
    cardId = cardId,
    status = status.name,
    redeemedAtEpochDay = redeemedAtEpochDay,
    amountSavedCents = amountSavedCents,
    note = note,
    periodStartEpochDay = periodStartEpochDay,
    createdAt = createdAt,
)

fun UserCardCrossReferenceEntity.toDomain(): UserCardCrossReference = UserCardCrossReference(
    userId = userId,
    cardId = cardId,
    userNickname = userNickname,
    priorityRank = priorityRank,
    totalSavingsCents = totalSavingsCents,
    lastUsedAt = lastUsedAt,
    isArchived = isArchived,
    addedAt = addedAt,
)

fun UserCardCrossReference.toEntity(): UserCardCrossReferenceEntity = UserCardCrossReferenceEntity(
    userId = userId,
    cardId = cardId,
    userNickname = userNickname,
    priorityRank = priorityRank,
    totalSavingsCents = totalSavingsCents,
    lastUsedAt = lastUsedAt,
    isArchived = isArchived,
    addedAt = addedAt,
)
