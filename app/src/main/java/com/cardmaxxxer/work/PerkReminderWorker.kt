package com.cardmaxxxer.work

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cardmaxxxer.MainActivity
import com.cardmaxxxer.R
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.core.notifications.NotificationChannels
import com.cardmaxxxer.data.local.mapper.toDomain
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Worker that posts a high-priority notification for a single expiring benefit.
 *
 * The benefit ID is passed via [KEY_BENEFIT_ID] in the input data. The worker
 * queries the benefit and its associated card, then builds and posts a
 * notification on the [NotificationChannels.CHANNEL_PERKS] channel.
 *
 * This worker is enqueued by [PerkAlarmReceiver] when an exact alarm fires.
 */
@HiltWorker
class PerkReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val cardBenefitDao: CardBenefitDao,
    private val creditCardDao: CreditCardDao,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val benefitId = inputData.getString(KEY_BENEFIT_ID) ?: return Result.failure()

        return try {
            val benefitEntity = cardBenefitDao.getById(benefitId)
                ?: return Result.failure()
            val benefit = benefitEntity.toDomain()

            val card = creditCardDao.getById(benefit.cardId)?.toDomain()
                ?: return Result.failure()

            postNotification(benefit, card)

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to post perk reminder for benefit $benefitId", e)
            Result.failure()
        }
    }

    // -------------------------------------------------------------------------
    // Notification
    // -------------------------------------------------------------------------

    private fun postNotification(
        benefit: com.cardmaxxxer.domain.model.CardBenefit,
        card: com.cardmaxxxer.domain.model.CreditCard,
    ) {
        // On Android 13+ (TIRAMISU) we need the POST_NOTIFICATIONS runtime permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w(TAG, "POST_NOTIFICATIONS permission not granted; skipping notification")
                return
            }
        }

        val daysLeft = benefit.expiresAtEpochDay?.let {
            ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.ofEpochDay(it))
        } ?: 0L

        val title = applicationContext.getString(R.string.perk_expiring_title)
        val message = applicationContext.getString(
            R.string.perk_expiring_message,
            benefit.title,
            card.nickname ?: card.productName,
            daysLeft,
        )

        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            benefit.id.hashCode(),
            Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(EXTRA_BENEFIT_ID, benefit.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            NotificationChannels.CHANNEL_PERKS,
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(
            benefit.id.hashCode(),
            notification,
        )
    }

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    companion object {
        private const val TAG = "PerkReminderWorker"

        /** Input-data key for the benefit ID (String). */
        const val KEY_BENEFIT_ID = "benefit_id"

        /** Intent extra key for the benefit ID when launching [MainActivity]. */
        const val EXTRA_BENEFIT_ID = "extra_benefit_id"
    }
}
