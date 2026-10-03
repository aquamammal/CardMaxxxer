package com.cardmaxxxer.ui.addcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cardmaxxxer.data.seed.CardCatalog
import com.cardmaxxxer.domain.model.CardNetwork

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    viewModel: AddCardViewModel = hiltViewModel(),
) {
    var issuer by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var selectedNetwork by remember { mutableStateOf(CardNetwork.VISA) }
    var networkDropdownExpanded by remember { mutableStateOf(false) }
    var lastFour by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var annualFee by remember { mutableStateOf("") }
    var foreignTransactionFee by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var catalogDropdownExpanded by remember { mutableStateOf(false) }
    var selectedCatalogCard by remember { mutableStateOf<CardCatalog.CardSpec?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Card") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Catalog dropdown - auto-fills all fields
            Text(
                text = "Quick Add from Catalog",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            ExposedDropdownMenuBox(
                expanded = catalogDropdownExpanded,
                onExpandedChange = { catalogDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = selectedCatalogCard?.let { "${it.issuer} - ${it.productName}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select a card to auto-fill") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = catalogDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                )
                ExposedDropdownMenu(
                    expanded = catalogDropdownExpanded,
                    onDismissRequest = { catalogDropdownExpanded = false },
                ) {
                    CardCatalog.cards.forEach { spec ->
                        DropdownMenuItem(
                            text = { Text("${spec.issuer} - ${spec.productName}") },
                            onClick = {
                                selectedCatalogCard = spec
                                issuer = spec.issuer
                                productName = spec.productName
                                selectedNetwork = spec.network
                                annualFee = if (spec.annualFeeCents > 0) spec.annualFeeCents.toString() else ""
                                foreignTransactionFee = spec.foreignTransactionFeePercent?.toString() ?: ""
                                catalogDropdownExpanded = false
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "— or enter manually —",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = issuer,
                onValueChange = { issuer = it },
                label = { Text("Issuer") },
                placeholder = { Text("e.g. Chase, Amex") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Product Name") },
                placeholder = { Text("e.g. Sapphire Preferred") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            ExposedDropdownMenuBox(
                expanded = networkDropdownExpanded,
                onExpandedChange = { networkDropdownExpanded = it },
            ) {
                OutlinedTextField(
                    value = selectedNetwork.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Network") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = networkDropdownExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                )
                ExposedDropdownMenu(
                    expanded = networkDropdownExpanded,
                    onDismissRequest = { networkDropdownExpanded = false },
                ) {
                    CardNetwork.entries.forEach { network ->
                        DropdownMenuItem(
                            text = { Text(network.name) },
                            onClick = {
                                selectedNetwork = network
                                networkDropdownExpanded = false
                            },
                        )
                    }
                }
            }

            OutlinedTextField(
                value = lastFour,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                        lastFour = newValue
                    }
                },
                label = { Text("Last Four (optional)") },
                placeholder = { Text("1234") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Nickname (optional)") },
                placeholder = { Text("e.g. My Travel Card") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = annualFee,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        annualFee = newValue
                    }
                },
                label = { Text("Annual Fee in cents (optional)") },
                placeholder = { Text("e.g. 9500 for $95") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            OutlinedTextField(
                value = foreignTransactionFee,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                        foreignTransactionFee = newValue
                    }
                },
                label = { Text("Foreign Transaction Fee % (optional)") },
                placeholder = { Text("e.g. 3.0") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )

            OutlinedTextField(
                value = balance,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        balance = newValue
                    }
                },
                label = { Text("Balance in cents (optional)") },
                placeholder = { Text("e.g. 15000 for $150") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.addCard(
                        issuer = issuer.trim(),
                        productName = productName.trim(),
                        network = selectedNetwork.name,
                        lastFour = lastFour.trim().ifBlank { null },
                        nickname = nickname.trim().ifBlank { null },
                        annualFeeCents = annualFee.trim().toLongOrNull() ?: 0L,
                        balanceCents = balance.trim().toLongOrNull() ?: 0L,
                        foreignTransactionFeePercent = foreignTransactionFee.trim().toDoubleOrNull(),
                    )
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = issuer.isNotBlank() && productName.isNotBlank(),
            ) {
                Text("Add Card")
            }
        }
    }
}
