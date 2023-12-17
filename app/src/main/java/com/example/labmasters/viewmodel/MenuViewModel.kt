package com.example.labmasters.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.labmasters.database.DatabaseClient.Companion.getInstance
import com.example.labmasters.database.dao.DatabaseDao
import com.example.labmasters.model.ModelDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class MenuViewModel (application: Application) : AndroidViewModel(application) {
    var databaseDao: DatabaseDao? = getInstance(application)?.appDatabase?.databaseDao()

    fun addDataMahasiswa(
        foto: String, nama: String,
        tanggal: String, npm: String, keterangan: String, aktivitas: String) {
        Completable.fromAction {
            val modelDatabase = ModelDatabase()
            modelDatabase.fotoSelfie = foto
            modelDatabase.nama = nama
            modelDatabase.npm = npm
            modelDatabase.tanggal = tanggal
            modelDatabase.keterangan = keterangan
            modelDatabase.aktivitas = aktivitas

            databaseDao?.insertData(modelDatabase)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}