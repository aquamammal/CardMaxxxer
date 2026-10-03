package com.cardmaxxxer

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.cardmaxxxer.core.notifications.NotificationChannels
import com.cardmaxxxer.data.seed.SeedDataLoader
import com.cardmaxxxer.domain.scheduling.PerkReminderScheduler
import com.cardmaxxxer.work.PerkExpirationScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class CardMaxxxerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var perkReminderScheduler: PerkReminderScheduler

    @Inject
    lateinit var seedDataLoader: SeedDataLoader

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        loadNativeLibraries()
        NotificationChannels.registerAll(this)
        perkReminderScheduler.scheduleDailySweep()
        appScope.launch {
            seedDataLoader.seedIfNeeded()
        }
    }

    private fun loadNativeLibraries() {
        runCatching { System.loadLibrary("sqlcipher") }
            .onFailure { Log.w(TAG, "SQLCipher native library preload failed; the open-helper factory will retry", it) }
    }

    private companion object {
        const val TAG = "CardMaxxxerApp"
    }
}
