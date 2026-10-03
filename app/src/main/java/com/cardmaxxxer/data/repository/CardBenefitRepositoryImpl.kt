package com.cardmaxxxer.data.repository

import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.data.local.mapper.toEntity
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.repository.CardBenefitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardBenefitRepositoryImpl @Inject constructor(
    private val cardBenefitDao: CardBenefitDao,
) : CardBenefitRepository {

    override fun observeByCardId(cardId: String): Flow<List<CardBenefit>> =
        cardBenefitDao.observeByCardId(cardId).map { list -> list.map { it.toDomain() } }

    override fun observeAll(): Flow<List<CardBenefit>> =
        cardBenefitDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: String): Flow<CardBenefit?> =
        cardBenefitDao.observeById(id).map { it?.toDomain() }

    override suspend fun getById(id: String): CardBenefit? =
        cardBenefitDao.getById(id)?.toDomain()

    override suspend fun getByCardId(cardId: String): List<CardBenefit> =
        cardBenefitDao.getByCardId(cardId).map { it.toDomain() }

    override suspend fun getExpiringBetween(nowEpochDay: Long, lookaheadEpochDay: Long): List<CardBenefit> =
        cardBenefitDao.getExpiringBetween(nowEpochDay, lookaheadEpochDay).map { it.toDomain() }

    override suspend fun getExpired(nowEpochDay: Long): List<CardBenefit> =
        cardBenefitDao.getExpired(nowEpochDay).map { it.toDomain() }

    override suspend fun insert(benefit: CardBenefit) {
        cardBenefitDao.insert(benefit.toEntity())
    }

    override suspend fun insertAll(benefits: List<CardBenefit>) {
        cardBenefitDao.insertAll(benefits.map { it.toEntity() })
    }

    override suspend fun update(benefit: CardBenefit) {
        cardBenefitDao.update(benefit.toEntity())
    }

    override suspend fun deleteById(id: String) {
        cardBenefitDao.deleteById(id)
    }
}
