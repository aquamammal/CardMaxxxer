package com.cardmaxxxer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cardmaxxxer.data.local.entity.CardBenefitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardBenefitDao {

    @Query("SELECT * FROM card_benefits WHERE cardId = :cardId ORDER BY expiresAtEpochDay DESC")
    fun observeByCardId(cardId: String): Flow<List<CardBenefitEntity>>

    @Query("SELECT * FROM card_benefits")
    fun observeAll(): Flow<List<CardBenefitEntity>>

    @Query("SELECT * FROM card_benefits WHERE id = :id")
    fun observeById(id: String): Flow<CardBenefitEntity?>

    @Query("SELECT * FROM card_benefits WHERE id = :id")
    suspend fun getById(id: String): CardBenefitEntity?

    @Query("SELECT * FROM card_benefits WHERE cardId = :cardId")
    suspend fun getByCardId(cardId: String): List<CardBenefitEntity>

    @Query("SELECT * FROM card_benefits WHERE expiresAtEpochDay IS NOT NULL AND expiresAtEpochDay >= :nowEpochDay AND expiresAtEpochDay <= :lookaheadEpochDay")
    suspend fun getExpiringBetween(nowEpochDay: Long, lookaheadEpochDay: Long): List<CardBenefitEntity>

    @Query("SELECT * FROM card_benefits WHERE expiresAtEpochDay IS NOT NULL AND expiresAtEpochDay < :nowEpochDay")
    suspend fun getExpired(nowEpochDay: Long): List<CardBenefitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(benefit: CardBenefitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(benefits: List<CardBenefitEntity>)

    @Update
    suspend fun update(benefit: CardBenefitEntity)

    @Delete
    suspend fun delete(benefit: CardBenefitEntity)

    @Query("DELETE FROM card_benefits WHERE id = :id")
    suspend fun deleteById(id: String)
}
