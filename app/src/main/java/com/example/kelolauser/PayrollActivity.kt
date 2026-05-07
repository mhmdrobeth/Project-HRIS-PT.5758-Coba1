package com.example.kelolauser

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PayrollActivity : AppCompatActivity() {

    private lateinit var rvPayroll: RecyclerView
    private lateinit var adapter: PayrollAdapter
    private lateinit var spinnerMonth: Spinner
    private val allPayrollData = mutableListOf<Payroll>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payroll)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        rvPayroll = findViewById(R.id.rvPayroll)
        spinnerMonth = findViewById(R.id.spinnerMonth)

        setupRecyclerView()
        setupDummyData()
        setupFilter()
    }

    private fun setupRecyclerView() {
        adapter = PayrollAdapter(emptyList())
        rvPayroll.layoutManager = LinearLayoutManager(this)
        rvPayroll.adapter = adapter
    }

    private fun setupDummyData() {
        val db = AppDatabase.getDatabase(this)
        val payrollDao = db.payrollDao()
        
        var payrolls = payrollDao.getAll()
        
        if (payrolls.isEmpty()) {
            payrollDao.insert(Payroll(month = "Januari", year = "2026", basicSalary = 5500000, allowance = 1200000, totalSalary = 6700000, dateIssued = "01 Jan 2026"))
            payrollDao.insert(Payroll(month = "Februari", year = "2026", basicSalary = 5500000, allowance = 1500000, totalSalary = 7000000, dateIssued = "01 Feb 2026"))
            payrollDao.insert(Payroll(month = "Maret", year = "2026", basicSalary = 5500000, allowance = 1000000, totalSalary = 6500000, dateIssued = "01 Mar 2026"))
            payrollDao.insert(Payroll(month = "April", year = "2026", basicSalary = 5500000, allowance = 1000000, totalSalary = 6500000, dateIssued = "01 Apr 2026"))
            payrolls = payrollDao.getAll()
        }
        
        allPayrollData.clear()
        allPayrollData.addAll(payrolls)
        adapter.updateData(allPayrollData)
    }

    private fun setupFilter() {
        val months = arrayOf("Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = spinnerAdapter

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                if (selectedMonth == "Semua Bulan") {
                    adapter.updateData(allPayrollData)
                } else {
                    val filteredList = allPayrollData.filter { it.month == selectedMonth }
                    adapter.updateData(filteredList)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
