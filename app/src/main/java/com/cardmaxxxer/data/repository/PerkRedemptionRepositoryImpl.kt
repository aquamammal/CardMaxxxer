package com.cardmaxxxer.data.repository

import com.cardmaxxxer.core.database.dao.PerkRedemptionLogDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.data.local.mapper.toEntity
import com.cardmaxxxer.domain.model.PerkRedemptionLog
import com.cardmaxxxer.domain.repository.PerkRedemptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PerkRedemptionRepositoryImpl @Inject constructor(
    private val perkRedemptionLogDao: PerkRedemptionLogDao,
) : PerkRedemptionRepository {

    override fun observeByBenefitId(benefitId: String): Flow<List<PerkRedemptionLog>> =
        perkRedemptionLogDao.observeByBenefitId(benefitId).map { list -> list.map { it.toDomain() } }

    override fun observeByCardId(cardId: String): Flow<List<PerkRedemptionLog>> =
        perkRedemptionLogDao.observeByCardId(cardId).map { list -> list.map { it.toDomain() } }

    override fun observeAll(): Flow<List<PerkRedemptionLog>> =
        perkRedemptionLogDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: String): PerkRedemptionLog? =
        perkRedemptionLogDao.getById(id)?.toDomain()

    override suspend fun getByBenefitId(benefitId: String): List<PerkRedemptionLog> =
        perkRedemptionLogDao.getByBenefitId(benefitId).map { it.toDomain() }

    override suspend fun insert(log: PerkRedemptionLog) {
        perkRedemptionLogDao.insert(log.toEntity())
    }

    override suspend fun update(log: PerkRedemptionLog) {
        perkRedemptionLogDao.update(log.toEntity())
    }
}
