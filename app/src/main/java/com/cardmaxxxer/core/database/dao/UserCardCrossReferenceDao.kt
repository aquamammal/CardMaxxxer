package com.cardmaxxxer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cardmaxxxer.data.local.entity.UserCardCrossReferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCardCrossReferenceDao {

    @Query("SELECT * FROM user_card_cross_reference WHERE userId = :userId AND isArchived = 0 ORDER BY priorityRank ASC")
    fun observeByUser(userId: String): Flow<List<UserCardCrossReferenceEntity>>

    @Query("SELECT * FROM user_card_cross_reference WHERE userId = :userId")
    fun observeAllByUser(userId: String): Flow<List<UserCardCrossReferenceEntity>>

    @Query("SELECT * FROM user_card_cross_reference WHERE userId = :userId AND cardId = :cardId")
    suspend fun get(userId: String, cardId: String): UserCardCrossReferenceEntity?

    @Query("SELECT * FROM user_card_cross_reference WHERE userId = :userId AND cardId = :cardId")
    fun observe(userId: String, cardId: String): Flow<UserCardCrossReferenceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: UserCardCrossReferenceEntity)

    @Update
    suspend fun update(crossRef: UserCardCrossReferenceEntity)

    @Delete
    suspend fun delete(crossRef: UserCardCrossReferenceEntity)
}
