package com.example.wisatanusantara

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wisatanusantara.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener {
            val inputEmail = binding.etEmail.text.toString()
            val inputPass = binding.etPassword.text.toString()

            // AMBIL DATA DARI SHAREDPREFERENCES
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val savedEmail = sharedPref.getString("SAVED_EMAIL", null)
            val savedPass = sharedPref.getString("SAVED_PASS", null)
            val savedName = sharedPref.getString("SAVED_NAME", null)

            // VALIDASI LOGIN
            if (inputEmail == savedEmail && inputPass == savedPass && inputEmail.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                // Lempar Nama ke Main untuk Greeting "Halo, [Nama]"
                intent.putExtra("EXTRA_USER", savedName)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}