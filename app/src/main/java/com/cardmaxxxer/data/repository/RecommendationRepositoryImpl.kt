package com.cardmaxxxer.data.repository

import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.core.database.dao.PerkRedemptionLogDao
import com.cardmaxxxer.core.database.dao.UserCardCrossReferenceDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.domain.engine.ContextCardRecommendationEngine
import com.cardmaxxxer.domain.engine.RecommendationInput
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.CardRecommendation
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.model.PerkRedemptionLog
import com.cardmaxxxer.domain.model.SpendingContext
import com.cardmaxxxer.domain.model.UserCardCrossReference
import com.cardmaxxxer.domain.model.WalletCard
import com.cardmaxxxer.domain.repository.RecommendationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepositoryImpl @Inject constructor(
    private val creditCardDao: CreditCardDao,
    private val cardBenefitDao: CardBenefitDao,
    private val perkRedemptionLogDao: PerkRedemptionLogDao,
    private val userCardCrossReferenceDao: UserCardCrossReferenceDao,
    private val recommendationEngine: ContextCardRecommendationEngine,
) : RecommendationRepository {

    override fun observeWalletCards(): Flow<List<WalletCard>> {
        val defaultUserId = "default_user"

        return combine(
            creditCardDao.observeAllActive(),
            cardBenefitDao.observeAll(),
            perkRedemptionLogDao.observeAll(),
            userCardCrossReferenceDao.observeByUser(defaultUserId),
        ) { cards, benefits, redemptions, crossRefs ->
            val benefitsByCard = benefits.groupBy { it.cardId }
            val redemptionsByBenefit = redemptions.groupBy { it.benefitId }
            val crossRefByCardId = crossRefs.associateBy { it.cardId }

            cards.map { card ->
                val cardBenefits = benefitsByCard[card.id].orEmpty()
                val crossRef = crossRefByCardId[card.id]
                val domainBenefits = cardBenefits.map { it.toDomain() }
                val domainRedemptions = redemptionsByBenefit.mapValues { (_, logs) -> logs.map { it.toDomain() } }
                val unusedValue = computeUnusedValue(domainBenefits, domainRedemptions)
                val expiringCount = countExpiringSoon(cardBenefits.map { it.toDomain() })

                WalletCard(
                    card = card.toDomain(),
                    crossReference = crossRef?.toDomain(),
                    benefits = cardBenefits.map { it.toDomain() },
                    unusedValueCents = unusedValue,
                    expiringSoonCount = expiringCount,
                )
            }
        }
    }

    override suspend fun getRecommendations(
        context: SpendingContext,
        todayEpochDay: Long,
        merchantCategory: com.cardmaxxxer.domain.model.MerchantCategory?,
        amountCents: Long?,
    ): List<CardRecommendation> {
        val defaultUserId = "default_user"
        val cards = creditCardDao.observeAllActive().first().map { it.toDomain() }
        val benefits = cardBenefitDao.observeAll().first().map { it.toDomain() }
        val redemptions = perkRedemptionLogDao.observeAll().first().map { it.toDomain() }
        val crossRefs = userCardCrossReferenceDao.observeByUser(defaultUserId).first().map { it.toDomain() }

        val input = RecommendationInput(
            context = context,
            todayEpochDay = todayEpochDay,
            cards = cards,
            benefits = benefits,
            redemptions = redemptions,
            merchantCategory = merchantCategory,
            amountCents = amountCents,
        )

        val recommendations = recommendationEngine.recommend(input)
        val crossRefByCardId = crossRefs.associateBy { it.cardId }

        return recommendations.map { rec ->
            rec.copy(crossReference = crossRefByCardId[rec.card.id])
        }
    }

    private fun computeUnusedValue(
        benefits: List<CardBenefit>,
        redemptionsByBenefit: Map<String, List<PerkRedemptionLog>>,
    ): Long {
        return benefits.sumOf { benefit ->
            // Only count redeemable benefits (statement credits, free goods) as unused value.
            // Insurance coverage amounts and point multipliers are not redeemable credits.
            if (benefit.valueKind != com.cardmaxxxer.domain.model.BenefitValueKind.STATEMENT_CREDIT &&
                benefit.valueKind != com.cardmaxxxer.domain.model.BenefitValueKind.FREE_GOODS
            ) return@sumOf 0L
            val total = benefit.valueCents ?: 0L
            val redeemed = redemptionsByBenefit[benefit.id]
                ?.filter { it.status == com.cardmaxxxer.domain.model.RedemptionStatus.FULLY_USED }
                ?.sumOf { it.amountSavedCents } ?: 0L
            maxOf(0L, total - redeemed)
        }
    }

    private fun countExpiringSoon(benefits: List<CardBenefit>): Int {
        val today = java.time.LocalDate.now().toEpochDay()
        return benefits.count { benefit ->
            benefit.expiresAtEpochDay?.let { expiry ->
                val daysUntil = expiry - today
                daysUntil in 0..14
            } == true
        }
    }
}
