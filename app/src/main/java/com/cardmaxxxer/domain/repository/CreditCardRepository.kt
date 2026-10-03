package com.cardmaxxxer.domain.repository

import com.cardmaxxxer.domain.model.CreditCard
import kotlinx.coroutines.flow.Flow

interface CreditCardRepository {
    fun observeAllActive(): Flow<List<CreditCard>>
    fun observeById(id: String): Flow<CreditCard?>
    suspend fun getById(id: String): CreditCard?
    suspend fun insert(card: CreditCard)
    suspend fun update(card: CreditCard)
    suspend fun deleteById(id: String)
}
