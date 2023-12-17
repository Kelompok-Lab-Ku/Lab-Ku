package com.example.labmasters.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_peminjaman")
class ModelDatabase : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    var uid = 0

    @ColumnInfo(name = "nama")
    lateinit var nama: String

    @ColumnInfo(name = "npm")
    lateinit var npm: String

    @ColumnInfo(name = "foto_bukti")
    lateinit var fotoSelfie: String

    @ColumnInfo(name = "tanggal")
    lateinit var tanggal: String

    @ColumnInfo(name = "keterangan")
    lateinit var keterangan: String

    @ColumnInfo(name = "aktivitas")
    lateinit var aktivitas: String
}