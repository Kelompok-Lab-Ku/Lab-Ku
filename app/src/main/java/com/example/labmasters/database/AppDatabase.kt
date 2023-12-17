package com.example.labmasters.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.labmasters.database.dao.DatabaseDao
import com.example.labmasters.model.ModelDatabase

@Database(entities = [ModelDatabase::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao?
}