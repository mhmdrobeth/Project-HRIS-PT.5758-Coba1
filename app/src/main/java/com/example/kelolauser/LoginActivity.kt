package com.example.kelolauser

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: MaterialButton = findViewById(R.id.btnLogin)
        val tvToRegister: TextView = findViewById(R.id.tvToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verifikasi Login menggunakan Room Database secara lokal
            Thread {
                val db = AppDatabase.getDatabase(this)
                val user = db.userDao().getUserByEmail(email)

                runOnUiThread {
                    if (user != null) {
                        // Cek apakah password cocok
                        if (user.password == password) {
                            // Simpan session user
                            val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putInt("USER_ID", user.id)
                                putString("USER_NAME", user.name)
                                putString("USER_EMAIL", user.email) // Simpan Email
                                putString("USER_ROLE", user.role) // Simpan Role (Admin HRD / Karyawan)
                                apply()
                            }

                            Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Jika email tidak ditemukan di database
                        Toast.makeText(this, "Akun belum terdaftar. Silakan registrasi terlebih dahulu.", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }

        tvToRegister.setOnClickListener {
            // Redirect to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
