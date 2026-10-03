package com.cardmaxxxer.ui.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.CreditCard
import com.cardmaxxxer.domain.model.UserCardCrossReference
import com.cardmaxxxer.domain.repository.CreditCardRepository
import com.cardmaxxxer.domain.repository.UserCardCrossReferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val creditCardRepository: CreditCardRepository,
    private val userCardCrossReferenceRepository: UserCardCrossReferenceRepository,
) : ViewModel() {

    fun addCard(
        issuer: String,
        productName: String,
        network: String,
        lastFour: String?,
        nickname: String?,
        annualFeeCents: Long,
        balanceCents: Long = 0L,
        foreignTransactionFeePercent: Double? = null,
    ) {
        viewModelScope.launch {
            val cardId = UUID.randomUUID().toString()
            val now = System.currentTimeMillis()

            val card = CreditCard(
                id = cardId,
                issuer = issuer,
                productName = productName,
                network = CardNetwork.valueOf(network),
                lastFour = lastFour,
                nickname = nickname,
                cardArtColor = defaultColorForIssuer(issuer),
                annualFeeCents = annualFeeCents,
                balanceCents = balanceCents,
                foreignTransactionFeePercent = foreignTransactionFeePercent,
                isActive = true,
                openedAtEpochDay = null,
                createdAt = now,
                updatedAt = now,
            )

            val crossRef = UserCardCrossReference(
                userId = "default_user",
                cardId = cardId,
                userNickname = nickname,
                priorityRank = 0,
                totalSavingsCents = 0L,
                lastUsedAt = null,
                isArchived = false,
                addedAt = now,
            )

            creditCardRepository.insert(card)
            userCardCrossReferenceRepository.insert(crossRef)
        }
    }

    private fun defaultColorForIssuer(issuer: String): Long {
        return when (issuer.lowercase()) {
            "chase" -> 0xFF117ACA
            "american express", "amex" -> 0xFF2E77BC
            "citi" -> 0xFF003B70
            "bank of america", "bofa" -> 0xFFD0001A
            "capital one" -> 0xFF004977
            "wells fargo" -> 0xFFD71E2B
            "barclays" -> 0xFF00AEEF
            "us bank" -> 0xFF0C2074
            "pnc" -> 0xFF0069AA
            "td bank" -> 0xFF34A853
            "schwab" -> 0xFF00A0DF
            "visa" -> 0xFF1A1F71
            "mastercard" -> 0xFFEB001B
            "discover" -> 0xFFFF6000
            else -> 0xFF1A73E8
        }
    }
}
