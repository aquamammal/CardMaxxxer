package com.cardmaxxxer.core.di

import android.content.Context
import com.cardmaxxxer.core.database.CardMaxxxerDatabase
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.core.database.dao.PerkRedemptionLogDao
import com.cardmaxxxer.core.database.dao.UserCardCrossReferenceDao
import com.cardmaxxxer.core.security.DatabaseKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseKeyManager(@ApplicationContext context: Context): DatabaseKeyManager =
        DatabaseKeyManager(context)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        keyManager: DatabaseKeyManager,
    ): CardMaxxxerDatabase = CardMaxxxerDatabase.getInstance(context, keyManager)

    @Provides
    fun provideCreditCardDao(db: CardMaxxxerDatabase): CreditCardDao = db.creditCardDao()

    @Provides
    fun provideCardBenefitDao(db: CardMaxxxerDatabase): CardBenefitDao = db.cardBenefitDao()

    @Provides
    fun providePerkRedemptionLogDao(db: CardMaxxxerDatabase): PerkRedemptionLogDao =
        db.perkRedemptionLogDao()

    @Provides
    fun provideUserCardCrossReferenceDao(db: CardMaxxxerDatabase): UserCardCrossReferenceDao =
        db.userCardCrossReferenceDao()
}
