package com.cardmaxxxer.work

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cardmaxxxer.domain.scheduling.PerkReminderScheduler
import com.cardmaxxxer.work.receiver.PerkAlarmReceiver
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of [PerkReminderScheduler] that uses [AlarmManager] for exact
 * per-benefit reminders and [WorkManager] for the daily sweep that re-evaluates all
 * expiring benefits.
 *
 * Exact alarms are scheduled with [AlarmManager.setExactAndAllowWhileIdle] on API 23+.
 * On API 31+ (S), if the user has not granted the `SCHEDULE_EXACT_ALARM` permission, the
 * implementation falls back to [AlarmManager.setAndAllowWhileIdle] which is still
 * relatively precise but allows the system to batch alarms.
 */
@Singleton
class PerkExpirationScheduler @Inject constructor(
    private val context: Context,
) : PerkReminderScheduler {

    private val alarmManager: AlarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private val workManager: WorkManager by lazy {
        WorkManager.getInstance(context)
    }

    // -------------------------------------------------------------------------
    // PerkReminderScheduler
    // -------------------------------------------------------------------------

    override fun scheduleReminder(benefitId: String, triggerAtMillis: Long) {
        val pendingIntent = createReminderPendingIntent(benefitId)

        // Cancel any existing alarm for this benefit before scheduling a new one.
        alarmManager.cancel(pendingIntent)

        scheduleExactAlarm(triggerAtMillis, pendingIntent)
    }

    override fun cancelReminder(benefitId: String) {
        val pendingIntent = createReminderPendingIntent(benefitId)
        alarmManager.cancel(pendingIntent)
    }

    override fun scheduleDailySweep() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<PerkExpirationScanWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS,
        )
            .setConstraints(constraints)
            .addTag(WORK_TAG_DAILY_SWEEP)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_DAILY_SWEEP,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest,
        )
    }

    override fun cancelAll() {
        // Cancel the daily sweep periodic work.
        workManager.cancelUniqueWork(WORK_NAME_DAILY_SWEEP)

        // Cancel any one-time perk reminder work that may still be enqueued.
        workManager.cancelAllWorkByTag(WORK_TAG_PERK)
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private fun createReminderPendingIntent(benefitId: String): PendingIntent {
        val intent = Intent(context, PerkAlarmReceiver::class.java).apply {
            action = ACTION_PERK_REMINDER
            putExtra(EXTRA_BENEFIT_ID, benefitId)
        }

        return PendingIntent.getBroadcast(
            context,
            benefitId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    /**
     * Schedules an exact alarm, falling back to an inexact alarm when the app does not
     * have permission to schedule exact alarms (API 31+).
     */
    private fun scheduleExactAlarm(triggerAtMillis: Long, pendingIntent: PendingIntent) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent,
                    )
                } else {
                    Log.w(
                        TAG,
                        "Exact alarms not permitted; falling back to inexact alarm for benefit reminder",
                    )
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent,
                    )
                }
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            }

            else -> {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent,
                )
            }
        }
    }

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    companion object {
        private const val TAG = "PerkExpirationScheduler"

        /** Intent action fired by [AlarmManager] when a perk reminder is due. */
        const val ACTION_PERK_REMINDER = "com.cardmaxxxer.action.PERK_REMINDER"

        /** Intent extra key for the benefit ID (String). */
        const val EXTRA_BENEFIT_ID = "benefit_id"

        /** Unique work name for the daily sweep periodic work. */
        const val WORK_NAME_DAILY_SWEEP = "perk_daily_sweep"

        /** Tag applied to the daily sweep periodic work request. */
        const val WORK_TAG_DAILY_SWEEP = "perk_daily_sweep_tag"

        /** Tag applied to all perk-related one-time work requests. */
        const val WORK_TAG_PERK = "perk_work_tag"
    }
}
