package com.cardmaxxxer.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.data.local.mapper.toDomain
import com.cardmaxxxer.domain.model.CardBenefit
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.ui.components.formatDollars
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CardMaxxxerWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java,
        )
        val cardBenefitDao = entryPoint.cardBenefitDao()
        val creditCardDao = entryPoint.creditCardDao()

        val today = LocalDate.now().toEpochDay()
        val lookahead = today + 30
        val expiringBenefits = cardBenefitDao.getExpiringBetween(today, lookahead)
        val allBenefits = cardBenefitDao.observeAll()
        val cards = creditCardDao.observeAll()

        // Collect flows
        val benefitsList = mutableListOf<CardBenefit>()
        allBenefits.collect { entities ->
            benefitsList.clear()
            benefitsList.addAll(entities.map { it.toDomain() })
        }

        val cardsList = mutableListOf<CreditCard>()
        cards.collect { entities ->
            cardsList.clear()
            cardsList.addAll(entities.map { it.toDomain() })
        }

        val totalUnused = benefitsList.sumOf { it.valueCents ?: 0L }
        val expiringList = expiringBenefits.map { it.toDomain() }

        provideContent {
            WidgetContent(
                expiringBenefits = expiringList,
                totalUnusedCents = totalUnused,
                cards = cardsList,
            )
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun cardBenefitDao(): CardBenefitDao
        fun creditCardDao(): CreditCardDao
    }
}

@Composable
private fun WidgetContent(
    expiringBenefits: List<CardBenefit>,
    totalUnusedCents: Long,
    cards: List<CreditCard>,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(ColorProvider(android.graphics.Color.WHITE))
            .cornerRadius(16.dp)
            .padding(16.dp)
            .clickable(actionRunCallback<OpenAppAction>()),
    ) {
        Text(
            text = "CardMaxxxer",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorProvider(android.graphics.Color.BLACK),
            ),
        )
        Spacer(GlanceModifier.height(8.dp))

        Text(
            text = "Total Unused: ${formatDollars(totalUnusedCents)}",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ColorProvider(android.graphics.Color.BLACK),
            ),
        )
        Spacer(GlanceModifier.height(12.dp))

        if (expiringBenefits.isEmpty()) {
            Text(
                text = "No expiring perks",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(android.graphics.Color.GRAY),
                ),
            )
        } else {
            expiringBenefits.take(3).forEach { benefit ->
                val daysLeft = benefit.expiresAtEpochDay?.let {
                    ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.ofEpochDay(it))
                } ?: 0
                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = benefit.title,
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = ColorProvider(android.graphics.Color.BLACK),
                        ),
                        modifier = GlanceModifier.defaultWeight(),
                    )
                    Text(
                        text = "${daysLeft}d",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(android.graphics.Color.RED),
                        ),
                    )
                }
            }
            if (expiringBenefits.size > 3) {
                Text(
                    text = "+${expiringBenefits.size - 3} more",
                    style = TextStyle(
                        fontSize = 11.sp,
                        color = ColorProvider(android.graphics.Color.GRAY),
                    ),
                )
            }
        }
    }
}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        // Open the main activity
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.let { context.startActivity(it) }
    }
}
