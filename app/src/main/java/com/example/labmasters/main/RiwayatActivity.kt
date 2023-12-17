package com.example.labmasters.main

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmasters.databinding.ActivityRiwayatBinding
import com.example.labmasters.model.ModelDatabase
import com.example.labmasters.viewmodel.RiwayatViewModel

class RiwayatActivity : AppCompatActivity(), RiwayatAdapter.RiwayatAdapterCallback {
    private lateinit var binding: ActivityRiwayatBinding
    var modelDatabaseList: MutableList<ModelDatabase> = ArrayList()
    lateinit var RiwayatAdapter: RiwayatAdapter
    lateinit var riwayatViewModel: RiwayatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
        setViewModel()

        riwayatViewModel = ViewModelProvider(this).get(RiwayatViewModel::class.java)

        setViewModel()
    }

    private fun setInitLayout() {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        binding.tvNotFound.visibility = View.GONE

        RiwayatAdapter = RiwayatAdapter(this, modelDatabaseList, this)
        binding.rvRiwayat.setHasFixedSize(true)
        binding.rvRiwayat.layoutManager = LinearLayoutManager(this)
        binding.rvRiwayat.adapter = RiwayatAdapter
    }

    private fun setViewModel() {
        riwayatViewModel = ViewModelProvider(this).get(RiwayatViewModel::class.java)
        riwayatViewModel.dataLaporan.observe(this) { modelDatabases: List<ModelDatabase> ->
            if (modelDatabases.isEmpty()) {
                binding.tvNotFound.visibility = View.VISIBLE
                binding.rvRiwayat.visibility = View.GONE
            } else {
                binding.tvNotFound.visibility = View.GONE
                binding.rvRiwayat.visibility = View.VISIBLE
            }
            RiwayatAdapter.setDataAdapter(modelDatabases)
        }
    }

    override fun onDelete(modelDatabase: ModelDatabase?) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Hapus riwayat ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface, i ->
            val uid = modelDatabase!!.uid
            riwayatViewModel.deleteDataById(uid)
            Toast.makeText(this@RiwayatActivity, "Yeay! Data yang dipilih sudah dihapus",
                Toast.LENGTH_SHORT).show()
        }
        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i:
        Int -> dialogInterface.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }}