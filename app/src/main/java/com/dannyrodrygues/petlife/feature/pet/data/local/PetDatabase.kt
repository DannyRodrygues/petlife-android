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
    version = 4,
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

        val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(
                database: SupportSQLiteDatabase,
            ) {
                database.execSQL(
                    """
                    ALTER TABLE pets
                    ADD COLUMN tenantId TEXT NOT NULL
                    DEFAULT 'a8d79e94-9a4c-4385-bde1-6bc4b89a4c8a'
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(
                database: SupportSQLiteDatabase,
            ) {
                database.execSQL(
                    """
            CREATE TABLE vaccines_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                tenantId TEXT NOT NULL,
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
            INSERT INTO vaccines_new (
                id,
                tenantId,
                petId,
                name,
                doseDescription,
                applicationDateMillis,
                nextDoseDateMillis,
                observations
            )
            SELECT
                vaccines.id,
                pets.tenantId,
                vaccines.petId,
                vaccines.name,
                vaccines.doseDescription,
                vaccines.applicationDateMillis,
                vaccines.nextDoseDateMillis,
                vaccines.observations
            FROM vaccines
            INNER JOIN pets
                ON pets.id = vaccines.petId
            """.trimIndent(),
                )

                database.execSQL(
                    """
            DROP TABLE vaccines
            """.trimIndent(),
                )

                database.execSQL(
                    """
            ALTER TABLE vaccines_new
            RENAME TO vaccines
            """.trimIndent(),
                )

                database.execSQL(
                    """
            CREATE INDEX index_vaccines_petId
            ON vaccines(petId)
            """.trimIndent(),
                )

                database.execSQL(
                    """
            CREATE INDEX index_vaccines_tenantId_petId
            ON vaccines(tenantId, petId)
            """.trimIndent(),
                )
            }
        }
    }
}