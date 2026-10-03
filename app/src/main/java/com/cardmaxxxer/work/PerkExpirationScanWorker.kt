package com.cardmaxxxer.work

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.domain.scheduling.PerkReminderScheduler
import com.cardmaxxxer.ui.widget.CardMaxxxerWidgetReceiver
import com.cardmaxxxer.work.receiver.PerkAlarmReceiver
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.ZoneId

/**
 * Worker that scans for benefits expiring within a lookahead window and schedules
 * exact alarms for each one. Also triggers a widget refresh so the home-screen
 * widget reflects the latest expiring-benefit data.
 *
 * This worker is enqueued by [PerkExpirationScheduler.scheduleDailySweep] as a
 * periodic daily task, and can also be enqueued on demand.
 */
@HiltWorker
class PerkExpirationScanWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cardBenefitDao: CardBenefitDao,
    private val perkReminderScheduler: PerkReminderScheduler,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val today = LocalDate.now()
            val todayEpochDay = today.toEpochDay()
            val lookaheadEpochDay = todayEpochDay + LOOKAHEAD_DAYS

            val expiringBenefits = cardBenefitDao.getExpiringBetween(todayEpochDay, lookaheadEpochDay)

            for (entity in expiringBenefits) {
                val benefit = entity.toDomain()
                benefit.expiresAtEpochDay?.let { expiry ->
                    val daysUntil = expiry - todayEpochDay
                    if (daysUntil in 0..benefit.reminderLeadDays) {
                        val triggerAt = calculateTriggerMillis(benefit, today)
                        perkReminderScheduler.scheduleReminder(benefit.id, triggerAt)
                    }
                }
            }

            updateWidget()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to scan expiring benefits", e)
            Result.retry()
        }
    }

    // -------------------------------------------------------------------------
    // Alarm scheduling
    // -------------------------------------------------------------------------

    /**
     * Calculates the trigger time for a benefit reminder.
     *
     * The reminder fires at the start of the day that is `reminderLeadDays` before
     * the benefit's expiration date. If that time has already passed, the current
     * time is used instead (immediate trigger).
     */
    private fun calculateTriggerMillis(
        benefit: com.cardmaxxxer.domain.model.CardBenefit,
        today: LocalDate,
    ): Long {
        val expiresAtEpochDay = benefit.expiresAtEpochDay ?: return System.currentTimeMillis()
        val reminderLeadDays = benefit.reminderLeadDays.toLong()

        val reminderDate = LocalDate.ofEpochDay(expiresAtEpochDay)
            .minusDays(reminderLeadDays)

        val reminderMillis = reminderDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return maxOf(reminderMillis, System.currentTimeMillis())
    }

    // -------------------------------------------------------------------------
    // Widget update
    // -------------------------------------------------------------------------

    /**
     * Sends an [AppWidgetManager.ACTION_APPWIDGET_UPDATE] broadcast to
     * [CardMaxxxerWidgetReceiver] so the widget refreshes with the latest data.
     */
    private fun updateWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val componentName = ComponentName(
            applicationContext,
            CardMaxxxerWidgetReceiver::class.java,
        )
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        if (appWidgetIds.isEmpty()) return

        val intent = Intent(applicationContext, CardMaxxxerWidgetReceiver::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        }
        applicationContext.sendBroadcast(intent)
    }

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    companion object {
        private const val TAG = "PerkExpirationScanWorker"

        /** Number of days ahead to look for expiring benefits. */
        const val LOOKAHEAD_DAYS = 30L
    }
}
