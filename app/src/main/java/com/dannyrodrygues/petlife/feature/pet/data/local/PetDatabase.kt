package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineDao
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity

@Database(
    entities = [
        PetEntity::class,
        VaccineEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class PetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun vaccineDao(): VaccineDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(
                database: SupportSQLiteDatabase,
            ) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS vaccines (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        petId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        doseDescription TEXT,
                        applicationDateMillis INTEGER,
                        nextDoseDateMillis INTEGER,
                        observations TEXT,
                        FOREIGN KEY(petId)
                            REFERENCES pets(id)
                            ON DELETE CASCADE
                    )
                    """.trimIndent(),
                )

                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS index_vaccines_petId
                    ON vaccines(petId)
                    """.trimIndent(),
                )
            }
        }
    }
}