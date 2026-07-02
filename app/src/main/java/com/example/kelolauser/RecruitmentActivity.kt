package com.example.kelolauser

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
    private var selectedCvUri: Uri? = null
    private lateinit var tvCvStatus: TextView

    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedCvUri = result.data?.data
            selectedCvUri?.let { uri ->
                val fileName = getFileName(uri)
                tvCvStatus.text = "Terpilih: $fileName"
                tvCvStatus.setTextColor(getColor(R.color.icon_blue))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruitment)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        tvCvStatus = findViewById(R.id.tvCvStatus)
        val btnUploadCv: LinearLayout = findViewById(R.id.btnUploadCv)
        btnUploadCv.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            pdfPickerLauncher.launch(intent)
        }

        rvCandidates = findViewById(R.id.rvCandidates)
        rvCandidates.layoutManager = LinearLayoutManager(this)
        adapter = CandidateAdapter(emptyList()) { candidate ->
            showCandidateDetail(candidate)
        }
        rvCandidates.adapter = adapter

        loadCandidates()

        val etNama: EditText = findViewById(R.id.etNamaCalon)
        val etEmail: EditText = findViewById(R.id.etEmailCalon)
        val etPhone: EditText = findViewById(R.id.etPhoneCalon)
        val etPosisi: EditText = findViewById(R.id.etPosisi)

        val btnSimpan: MaterialButton = findViewById(R.id.btnSimpanCalon)
        btnSimpan.setOnClickListener {
            val name = etNama.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val position = etPosisi.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || position.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data termasuk nomor telepon", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Format email tidak valid"
                return@setOnClickListener
            }

            if (selectedCvUri == null) {
                Toast.makeText(this, "Silakan upload CV (PDF) terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Persist permission for the URI
            contentResolver.takePersistableUriPermission(selectedCvUri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(Date())

            val candidate = Candidate(
                name = name,
                email = email,
                phone = phone,
                position = position,
                dateApplied = currentDate,
                cvUri = selectedCvUri.toString()
            )

            Thread {
                val db = AppDatabase.getDatabase(this)
                db.candidateDao().insert(candidate)

                runOnUiThread {
                    Toast.makeText(this, "Data calon karyawan $name berhasil disimpan", Toast.LENGTH_SHORT).show()
                    
                    etNama.text.clear()
                    etEmail.text.clear()
                    etPhone.text.clear()
                    etPosisi.text.clear()
                    selectedCvUri = null
                    tvCvStatus.text = "Upload CV (PDF)"
                    tvCvStatus.setTextColor(getColor(R.color.text_secondary))
                    
                    loadCandidates()
                }
            }.start()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) result = cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "file.pdf"
    }

    private fun showCandidateDetail(candidate: Candidate) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Detail Kandidat")
            .setMessage("""
                Nama: ${candidate.name}
                Email: ${candidate.email}
                Telepon: ${candidate.phone}
                Posisi: ${candidate.position}
                Tanggal: ${candidate.dateApplied}
            """.trimIndent())
            .setPositiveButton("Lihat CV") { _, _ ->
                candidate.cvUri?.let { uriString ->
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(Uri.parse(uriString), "application/pdf")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(this, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(this, "CV tidak tersedia", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tutup", null)
            .create()
        dialog.show()
    }

    private fun loadCandidates() {
        Thread {
            val db = AppDatabase.getDatabase(this)
            val candidates = db.candidateDao().getAll()
            runOnUiThread {
                adapter.updateData(candidates)
            }
        }.start()
    }
}
