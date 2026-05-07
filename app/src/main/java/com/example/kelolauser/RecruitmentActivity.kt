package com.example.kelolauser

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecruitmentActivity : AppCompatActivity() {
    private lateinit var rvCandidates: RecyclerView
    private lateinit var adapter: CandidateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruitment)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        rvCandidates = findViewById(R.id.rvCandidates)
        rvCandidates.layoutManager = LinearLayoutManager(this)
        adapter = CandidateAdapter(emptyList())
        rvCandidates.adapter = adapter

        loadCandidates()

        val etNama: EditText = findViewById(R.id.etNamaCalon)
        val etEmail: EditText = findViewById(R.id.etEmailCalon)
        val etPosisi: EditText = findViewById(R.id.etPosisi)

        val btnSimpan: MaterialButton = findViewById(R.id.btnSimpanCalon)
        btnSimpan.setOnClickListener {
            val name = etNama.text.toString()
            val email = etEmail.text.toString()
            val position = etPosisi.text.toString()

            if (name.isEmpty() || email.isEmpty() || position.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val candidate = Candidate(
                name = name,
                email = email,
                position = position,
                dateApplied = currentDate
            )

            val db = AppDatabase.getDatabase(this)
            db.candidateDao().insert(candidate)

            Toast.makeText(this, "Data calon karyawan $name berhasil disimpan", Toast.LENGTH_SHORT).show()
            
            etNama.text.clear()
            etEmail.text.clear()
            etPosisi.text.clear()
            
            loadCandidates()
        }
    }

    private fun loadCandidates() {
        val db = AppDatabase.getDatabase(this)
        val candidates = db.candidateDao().getAll()
        adapter.updateData(candidates)
    }
}
