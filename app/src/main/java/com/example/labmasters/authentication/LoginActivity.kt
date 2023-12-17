package com.example.labmasters.authentication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.labmasters.R
import com.example.labmasters.databinding.ActivityLoginBinding
import com.example.labmasters.main.HomeActivity

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView


@SuppressLint("CheckResult")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    var isEmailValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Auth
        auth = FirebaseAuth.getInstance()

        // Email Validation
        val emailStream = RxTextView.textChanges(binding.emLogin)
            .skipInitialValue()
            .map { email ->
                !email.endsWith("@gmail.com")
            }
        // Password Validation
        val passwordStream = RxTextView.textChanges(binding.pwLogin)
            .skipInitialValue()
            .map { password ->
                password.isEmpty()
            }
        passwordStream.subscribe {
            showTextMinimalAlert(it, "Password")
        }

        // Click
        binding.btforgot.setOnClickListener {
            val intent = (Intent(this, ResetPasswordActivity::class.java))
            startActivity(intent)
        }
        binding.btLog.setOnClickListener {
            val email = binding.emLogin.text.toString().trim()
            val password = binding.pwLogin.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()) {
                finish()
                return@setOnClickListener
            }

            // Pengecekan apakah email dan password sesuai dengan data yang terdaftar
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Login berhasil, alihkan ke aktivitas HomeActivity
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        Toast.makeText(
                            this,
                            "Login failed. Please check your email and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        // Google sign in
    }


    private fun loginUser(email: String, password: String) {
        if(!isEmailValid) {
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { login ->
                if (login.isSuccessful) {
                    Intent(this, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, login.exception?.message, Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String) {
        if (text == "Email") {
            isEmailValid = !isNotValid
            binding.emLogin.error = if (isNotValid) "$text This field must not be empty" else null
        } else if (text == "Password")
            binding.pwLogin.error = if (isNotValid) "$text This field must not be empty" else null
    }

}