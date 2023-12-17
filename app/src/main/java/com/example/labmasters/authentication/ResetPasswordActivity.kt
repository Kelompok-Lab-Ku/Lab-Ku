package com.example.labmasters.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.labmasters.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView

@SuppressLint("CheckResult")
class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth
    var isEmailValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Auth
        auth = FirebaseAuth.getInstance()

        // Email Validation
        val emailStream = RxTextView.textChanges(binding.emReset)
            .skipInitialValue()
            .map { email ->
                !email.endsWith("@gmail.com")
            }
        emailStream.subscribe{
            showEmailValidAlert(it)
        }

        //Reset Password
        binding.btFgtPwd.setOnClickListener{
            val email = binding.emReset.text.toString().trim()
            if(!isEmailValid) {
                return@setOnClickListener
            }
            if(email.isEmpty()) {
                finish()
                return@setOnClickListener
            }


            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this) { reset ->
                    if (reset.isSuccessful) {
                        Intent(this, LoginActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this, "Check your email to reset your password!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, reset.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        isEmailValid = !isNotValid
        if (isNotValid) {
            binding.emReset.error = "Email tidak valid!"
        } else {
            binding.emReset.error = null
            binding.btFgtPwd.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3697DB")));

        }
    }
}