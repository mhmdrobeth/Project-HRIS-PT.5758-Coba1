package com.example.kelolauser

import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName: EditText = findViewById(R.id.etName)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnRegister: MaterialButton = findViewById(R.id.btnRegister)
        val tvToLogin: TextView = findViewById(R.id.tvToLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simpan ke Room Database secara lokal
            val newUser = User(
                name = name,
                email = email,
                password = password, // Sekarang password ikut disimpan
                role = "Karyawan",
                department = "IT",
                status = "Aktif",
                initial = name.take(1).uppercase()
            )

            // Jalankan di background thread karena Room tidak boleh di main thread
            Thread {
                AppDatabase.getDatabase(this).userDao().insert(newUser)
                runOnUiThread {
                    Toast.makeText(this, "Berhasil Daftar secara Lokal!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }

        tvToLogin.setOnClickListener {
            // Go back to login
            finish()
        }
    }
}
