package com.cardmaxxxer.work.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cardmaxxxer.domain.scheduling.PerkReminderScheduler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * BroadcastReceiver that reschedules perk reminders after a device reboot.
 *
 * Exact alarms scheduled via [AlarmManager] are cleared when the device reboots,
 * so this receiver re-enqueues the daily sweep periodic work which will
 * re-evaluate all expiring benefits and re-schedule their alarms.
 */
@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var perkReminderScheduler: PerkReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            perkReminderScheduler.scheduleDailySweep()
        }
    }
}
