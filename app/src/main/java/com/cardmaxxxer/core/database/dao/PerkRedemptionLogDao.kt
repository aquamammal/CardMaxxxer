package com.cardmaxxxer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cardmaxxxer.data.local.entity.PerkRedemptionLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PerkRedemptionLogDao {

    @Query("SELECT * FROM perk_redemption_logs WHERE benefitId = :benefitId ORDER BY redeemedAtEpochDay DESC")
    fun observeByBenefitId(benefitId: String): Flow<List<PerkRedemptionLogEntity>>

    @Query("SELECT * FROM perk_redemption_logs WHERE cardId = :cardId ORDER BY redeemedAtEpochDay DESC")
    fun observeByCardId(cardId: String): Flow<List<PerkRedemptionLogEntity>>

    @Query("SELECT * FROM perk_redemption_logs ORDER BY redeemedAtEpochDay DESC")
    fun observeAll(): Flow<List<PerkRedemptionLogEntity>>

    @Query("SELECT * FROM perk_redemption_logs WHERE id = :id")
    suspend fun getById(id: String): PerkRedemptionLogEntity?

    @Query("SELECT * FROM perk_redemption_logs WHERE benefitId = :benefitId ORDER BY redeemedAtEpochDay DESC")
    suspend fun getByBenefitId(benefitId: String): List<PerkRedemptionLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: PerkRedemptionLogEntity)

    @Update
    suspend fun update(log: PerkRedemptionLogEntity)

    @Delete
    suspend fun delete(log: PerkRedemptionLogEntity)
}
