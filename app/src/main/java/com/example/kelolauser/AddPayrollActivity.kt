package com.example.kelolauser

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class AddPayrollActivity : AppCompatActivity() {

    private lateinit var spinnerUser: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var etYear: EditText
    private lateinit var etBasicSalary: EditText
    private lateinit var etAllowance: EditText
    private lateinit var btnSave: MaterialButton

    private var userList: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_payroll)

        spinnerUser = findViewById(R.id.spinnerUser)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        etYear = findViewById(R.id.etYear)
        etBasicSalary = findViewById(R.id.etBasicSalary)
        etAllowance = findViewById(R.id.etAllowance)
        btnSave = findViewById(R.id.btnSavePayroll)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        setupUserSpinner()
        setupMonthSpinner()
        
        etYear.setText(Calendar.getInstance().get(Calendar.YEAR).toString())
        etBasicSalary.setText("4000000")
        etAllowance.setText("1000000")

        btnSave.setOnClickListener {
            savePayroll()
        }
    }

    private fun setupUserSpinner() {
        Thread {
            val db = AppDatabase.getDatabase(this)
            userList = db.userDao().getAll()
            val userNames = userList.map { it.name }

            runOnUiThread {
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerUser.adapter = adapter
            }
        }.start()
    }

    private fun setupMonthSpinner() {
        val months = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = adapter
        
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        spinnerMonth.setSelection(currentMonth)
    }

    private fun savePayroll() {
        val year = etYear.text.toString().trim()
        val basicSalaryStr = etBasicSalary.text.toString().trim()
        val allowanceStr = etAllowance.text.toString().trim()

        if (year.isEmpty() || basicSalaryStr.isEmpty() || allowanceStr.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        if (userList.isEmpty()) {
            Toast.makeText(this, "Data karyawan tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedUser = userList[spinnerUser.selectedItemPosition]
        val selectedMonth = spinnerMonth.selectedItem.toString()
        val basicSalary = basicSalaryStr.toLong()
        val allowance = allowanceStr.toLong()
        val totalSalary = basicSalary + allowance
        
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val dateIssued = dateFormat.format(Date())

        Thread {
            val db = AppDatabase.getDatabase(this)
            val newPayroll = Payroll(
                userId = selectedUser.id,
                month = selectedMonth,
                year = year,
                basicSalary = basicSalary,
                allowance = allowance,
                totalSalary = totalSalary,
                dateIssued = dateIssued
            )
            db.payrollDao().insert(newPayroll)

            runOnUiThread {
                Toast.makeText(this, "Slip gaji berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}
