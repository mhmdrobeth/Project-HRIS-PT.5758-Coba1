package com.example.kelolauser

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class EditUserActivity : AppCompatActivity() {
    private var userId: Int = -1
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        userId = intent.getIntExtra("USER_ID", -1)
        val userName = intent.getStringExtra("USER_NAME") ?: ""
        val userDept = intent.getStringExtra("USER_DEPT") ?: ""
        val userStatus = intent.getStringExtra("USER_STATUS") ?: ""
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        val userPass = intent.getStringExtra("USER_PASS") ?: ""
        val userInitial = intent.getStringExtra("USER_INITIAL") ?: ""

        val etName: EditText = findViewById(R.id.etEditName)
        val etDept: EditText = findViewById(R.id.etEditDepartment)
        val etStatus: EditText = findViewById(R.id.etEditStatus)

        etName.setText(userName)
        etDept.setText(userDept)
        etStatus.setText(userStatus)

        findViewById<MaterialButton>(R.id.btnUpdateUser).setOnClickListener {
            val updatedUser = User(
                id = userId,
                name = etName.text.toString(),
                email = userEmail,
                password = userPass,
                role = "Karyawan",
                department = etDept.text.toString(),
                status = etStatus.text.toString(),
                initial = userInitial
            )
            AppDatabase.getDatabase(this).userDao().update(updatedUser)
            Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<MaterialButton>(R.id.btnDeleteUser).setOnClickListener {
            val deleteUser = User(
                id = userId,
                name = userName,
                email = userEmail,
                password = userPass,
                role = "Karyawan",
                department = userDept,
                status = userStatus,
                initial = userInitial
            )
            AppDatabase.getDatabase(this).userDao().delete(deleteUser)
            Toast.makeText(this, "Karyawan berhasil dihapus", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
