package com.example.kelolauser

import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class AnalyticsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var tvAttendanceSummary: TextView
    private lateinit var pbLateRate: ProgressBar
    private lateinit var tvLateRate: TextView
    private lateinit var tvTotalSalary: TextView
    private lateinit var tvAverageSalary: TextView
    private lateinit var tvTotalEmployees: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        db = AppDatabase.getDatabase(this)
        
        tvAttendanceSummary = findViewById(R.id.tvAttendanceSummary)
        pbLateRate = findViewById(R.id.pbLateRate)
        tvLateRate = findViewById(R.id.tvLateRate)
        tvTotalSalary = findViewById(R.id.tvTotalSalary)
        tvAverageSalary = findViewById(R.id.tvAverageSalary)
        tvTotalEmployees = findViewById(R.id.tvTotalEmployees)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener { finish() }

        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val totalEmployees = db.userDao().getUserCount()
            val totalAttendance = db.attendanceDao().getTotalAttendanceCount()
            val lateCount = db.attendanceDao().getLateCount()
            val totalSalary = db.payrollDao().getTotalSalary()
            val averageSalary = db.payrollDao().getAverageSalary()

            val lateRate = if (totalAttendance > 0) (lateCount.toDouble() / totalAttendance * 100).toInt() else 0

            withContext(Dispatchers.Main) {
                tvTotalEmployees.text = getString(R.string.total_employees, totalEmployees)
                tvAttendanceSummary.text = getString(R.string.attendance_summary, totalAttendance, lateCount)
                
                pbLateRate.progress = lateRate
                tvLateRate.text = getString(R.string.late_rate, lateRate)

                val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                tvTotalSalary.text = getString(R.string.total_salary_expense, formatter.format(totalSalary))
                tvAverageSalary.text = getString(R.string.average_salary, formatter.format(averageSalary))
            }
        }
    }
}
