package com.cardmaxxxer.domain.scheduling

interface PerkReminderScheduler {
    fun scheduleReminder(benefitId: String, triggerAtMillis: Long)
    fun cancelReminder(benefitId: String)
    fun scheduleDailySweep()
    fun cancelAll()
}
