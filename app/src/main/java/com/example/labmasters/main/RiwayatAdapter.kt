package com.example.labmasters.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.labmasters.R
import com.example.labmasters.databinding.ListRiwayatAktivitasBinding
import com.example.labmasters.model.ModelDatabase
import com.example.labmasters.utils.BitmapManager.base64ToBitmap
import com.google.android.material.imageview.ShapeableImageView
import java.lang.String

class RiwayatAdapter (
    var mContext: Context,
    var modelDatabase: MutableList<ModelDatabase>,
    var mAdapterCallback: RiwayatAdapterCallback) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    fun setDataAdapter(items: List<ModelDatabase>) {
        modelDatabase.clear()
        modelDatabase.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListRiwayatAktivitasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelDatabase[position]
        holder.tvNomor.text = String.valueOf(data.uid)
        holder.tvNama.text = data.nama
        holder.tvNpm.text = data.npm
        holder.tvTanggal.text = data.tanggal
        holder.tvKeterangan.text = data.keterangan
        holder.tvStatusRiwayat.text = data.aktivitas

        Glide.with(mContext)
            .load(base64ToBitmap(data.fotoSelfie))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_photo_camera)
            .into(holder.imageProfile)

        when (data.keterangan) {
            "Peminjaman" -> {
                holder.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius)
                holder.colorStatus.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            }

            "Pengembalian" -> {
                holder.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius)
                holder.colorStatus.backgroundTintList = ColorStateList.valueOf(Color.RED)
            }
        }
    }

    override fun getItemCount(): Int {
        return modelDatabase.size
    }

    inner class ViewHolder(private val binding: ListRiwayatAktivitasBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvStatusRiwayat: TextView = binding.tvStatusRiwayat
        var tvNomor: TextView = binding.tvNomor
        var tvNama: TextView = binding.tvNama
        var tvNpm: TextView = binding.tvNpm
        var tvKeterangan: TextView = binding.tvKeterangan
        var tvTanggal: TextView = binding.tvTanggal
        var cvHistory: CardView = binding.cvHistory
        var imageProfile: ShapeableImageView = binding.imageProfile
        var colorStatus: View = binding.colorStatus

        init {
            binding.cvHistory.setOnClickListener {
                val modelLaundry = modelDatabase[adapterPosition]
                mAdapterCallback.onDelete(modelLaundry)
            }
        }
    }

    interface RiwayatAdapterCallback {
        fun onDelete(modelDatabase: ModelDatabase?)
    }
}

