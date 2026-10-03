package com.cardmaxxxer.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cardmaxxxer.core.database.dao.CardBenefitDao
import com.cardmaxxxer.core.database.dao.CreditCardDao
import com.cardmaxxxer.core.database.dao.PerkRedemptionLogDao
import com.cardmaxxxer.core.database.dao.UserCardCrossReferenceDao
import com.cardmaxxxer.core.security.DatabaseKeyManager
import com.cardmaxxxer.data.local.entity.CardBenefitEntity
import com.cardmaxxxer.data.local.entity.CreditCardEntity
import com.cardmaxxxer.data.local.entity.PerkRedemptionLogEntity
import com.cardmaxxxer.data.local.entity.UserCardCrossReferenceEntity
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Database(
    entities = [
        CreditCardEntity::class,
        CardBenefitEntity::class,
        PerkRedemptionLogEntity::class,
        UserCardCrossReferenceEntity::class,
    ],
    version = 5,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class CardMaxxxerDatabase : RoomDatabase() {

    abstract fun creditCardDao(): CreditCardDao
    abstract fun cardBenefitDao(): CardBenefitDao
    abstract fun perkRedemptionLogDao(): PerkRedemptionLogDao
    abstract fun userCardCrossReferenceDao(): UserCardCrossReferenceDao

    companion object {
        private const val DATABASE_NAME = "cardmaxxxer.db"

        @Volatile
        private var INSTANCE: CardMaxxxerDatabase? = null

        fun getInstance(context: Context, keyManager: DatabaseKeyManager): CardMaxxxerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, keyManager).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context, keyManager: DatabaseKeyManager): CardMaxxxerDatabase {
            val passphrase = keyManager.getPassphrase()
            val factory = SupportOpenHelperFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext,
                CardMaxxxerDatabase::class.java,
                DATABASE_NAME
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
