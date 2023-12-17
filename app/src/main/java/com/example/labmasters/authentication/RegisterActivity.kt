package com.example.labmasters.authentication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.labmasters.R
import com.example.labmasters.databinding.ActivityMainBinding
import com.example.labmasters.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    var isEmailValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Auth
        auth = FirebaseAuth.getInstance()

        // Email Validation
        val emailStream = RxTextView.textChanges(binding.emRegis)
            .skipInitialValue()
            .map { email ->
                !email.endsWith("@gmail.com")
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }
        // Password Validation
        val passwordStream = RxTextView.textChanges(binding.pwRegis)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe{
            showTextMinimalAlert(it, "Password")
        }

        // Click
        binding.btRegis.setOnClickListener{
            val email = binding.emRegis.text.toString().trim()
            val password = binding.pwRegis.text.toString().trim()
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        if(!isEmailValid) {
            return
        }
        if(email.isEmpty() || password.isEmpty()) {
            finish()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    startActivity(Intent(this, LoginActivity::class.java))
                    Toast.makeText(this, "Register Successfull", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun showEmailValidAlert(isNotValid: Boolean){
        isEmailValid = !isNotValid
        binding.emRegis.error = if (isNotValid) "Email is invalid!" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean, text: String){
        binding.pwRegis.error = if (isNotValid) "$text The password must be more than 6 characters!" else null
    }
}