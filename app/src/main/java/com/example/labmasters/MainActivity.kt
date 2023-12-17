package com.example.labmasters

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.labmasters.authentication.LoginActivity
import com.example.labmasters.authentication.RegisterActivity
import com.example.labmasters.databinding.ActivityMainBinding
import com.example.labmasters.main.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.login.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.register.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


}