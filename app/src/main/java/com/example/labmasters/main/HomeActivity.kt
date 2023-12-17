package com.example.labmasters.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.labmasters.authentication.LoginActivity
import com.example.labmasters.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    lateinit var session: LoginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        setInitLayout()
    }
    private fun setInitLayout(){
        session = LoginActivity()

        // Click
        binding.peminjaman.setOnClickListener {
            val intent = (Intent(this, PemilihanMenuActivity::class.java))
            intent.putExtra(PemilihanMenuActivity.DATA_TITLE, "Peminjaman")
            startActivity(intent)
        }
        binding.pengembalian.setOnClickListener {
            val intent = (Intent(this, PemilihanMenuActivity::class.java))
            intent.putExtra(PemilihanMenuActivity.DATA_TITLE, "Pengembalian")
            startActivity(intent)
        }
        binding.btLogout.setOnClickListener{
            auth.signOut()
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                Toast.makeText(this, "Logout Successful!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.history.setOnClickListener {
            val intent = (Intent(this, RiwayatActivity::class.java))
            startActivity(intent)
        }
    }
}
