package com.cardmaxxxer.core.di

import com.cardmaxxxer.data.repository.CardBenefitRepositoryImpl
import com.cardmaxxxer.data.repository.CreditCardRepositoryImpl
import com.cardmaxxxer.data.repository.PerkRedemptionRepositoryImpl
import com.cardmaxxxer.data.repository.RecommendationRepositoryImpl
import com.cardmaxxxer.data.repository.UserCardCrossReferenceRepositoryImpl
import com.cardmaxxxer.domain.repository.CardBenefitRepository
import com.cardmaxxxer.domain.repository.CreditCardRepository
import com.cardmaxxxer.domain.repository.PerkRedemptionRepository
import com.cardmaxxxer.domain.repository.RecommendationRepository
import com.cardmaxxxer.domain.repository.UserCardCrossReferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCreditCardRepository(impl: CreditCardRepositoryImpl): CreditCardRepository

    @Binds
    @Singleton
    abstract fun bindCardBenefitRepository(impl: CardBenefitRepositoryImpl): CardBenefitRepository

    @Binds
    @Singleton
    abstract fun bindPerkRedemptionRepository(impl: PerkRedemptionRepositoryImpl): PerkRedemptionRepository

    @Binds
    @Singleton
    abstract fun bindRecommendationRepository(impl: RecommendationRepositoryImpl): RecommendationRepository

    @Binds
    @Singleton
    abstract fun bindUserCardCrossReferenceRepository(impl: UserCardCrossReferenceRepositoryImpl): UserCardCrossReferenceRepository
}
