package com.cardmaxxxer.data.repository

import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.data.local.mapper.toEntity
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditCardRepositoryImpl @Inject constructor(
    private val creditCardDao: CreditCardDao,
) : CreditCardRepository {

    override fun observeAllActive(): Flow<List<CreditCard>> =
        creditCardDao.observeAllActive().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: String): Flow<CreditCard?> =
        creditCardDao.observeById(id).map { it?.toDomain() }

    override suspend fun getById(id: String): CreditCard? =
        creditCardDao.getById(id)?.toDomain()

    override suspend fun insert(card: CreditCard) {
        creditCardDao.insert(card.toEntity())
    }

    override suspend fun update(card: CreditCard) {
        creditCardDao.update(card.toEntity())
    }

    override suspend fun deleteById(id: String) {
        creditCardDao.deleteById(id)
    }
}
