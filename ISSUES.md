# Bug Report: Card Reorder Not Working

## Summary
The up/down arrow buttons on wallet cards do not reorder the cards. Tapping them has no visible effect.

## Environment
- App: CardMaxxxer (Android, Kotlin + Jetpack Compose)
- Device: Samsung SM-N975U1 (Android 12)
- Repo: https://github.com/aquamammal/CardMaxxxer
- Branch: master
- Last commit: 181146c

## Steps to Reproduce
1. Open the app — wallet shows cards
2. Tap the ↑ (up) arrow on any card that is not the first card
3. Observe: nothing happens, card order does not change
4. Tap the ↓ (down) arrow on any card that is not the last card
5. Observe: nothing happens, card order does not change

## Expected Behavior
- Tapping ↑ should move the card up one position in the list
- Tapping ↓ should move the card down one position
- The new order should persist across app restarts

## Actual Behavior
- Nothing happens when tapping the arrows
- Card order remains unchanged

## Relevant Code

### UI: `app/src/main/java/com/cardmaxxxer/ui/wallet/WalletScreen.kt`
- `WalletCardItem` composable renders the card with ↑/↓ `IconButton`s
- `onMoveUp` and `onMoveDown` callbacks are passed from the parent
- The buttons call `viewModel.moveCard(cardId, -1)` and `viewModel.moveCard(cardId, 1)`

### ViewModel: `app/src/main/java/com/cardmaxxxer/ui/wallet/WalletViewModel.kt`
- `moveCard(cardId: String, direction: Int)` function (line ~63)
- It queries `creditCardDao.observeAllActive().first()` and `userCardCrossReferenceRepository.observeByUser("default_user").first()`
- It sorts cards by `priorityRank`, finds the card, and swaps `priorityRank` values with the target card
- It calls `userCardCrossReferenceRepository.update()` on both cross-references

### Data Model: `app/src/main/java/com/cardmaxxxer/domain/model/UserCardCrossReference.kt`
- `priorityRank: Int` field controls display order
- `userId: String` is always `"default_user"` (no multi-user support)

### DAO: `app/src/main/java/com/cardmaxxxer/core/database/dao/UserCardCrossReferenceDao.kt`
- `observeByUser(userId: String): Flow<List<UserCardCrossReferenceEntity>>` — returns cards ordered by `priorityRank ASC`
- `update(crossRef: UserCardCrossReferenceEntity)` — updates a single cross-reference

### Repository: `app/src/main/java/com/cardmaxxxer/data/repository/UserCardCrossReferenceRepositoryImpl.kt`
- `update(crossRef: UserCardCrossReference)` delegates to `userCardCrossReferenceDao.update(crossRef.toEntity())`

## Possible Root Causes

1. **StateFlow not re-emitting after update**: The `update()` call modifies the database, but the `observeByUser` Flow may not re-emit if Room's change detection doesn't trigger. The `observeAllActive()` Flow on `CreditCardDao` re-emits (since cards table is unchanged), but the `observeByUser` Flow on `UserCardCrossReferenceDao` may not detect the change because the primary key `(userId, cardId)` hasn't changed — only the `priorityRank` column.

2. **Race condition**: `moveCard` reads `walletCards.value` (a cached StateFlow) which may be stale. The function was changed to query the DB directly, but the `observeByUser` Flow might still not re-emit.

3. **Transaction not committed**: The `update()` calls may not be committed to the database before the Flow tries to re-emit.

4. **Sorting issue**: The `observeByUser` query sorts by `priorityRank ASC`, but if two cards have the same `priorityRank` (e.g., all initialized to 0), the sort is unstable and the UI may not reflect the change.

## Suggested Fixes

### Option A: Force Flow re-emission
After updating the cross-references, force the wallet to re-read from the database by emitting a new value into a trigger Flow:
```kotlin
private val _refreshTrigger = MutableStateFlow(0)
val walletCards: StateFlow<List<WalletCard>> = combine(
    recommendationRepository.observeWalletCards(),
    _refreshTrigger
) { cards, _ -> cards }.stateIn(...)

fun moveCard(cardId: String, direction: Int) {
    viewModelScope.launch {
        // ... existing swap logic ...
        _refreshTrigger.value = _refreshTrigger.value + 1
    }
}
```

### Option B: Use Room's @Update with proper change detection
Ensure the `update()` method in `UserCardCrossReferenceDao` is annotated correctly and that Room's change detection picks up the `priorityRank` change. May need to use `@Query("UPDATE user_card_cross_reference SET priorityRank = :rank WHERE userId = :userId AND cardId = :cardId")` instead of `@Update`.

### Option C: Re-insert instead of update
Delete and re-insert the cross-reference rows to force Room to detect the change:
```kotlin
userCardCrossReferenceDao.delete(currentCrossRef)
userCardCrossReferenceDao.insert(currentCrossRef.copy(priorityRank = targetCrossRef.priorityRank))
```

### Option D: Initialize priority ranks correctly
Ensure all cards have unique `priorityRank` values when seeded (currently all set to 0):
```kotlin
seedCards.forEachIndexed { index, seedCard ->
    // ...
    val crossRef = UserCardCrossReference(
        // ...
        priorityRank = index,  // unique rank per card
        // ...
    )
}
```

## Additional Context
- The app uses Room with `fallbackToDestructiveMigration()` (schema version 5)
- The database is SQLCipher-encrypted
- The `observeByUser` query: `SELECT * FROM user_card_cross_reference WHERE userId = :userId AND isArchived = 0 ORDER BY priorityRank ASC`
- All cards are seeded with `priorityRank = 0` which may cause sort instability
