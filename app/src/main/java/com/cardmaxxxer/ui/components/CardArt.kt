package com.cardmaxxxer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.CreditCard

/**
 * Realistic card art with detailed gradients, EMV chip, hologram, and issuer branding.
 */
@Composable
fun CardArt(
    card: CreditCard,
    modifier: Modifier = Modifier,
    showNetwork: Boolean = true,
) {
    val (gradient, accent) = when (card.issuer.lowercase()) {
        "american express", "amex" -> listOf(
            Color(0xFF1A5A9E), Color(0xFF2E77BC), Color(0xFF1A5A9E)
        ) to Color(0xFFD4AF37)
        "chase" -> listOf(
            Color(0xFF0A4A8A), Color(0xFF117ACA), Color(0xFF0A4A8A)
        ) to Color(0xFFFFFFFF)
        "citi" -> listOf(
            Color(0xFF002244), Color(0xFF003B70), Color(0xFF002244)
        ) to Color(0xFFD4AF37)
        "capital one" -> listOf(
            Color(0xFF002D4D), Color(0xFF004977), Color(0xFF002D4D)
        ) to Color(0xFFD4AF37)
        "wells fargo" -> listOf(
            Color(0xFF8B0000), Color(0xFFD71E2B), Color(0xFF8B0000)
        ) to Color(0xFFFFFFFF)
        "bank of america", "bofa" -> listOf(
            Color(0xFF8B0000), Color(0xFFD0001A), Color(0xFF8B0000)
        ) to Color(0xFFFFFFFF)
        "barclays" -> listOf(
            Color(0xFF0077B6), Color(0xFF00AEEF), Color(0xFF0077B6)
        ) to Color(0xFFFFFFFF)
        "discover" -> listOf(
            Color(0xFFCC4D00), Color(0xFFFF6000), Color(0xFFCC4D00)
        ) to Color(0xFFFFFFFF)
        "synchrony bank" -> listOf(
            Color(0xFF3D4F5F), Color(0xFF6B7B8D), Color(0xFF3D4F5F)
        ) to Color(0xFFD4AF37)
        else -> listOf(
            Color(0xFF0D47A1), Color(0xFF1A73E8), Color(0xFF0D47A1)
        ) to Color(0xFFD4AF37)
    }

    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.linearGradient(gradient))
            .padding(12.dp),
    ) {
        // Subtle radial highlight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.08f), Color.Transparent),
                        radius = 250f,
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                // EMV chip with contact pads
                Box(
                    modifier = Modifier
                        .size(width = 38.dp, height = 28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFD4AF37), Color(0xFFB8960C))
                            )
                        )
                        .padding(2.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF8B6914))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF8B6914))
                        )
                    }
                }
                if (showNetwork) {
                    NetworkLogo(network = card.network)
                }
            }

            // Card number
            Text(
                text = "•••• •••• •••• ${card.lastFour ?: "----"}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column {
                    Text(
                        text = card.issuer.uppercase(),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                    Text(
                        text = (card.nickname ?: card.productName).uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                // Contactless payment icon
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun NetworkLogo(network: CardNetwork) {
    val (label, bgColor, textColor) = when (network) {
        CardNetwork.VISA -> Triple("VISA", Color(0xFF1A1F71), Color.White)
        CardNetwork.MASTERCARD -> Triple("MC", Color(0xFFEB001B), Color.White)
        CardNetwork.AMEX -> Triple("AMEX", Color(0xFF2E77BC), Color.White)
        CardNetwork.DISCOVER -> Triple("DISC", Color(0xFFFF6000), Color.White)
        CardNetwork.OTHER -> Triple("CARD", Color(0xFF666666), Color.White)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
