package com.cardmaxxxer.domain.repository

import com.cardmaxxxer.domain.model.PerkRedemptionLog
import kotlinx.coroutines.flow.Flow

interface PerkRedemptionRepository {
    fun observeByBenefitId(benefitId: String): Flow<List<PerkRedemptionLog>>
    fun observeByCardId(cardId: String): Flow<List<PerkRedemptionLog>>
    fun observeAll(): Flow<List<PerkRedemptionLog>>
    suspend fun getById(id: String): PerkRedemptionLog?
    suspend fun getByBenefitId(benefitId: String): List<PerkRedemptionLog>
    suspend fun insert(log: PerkRedemptionLog)
    suspend fun update(log: PerkRedemptionLog)
}
