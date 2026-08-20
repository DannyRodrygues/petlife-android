package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PetEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PetDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
}

