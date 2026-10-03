package com.cardmaxxxer.core.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.cardmaxxxer.domain.scheduling.PerkReminderScheduler
import com.cardmaxxxer.work.PerkExpirationScheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @Singleton
    abstract fun bindPerkReminderScheduler(
        impl: PerkExpirationScheduler,
    ): PerkReminderScheduler

    companion object {

        @Provides
        @Singleton
        fun provideApplicationContext(
            @ApplicationContext context: Context,
        ): Context = context

        @Provides
        @Singleton
        fun provideWorkManagerConfiguration(
            workerFactory: HiltWorkerFactory,
        ): Configuration = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}
