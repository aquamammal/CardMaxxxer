package com.cardmaxxxer.domain.repository

import com.cardmaxxxer.domain.model.CardRecommendation
import com.cardmaxxxer.domain.model.SpendingContext
import kotlinx.coroutines.flow.Flow

interface RecommendationRepository {
    fun observeWalletCards(): Flow<List<com.cardmaxxxer.domain.model.WalletCard>>
    suspend fun getRecommendations(
        context: SpendingContext,
        todayEpochDay: Long,
        merchantCategory: com.cardmaxxxer.domain.model.MerchantCategory? = null,
        amountCents: Long? = null,
    ): List<CardRecommendation>
}
