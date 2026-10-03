package com.cardmaxxxer.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {

    const val CHANNEL_PERKS = "perk_reminders"
    const val CHANNEL_GENERAL = "general"

    fun registerAll(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(NotificationManager::class.java) ?: return

        val perksChannel = NotificationChannel(
            CHANNEL_PERKS,
            "Perk Reminders",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Notifications for expiring and available perks"
            enableVibration(true)
        }

        val generalChannel = NotificationChannel(
            CHANNEL_GENERAL,
            "General",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "General notifications"
        }

        manager.createNotificationChannel(perksChannel)
        manager.createNotificationChannel(generalChannel)
    }
}
