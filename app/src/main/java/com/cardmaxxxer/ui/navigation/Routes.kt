package com.cardmaxxxer.ui.navigation

object Routes {
    const val WALLET = "wallet"
    const val ADD_CARD = "add_card"
    const val CONTEXT_SELECTOR = "context_selector/{cardId}"
    const val PERK_DETAIL = "perk_detail/{benefitId}/{cardId}"
    const val UNUSED_VALUE = "unused_value"
    const val CARD_DETAIL = "card_detail/{cardId}"
    const val EDIT_CARD = "edit_card/{cardId}"

    fun contextSelector(cardId: String) = "context_selector/$cardId"
    fun perkDetail(benefitId: String, cardId: String) = "perk_detail/$benefitId/$cardId"
    fun cardDetail(cardId: String) = "card_detail/$cardId"
    fun editCard(cardId: String) = "edit_card/$cardId"
}
