package com.example.kelolauser

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class PayrollActivity : AppCompatActivity() {

    private lateinit var rvPayroll: RecyclerView
    private lateinit var adapter: PayrollAdapter
    private lateinit var spinnerMonth: Spinner
    private lateinit var btnAddPayroll: MaterialButton
    private val allPayrollData = mutableListOf<Payroll>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payroll)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        rvPayroll = findViewById(R.id.rvPayroll)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        btnAddPayroll = findViewById(R.id.btnAddPayroll)

        val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
        val role = sharedPref.getString("USER_ROLE", "Karyawan")
        if (role == "Karyawan") {
            btnAddPayroll.visibility = View.GONE
        } else {
            btnAddPayroll.visibility = View.VISIBLE
        }

        btnAddPayroll.setOnClickListener {
            startActivity(Intent(this, AddPayrollActivity::class.java))
        }

        setupRecyclerView()
        setupFilter()
    }

    override fun onResume() {
        super.onResume()
        loadPayrollData()
    }

    private fun loadPayrollData() {
        Thread {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                val payrollDao = db.payrollDao()
                val userDao = db.userDao()

                val sharedPref = getSharedPreferences("UserSession", android.content.Context.MODE_PRIVATE)
                val currentUserId = sharedPref.getInt("USER_ID", -1)
                val role = sharedPref.getString("USER_ROLE", "Karyawan")
                
                // Pastikan data standar ada (4jt / 1jt)
                val existing = payrollDao.getAll()
                if (existing.isEmpty() || existing.any { it.basicSalary != 4000000L }) {
                    payrollDao.deleteAll()
                    val targetId = if (currentUserId != -1) currentUserId else (userDao.getAll().firstOrNull()?.id ?: 1)
                    
                    val months = listOf("Januari", "Februari", "Maret", "April")
                    months.forEach { m ->
                        val basic = 4000000L
                        val allowance = 1000000L
                        val bonus = 1000000L
                        val deduction = 600000L
                        val netSalary = basic + allowance + bonus - deduction
                        
                        payrollDao.insert(Payroll(
                            userId = targetId,
                            month = m,
                            year = "2026",
                            basicSalary = basic,
                            allowance = allowance,
                            totalSalary = netSalary,
                            dateIssued = "25 $m 2026"
                        ))
                    }
                }

                val payrolls = if (role == "Admin HRD") {
                    payrollDao.getAll()
                } else {
                    payrollDao.getPayrollByUserId(currentUserId)
                }

                runOnUiThread {
                    allPayrollData.clear()
                    allPayrollData.addAll(payrolls)
                    updateFilteredData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun updateFilteredData() {
        if (!::spinnerMonth.isInitialized) return
        val selectedMonth = spinnerMonth.selectedItem?.toString() ?: "Semua Bulan"
        val filtered = if (selectedMonth == "Semua Bulan") allPayrollData else allPayrollData.filter { it.month == selectedMonth }
        adapter.updateData(filtered)
    }

    private fun setupRecyclerView() {
        adapter = PayrollAdapter(emptyList())
        rvPayroll.layoutManager = LinearLayoutManager(this)
        rvPayroll.adapter = adapter

        adapter.setOnItemClickListener(object : PayrollAdapter.OnItemClickListener {
            override fun onItemClick(payroll: Payroll) {
                // Navigasi yang AMAN ke Detail
                val intent = Intent(this@PayrollActivity, PayrollDetailActivity::class.java).apply {
                    putExtra("month", payroll.month)
                    putExtra("year", payroll.year)
                    putExtra("basicSalary", payroll.basicSalary)
                    putExtra("allowance", payroll.allowance)
                    putExtra("userId", payroll.userId)
                }
                startActivity(intent)
            }

            override fun onDownloadClick(payroll: Payroll) {
                Toast.makeText(this@PayrollActivity, "Mengunduh Slip Gaji ${payroll.month} ${payroll.year}...", Toast.LENGTH_SHORT).show()
                // Simulasi proses unduh
                rvPayroll.postDelayed({
                    Toast.makeText(this@PayrollActivity, "Slip Gaji ${payroll.month} Berhasil Disimpan", Toast.LENGTH_LONG).show()
                }, 1500)
            }
        })
    }

    private fun setupFilter() {
        val months = arrayOf("Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = spinnerAdapter
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) { updateFilteredData() }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }
}
