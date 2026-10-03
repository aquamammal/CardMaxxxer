package com.cardmaxxxer.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.cardmaxxxer.data.local.entity.CreditCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {

    @Query("SELECT * FROM credit_cards WHERE isActive = 1 ORDER BY createdAt DESC")
    fun observeAllActive(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<CreditCardEntity>>

    @Query("SELECT * FROM credit_cards WHERE id = :id")
    fun observeById(id: String): Flow<CreditCardEntity?>

    @Query("SELECT * FROM credit_cards WHERE id = :id")
    suspend fun getById(id: String): CreditCardEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: CreditCardEntity)

    @Update
    suspend fun update(card: CreditCardEntity)

    @Delete
    suspend fun delete(card: CreditCardEntity)

    @Query("DELETE FROM credit_cards WHERE id = :id")
    suspend fun deleteById(id: String)
}
