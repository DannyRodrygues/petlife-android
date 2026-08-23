package com.dannyrodrygues.petlife.feature.pet.data.local

import android.content.Context
import androidx.room.Room

object PetDatabaseProvider {

    @Volatile
    private var INSTANCE: PetDatabase? = null

    fun getDatabase(context: Context): PetDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                PetDatabase::class.java,
                "petlife_database",
            )
                .addMigrations(
                    PetDatabase.MIGRATION_1_2,
                )
                .build()

            INSTANCE = instance
            instance
        }
    }
}

