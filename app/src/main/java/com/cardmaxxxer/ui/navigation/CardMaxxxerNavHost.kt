package com.cardmaxxxer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cardmaxxxer.ui.addcard.AddCardScreen
import com.cardmaxxxer.ui.carddetail.CardDetailScreen
import com.cardmaxxxer.ui.context.ContextSelectorScreen
import com.cardmaxxxer.ui.editcard.EditCardScreen
import com.cardmaxxxer.ui.perkdetail.PerkDetailScreen
import com.cardmaxxxer.ui.wallet.UnusedValueScreen
import com.cardmaxxxer.ui.wallet.WalletScreen

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(Routes.WALLET, "Wallet", Icons.Default.AccountBalanceWallet),
    BottomNavItem("context", "Context", Icons.Default.Tune),
)

@Composable
fun CardMaxxxerNavHost(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.WALLET,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.WALLET) {
                WalletScreen(
                    onCardClick = { cardId ->
                        navController.navigate(Routes.cardDetail(cardId))
                    },
                    onAddCard = {
                        navController.navigate(Routes.ADD_CARD)
                    },
                    onUnusedValueClick = {
                        navController.navigate(Routes.UNUSED_VALUE)
                    },
                )
            }

            composable(Routes.UNUSED_VALUE) {
                UnusedValueScreen(
                    onBack = { navController.popBackStack() },
                    onCardClick = { cardId ->
                        navController.navigate(Routes.cardDetail(cardId))
                    },
                )
            }

            composable(
                route = Routes.CARD_DETAIL,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                CardDetailScreen(
                    cardId = cardId,
                    onBack = { navController.popBackStack() },
                    onBenefitClick = { benefitId, cardId ->
                        navController.navigate(Routes.perkDetail(benefitId, cardId))
                    },
                    onEditClick = { cardId ->
                        navController.navigate(Routes.editCard(cardId))
                    },
                )
            }

            composable(
                route = Routes.EDIT_CARD,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                EditCardScreen(
                    cardId = cardId,
                    onBack = { navController.popBackStack() },
                )
            }

            composable(Routes.ADD_CARD) {
                AddCardScreen(
                    onBack = { navController.popBackStack() },
                )
            }

            composable("context") {
                ContextSelectorScreen(
                    cardId = null,
                    onBack = { navController.popBackStack() },
                    onPerkClick = { benefitId, _ ->
                        navController.navigate(Routes.perkDetail(benefitId, benefitId))
                    },
                )
            }

            composable(
                route = Routes.CONTEXT_SELECTOR,
                arguments = listOf(navArgument("cardId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                ContextSelectorScreen(
                    cardId = cardId,
                    onBack = { navController.popBackStack() },
                    onPerkClick = { benefitId, cardId ->
                        navController.navigate(Routes.perkDetail(benefitId, cardId))
                    },
                )
            }

            composable(
                route = Routes.PERK_DETAIL,
                arguments = listOf(
                    navArgument("benefitId") { type = NavType.StringType },
                    navArgument("cardId") { type = NavType.StringType },
                ),
            ) { backStackEntry ->
                val benefitId = backStackEntry.arguments?.getString("benefitId") ?: return@composable
                val cardId = backStackEntry.arguments?.getString("cardId") ?: return@composable
                PerkDetailScreen(
                    benefitId = benefitId,
                    cardId = cardId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
