package com.example.labmasters.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.labmasters.model.ModelDatabase

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM tbl_peminjaman")
    fun getAllHistory(): LiveData<List<ModelDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(vararg modelDatabases: ModelDatabase)

    @Query("DELETE FROM tbl_peminjaman WHERE uid= :uid")
    fun deleteHistoryById(uid: Int)

    @Query("DELETE FROM tbl_peminjaman")
    fun deleteAllHistory()
}