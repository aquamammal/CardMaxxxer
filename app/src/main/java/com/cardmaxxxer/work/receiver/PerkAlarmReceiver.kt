package com.cardmaxxxer.work.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cardmaxxxer.work.PerkExpirationScheduler
import com.cardmaxxxer.work.PerkReminderWorker
import dagger.hilt.android.AndroidEntryPoint

/**
 * BroadcastReceiver that fires when an exact alarm scheduled by
 * [PerkExpirationScheduler] triggers.
 *
 * The receiver extracts the benefit ID from the intent and enqueues a
 * [PerkReminderWorker] to post the notification.
 */
@AndroidEntryPoint
class PerkAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val benefitId = intent.getStringExtra(PerkExpirationScheduler.EXTRA_BENEFIT_ID)
            ?: return

        val inputData = Data.Builder()
            .putString(PerkReminderWorker.KEY_BENEFIT_ID, benefitId)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<PerkReminderWorker>()
            .setInputData(inputData)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private companion object {
        const val WORK_TAG = "perk_reminder"
    }
}
