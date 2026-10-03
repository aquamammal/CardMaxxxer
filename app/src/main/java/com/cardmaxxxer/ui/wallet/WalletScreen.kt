package com.cardmaxxxer.ui.wallet

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cardmaxxxer.domain.model.CardNetwork
import com.cardmaxxxer.domain.model.WalletCard
import com.cardmaxxxer.ui.components.CardArt
import com.cardmaxxxer.ui.components.formatDollars

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WalletScreen(
    onCardClick: (String) -> Unit,
    onAddCard: () -> Unit,
    onUnusedValueClick: () -> Unit = {},
    viewModel: WalletViewModel = hiltViewModel(),
) {
    val cards by viewModel.filteredCards.collectAsState()
    val allCards by viewModel.walletCards.collectAsState()
    val totalUnused by viewModel.totalUnusedValueCents.collectAsState()
    val totalExpiring by viewModel.totalExpiringSoon.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val availableIssuers by viewModel.availableIssuers.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wallet") },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = if (filter != WalletFilter()) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCard) {
                Icon(Icons.Default.Add, contentDescription = "Add Card")
            }
        },
    ) { padding ->
        if (allCards.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    SummaryHeader(
                        totalUnusedCents = totalUnused,
                        expiringCount = totalExpiring,
                        onClick = { onUnusedValueClick() },
                    )
                }
                if (filter != WalletFilter()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${cards.size} of ${allCards.size} cards",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            IconButton(onClick = { viewModel.updateFilter(WalletFilter()) }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear filters",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
                itemsIndexed(cards, key = { _, item -> item.card.id }) { index, walletCard ->
                    WalletCardItem(
                        walletCard = walletCard,
                        isFirst = index == 0,
                        isLast = index == cards.size - 1,
                        onClick = { onCardClick(walletCard.card.id) },
                        onMoveUp = { viewModel.moveCard(walletCard.card.id, -1) },
                        onMoveDown = { viewModel.moveCard(walletCard.card.id, 1) },
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            filter = filter,
            availableIssuers = availableIssuers,
            onFilterChange = { viewModel.updateFilter(it) },
            onDismiss = { showFilterSheet = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    filter: WalletFilter,
    availableIssuers: List<String>,
    onFilterChange: (WalletFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    var issuerExpanded by remember { mutableStateOf(false) }
    var annualFeeExpanded by remember { mutableStateOf(false) }
    var ftfExpanded by remember { mutableStateOf(false) }
    var networkExpanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Filter Cards",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            // Issuer dropdown
            ExposedDropdownMenuBox(
                expanded = issuerExpanded,
                onExpandedChange = { issuerExpanded = it },
            ) {
                OutlinedTextField(
                    value = filter.issuer ?: "All Issuers",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Issuer") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = issuerExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = issuerExpanded,
                    onDismissRequest = { issuerExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("All Issuers") },
                        onClick = {
                            onFilterChange(filter.copy(issuer = null))
                            issuerExpanded = false
                        },
                    )
                    availableIssuers.forEach { issuer ->
                        DropdownMenuItem(
                            text = { Text(issuer) },
                            onClick = {
                                onFilterChange(filter.copy(issuer = issuer))
                                issuerExpanded = false
                            },
                        )
                    }
                }
            }

            // Annual fee dropdown
            ExposedDropdownMenuBox(
                expanded = annualFeeExpanded,
                onExpandedChange = { annualFeeExpanded = it },
            ) {
                OutlinedTextField(
                    value = when (filter.hasAnnualFee) {
                        null -> "All"
                        true -> "Has Annual Fee"
                        false -> "No Annual Fee"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Annual Fee") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = annualFeeExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = annualFeeExpanded,
                    onDismissRequest = { annualFeeExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            onFilterChange(filter.copy(hasAnnualFee = null))
                            annualFeeExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Has Annual Fee") },
                        onClick = {
                            onFilterChange(filter.copy(hasAnnualFee = true))
                            annualFeeExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("No Annual Fee") },
                        onClick = {
                            onFilterChange(filter.copy(hasAnnualFee = false))
                            annualFeeExpanded = false
                        },
                    )
                }
            }

            // Foreign transaction fee dropdown
            ExposedDropdownMenuBox(
                expanded = ftfExpanded,
                onExpandedChange = { ftfExpanded = it },
            ) {
                OutlinedTextField(
                    value = when (filter.hasForeignTransactionFee) {
                        null -> "All"
                        true -> "Has FTF"
                        false -> "No FTF"
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Foreign Transaction Fee") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ftfExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = ftfExpanded,
                    onDismissRequest = { ftfExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            onFilterChange(filter.copy(hasForeignTransactionFee = null))
                            ftfExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Has FTF") },
                        onClick = {
                            onFilterChange(filter.copy(hasForeignTransactionFee = true))
                            ftfExpanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("No FTF") },
                        onClick = {
                            onFilterChange(filter.copy(hasForeignTransactionFee = false))
                            ftfExpanded = false
                        },
                    )
                }
            }

            // Network dropdown
            ExposedDropdownMenuBox(
                expanded = networkExpanded,
                onExpandedChange = { networkExpanded = it },
            ) {
                OutlinedTextField(
                    value = filter.network ?: "All Networks",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Network") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = networkExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                )
                ExposedDropdownMenu(
                    expanded = networkExpanded,
                    onDismissRequest = { networkExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("All Networks") },
                        onClick = {
                            onFilterChange(filter.copy(network = null))
                            networkExpanded = false
                        },
                    )
                    CardNetwork.entries.forEach { network ->
                        DropdownMenuItem(
                            text = { Text(network.name) },
                            onClick = {
                                onFilterChange(filter.copy(network = network.name))
                                networkExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(onClick = { onFilterChange(WalletFilter()) }) {
                    Text("Clear All")
                }
                Button(onClick = onDismiss) {
                    Text("Done")
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SummaryHeader(
    totalUnusedCents: Long,
    expiringCount: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Unused Value",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = formatDollars(totalUnusedCents),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            if (expiringCount > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$expiringCount expiring soon",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tap to see breakdown →",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WalletCardItem(
    walletCard: WalletCard,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
) {
    val card = walletCard.card
    val benefits = walletCard.benefits
    val usedValue = benefits.sumOf { it.valueCents ?: 0 } - walletCard.unusedValueCents
    val totalValue = benefits.sumOf { it.valueCents ?: 0 }
    val progress = if (totalValue > 0) usedValue.toFloat() / totalValue.toFloat() else 0f
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CardArt(
                    card = card,
                    modifier = Modifier.size(width = 80.dp, height = 50.dp),
                )
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = card.nickname ?: card.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${card.issuer} •••• ${card.lastFour ?: "----"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    card.foreignTransactionFeePercent?.let { fee ->
                        if (fee > 0) {
                            Text(
                                text = "Foreign transaction fee: ${fee}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                        } else {
                            Text(
                                text = "No foreign transaction fees",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Text(
                        text = if (card.annualFeeCents > 0) "Annual fee: ${formatDollars(card.annualFeeCents)}" else "No annual fee",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (card.annualFeeCents > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "${benefits.size} benefits",
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = "${formatDollars(walletCard.unusedValueCents)} unused",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
        // Drag handle - separate from clickable Card
        val draggableState = rememberDraggableState { delta ->
            dragOffset += delta
            if (dragOffset < -20 && !isFirst) {
                onMoveUp()
                dragOffset = 0f
            } else if (dragOffset > 20 && !isLast) {
                onMoveDown()
                dragOffset = 0f
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Vertical,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.DragHandle,
                contentDescription = "Drag to reorder",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No cards yet",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Add a credit card to start tracking benefits",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
