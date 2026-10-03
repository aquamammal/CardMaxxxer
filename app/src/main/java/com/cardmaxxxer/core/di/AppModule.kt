package com.cardmaxxxer.core.di

import com.cardmaxxxer.domain.engine.ContextCardRecommendationEngine
import com.cardmaxxxer.domain.engine.ScoringWeights
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideScoringWeights(): ScoringWeights = ScoringWeights()

    @Provides
    @Singleton
    fun provideContextCardRecommendationEngine(
        weights: ScoringWeights,
    ): ContextCardRecommendationEngine = ContextCardRecommendationEngine(weights)
}
