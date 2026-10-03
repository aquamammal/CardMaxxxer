package com.cardmaxxxer.domain.repository

import com.cardmaxxxer.domain.model.CardBenefit
import kotlinx.coroutines.flow.Flow

interface CardBenefitRepository {
    fun observeByCardId(cardId: String): Flow<List<CardBenefit>>
    fun observeAll(): Flow<List<CardBenefit>>
    fun observeById(id: String): Flow<CardBenefit?>
    suspend fun getById(id: String): CardBenefit?
    suspend fun getByCardId(cardId: String): List<CardBenefit>
    suspend fun getExpiringBetween(nowEpochDay: Long, lookaheadEpochDay: Long): List<CardBenefit>
    suspend fun getExpired(nowEpochDay: Long): List<CardBenefit>
    suspend fun insert(benefit: CardBenefit)
    suspend fun insertAll(benefits: List<CardBenefit>)
    suspend fun update(benefit: CardBenefit)
    suspend fun deleteById(id: String)
}
