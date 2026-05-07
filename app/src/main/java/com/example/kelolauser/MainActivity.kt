package com.example.kelolauser

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DividerItemDecoration
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Inisialisasi adapter dengan list kosong dulu
        adapter = UserAdapter(emptyList())
        recyclerView.adapter = adapter

        adapter.setOnUserClickListener(object : UserAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                val intent = Intent(this@MainActivity, EditUserActivity::class.java).apply {
                    putExtra("USER_ID", user.id)
                    putExtra("USER_NAME", user.name)
                    putExtra("USER_DEPT", user.department)
                    putExtra("USER_STATUS", user.status)
                    putExtra("USER_EMAIL", user.email)
                    putExtra("USER_PASS", user.password)
                    putExtra("USER_INITIAL", user.initial)
                }
                startActivity(intent)
            }
        })

        // Ambil data dari Room Database
        loadDataFromDatabase()

        // Set Real Date
        setCurrentDate()

        val tvLihatSemua: TextView = findViewById(R.id.tvLihatSemua)
        tvLihatSemua.setOnClickListener {
            loadDataFromDatabase()
        }

        // Tambahkan Klik untuk Payroll
        val btnPayroll: LinearLayout = findViewById(R.id.btnPayroll)
        btnPayroll.setOnClickListener {
            val intent = Intent(this, PayrollActivity::class.java)
            startActivity(intent)
        }

        // Tambahkan Klik untuk HR AI Analytics
        val btnAnalytics: LinearLayout = findViewById(R.id.btnAnalytics)
        btnAnalytics.setOnClickListener {
            val intent = Intent(this, AnalyticsActivity::class.java)
            startActivity(intent)
        }

        // Tambahkan Klik untuk Rekrutmen
        val btnRecruitment: LinearLayout = findViewById(R.id.btnRecruitment)
        btnRecruitment.setOnClickListener {
            val intent = Intent(this, RecruitmentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }

    private fun setCurrentDate() {
        val tvCurrentDate: TextView = findViewById(R.id.tvCurrentDate)
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val currentDate = dateFormat.format(calendar.time)
        tvCurrentDate.text = currentDate
    }

    private fun loadDataFromDatabase() {
        Thread {
            val db = AppDatabase.getDatabase(this)
            val userList = db.userDao().getAll()
            
            runOnUiThread {
                if (userList.isNotEmpty()) {
                    adapter.updateData(userList)
                } else {
                    Toast.makeText(this, "Belum ada data di database", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}
