package com.example.labmasters.main

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.labmasters.R
import com.example.labmasters.databinding.ActivityPemilihanMenuBinding
import com.example.labmasters.utils.BitmapManager.bitmapToBase64
import com.example.labmasters.viewmodel.MenuViewModel
import com.karumi.dexter.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class PemilihanMenuActivity : AppCompatActivity() {

    var REQ_CAMERA = 101
    var strFilePath: String = ""
    var strLatitude = "0"
    var strLongitude = "0"
    lateinit var fileDirectoty: File
    lateinit var imageFilename: File
    lateinit var exifInterface: ExifInterface
    lateinit var strBase64Photo: String
    lateinit var strTitle: String
    lateinit var strTimeStamp: String
    lateinit var strImageName: String
    lateinit var menuViewModel: MenuViewModel
    lateinit var progressDialog: ProgressDialog

    private lateinit var binding: ActivityPemilihanMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemilihanMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
        setUploadData()

        // Click Button Back
        binding.toolbar.setOnClickListener {
            val intent = (Intent(this, HomeActivity::class.java))
            startActivity(intent)
        }

    }

    private fun setInitLayout() {
        progressDialog = ProgressDialog(this)
        strTitle = intent.extras?.getString(DATA_TITLE).toString()

        if (strTitle != null) {
            binding.title.text = strTitle
        }
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        menuViewModel = ViewModelProvider(this, (ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application) as ViewModelProvider.Factory)).get(MenuViewModel::class.java)

        binding.inputTanggal.setOnClickListener {
            val tanggalMenu = Calendar.getInstance()
            val date =
                DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    tanggalMenu[Calendar.YEAR] = year
                    tanggalMenu[Calendar.MONTH] = monthOfYear
                    tanggalMenu[Calendar.DAY_OF_MONTH] = dayOfMonth
                    val strFormatDefault = "dd MMMM yyyy HH:mm"
                    val simpleDateFormat = SimpleDateFormat(strFormatDefault, Locale.getDefault())
                    binding.inputTanggal.setText(simpleDateFormat.format(tanggalMenu.time))
                }
            DatePickerDialog(
                this@PemilihanMenuActivity, date,
                tanggalMenu[Calendar.YEAR],
                tanggalMenu[Calendar.MONTH],
                tanggalMenu[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.layoutImage.setOnClickListener {
            Dexter.withContext(this@PemilihanMenuActivity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                cameraIntent.putExtra(
                                    "com.google.assistant.extra.USE_FRONT_CAMERA",
                                    true
                                )
                                cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
                                cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1)
                                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)

//                                // Samsung
//                                cameraIntent.putExtra("camerafacing", "front")
//                                cameraIntent.putExtra("previous_mode", "front")

                                // Huawei
                                cameraIntent.putExtra("default_camera", "1")
                                cameraIntent.putExtra(
                                    "default_mode",
                                    "com.huawei.camera2.mode.photo.PhotoMode")
                                cameraIntent.putExtra(
                                    MediaStore.EXTRA_OUTPUT,
                                    FileProvider.getUriForFile(
                                        this@PemilihanMenuActivity,
                                        packageName + ".provider",
                                        createImageFile()
                                    )
                                )
                                startActivityForResult(cameraIntent, REQ_CAMERA)
                            } catch (ex: IOException) {
                                Toast.makeText(this@PemilihanMenuActivity,
                                    "Ups, gagal membuka kamera", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }
    }


    private fun setUploadData() {
        binding.btSubmit.setOnClickListener {
            val strNama = binding.inputNama.text.toString()
            val strNpm = binding.inputNPM.text.toString()
            val strTanggal = binding.inputTanggal.text.toString()
            val strKeterangan = binding.inputKeterangan.text.toString()
            val strAktivitas = binding.inputAktivitas.text.toString()
            if (strFilePath.isNullOrEmpty() || strNama.isNullOrEmpty() || strNpm.isNullOrEmpty()
                || strTanggal.isNullOrEmpty() || strKeterangan.isNullOrEmpty() || strAktivitas.isNullOrEmpty()) {
                Toast.makeText(this@PemilihanMenuActivity,
                    "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show()
            } else {
                menuViewModel.addDataMahasiswa(
                    strBase64Photo,
                    strNama,
                    strTanggal,
                    strNpm,
                    strKeterangan,
                    strAktivitas)
                Toast.makeText(this@PemilihanMenuActivity,
                    "Laporan Anda terkirim!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        strTimeStamp = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date())
        strImageName = "IMG_"
        fileDirectoty = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "")
        imageFilename = File.createTempFile(strImageName, ".jpg", cacheDir)
        strFilePath = imageFilename.getAbsolutePath()
        return imageFilename
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        convertImage(strFilePath)
    }

    private fun convertImage(imageFilePath: String?) {
        val imageFile = File(imageFilePath)
        if (imageFile.exists()) {
            val options = BitmapFactory.Options()
            var bitmapImage = BitmapFactory.decodeFile(strFilePath, options)

            try {
                exifInterface = ExifInterface(imageFile.absolutePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }

            bitmapImage = Bitmap.createBitmap(
                bitmapImage,
                0,
                0,
                bitmapImage.width,
                bitmapImage.height,
                matrix,
                true
            )

            if (bitmapImage == null) {
                Toast.makeText(this@PemilihanMenuActivity,
                    "Ups, foto kamu belum ada!", Toast.LENGTH_LONG).show()
            } else {
                val resizeImage = (bitmapImage.height * (512.0 / bitmapImage.width)).toInt()
                val scaledBitmap = Bitmap.createScaledBitmap(bitmapImage, 512, resizeImage, true)
                Glide.with(this)
                    .load(scaledBitmap)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_photo_camera)
                    .into(binding.imageBukti)
                strBase64Photo = bitmapToBase64(scaledBitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_TITLE = "TITLE"
    }
}