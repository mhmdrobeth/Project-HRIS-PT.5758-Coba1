package com.example.kelolauser

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.NumberFormat
import java.util.Locale

class PayrollDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_payroll_detail)

            // 1. Back Button
            findViewById<ImageView>(R.id.btnBack)?.setOnClickListener { finish() }

            // 2. Ambil Data Intent
            val month = intent.getStringExtra("month") ?: "Januari"
            val year = intent.getStringExtra("year") ?: "2026"
            val basicSalary = intent.getLongExtra("basicSalary", 4000000L)
            val allowance = intent.getLongExtra("allowance", 1000000L)
            val userId = intent.getIntExtra("userId", 0)

            // 3. Binding Data
            findViewById<TextView>(R.id.tvPeriodTitle)?.text = "PERIODE: ${month.uppercase()} $year"
            findViewById<TextView>(R.id.tvSlipBasic)?.text = formatRupiah(basicSalary)
            
            val splitAllowance = allowance / 2
            findViewById<TextView>(R.id.tvSlipMakan)?.text = formatRupiah(splitAllowance)
            findViewById<TextView>(R.id.tvSlipTransport)?.text = formatRupiah(splitAllowance)
            
            val bonus = 1000000L
            findViewById<TextView>(R.id.tvSlipBonus)?.text = formatRupiah(bonus)
            
            val totalIncome = basicSalary + allowance + bonus
            val totalDeduction = 600000L
            
            findViewById<TextView>(R.id.tvSlipTotalDeduction)?.text = "- ${formatRupiah(totalDeduction)}"
            findViewById<TextView>(R.id.tvSlipNetSalary)?.text = formatRupiah(totalIncome - totalDeduction)

            // Styling Dynamic
            findViewById<TextView>(R.id.tvSlipNetSalary)?.setTextColor(ContextCompat.getColor(this, R.color.primary_m2))

            loadUserInfo(userId)

            // 5. Fitur Unduh Slip Gaji (Simpan ke Storage)
            findViewById<View>(R.id.btnDownloadSlip)?.setOnClickListener {
                generateAndSavePdf(month, year)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Sistem: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateAndSavePdf(month: String, year: String) {
        val view = findViewById<View>(R.id.cardSlip) ?: return
        
        try {
            // 1. Create Bitmap from View
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            // 2. Create PDF Document
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            
            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)

            // 3. Save to Storage (Scoped Storage for Android 10+)
            val fileName = "Slip_Gaji_${month}_${year}_${System.currentTimeMillis()}.pdf"
            val outputStream: OutputStream?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                outputStream = uri?.let { resolver.openOutputStream(it) }
            } else {
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, fileName)
                outputStream = FileOutputStream(file)
            }

            outputStream?.use {
                pdfDocument.writeTo(it)
                Toast.makeText(this, "Slip Gaji berhasil disimpan di folder Download", Toast.LENGTH_LONG).show()
            }
            
            pdfDocument.close()
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal mengunduh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserInfo(userId: Int) {
        Thread {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                val user = if (userId != 0) db.userDao().getUserById(userId) else db.userDao().getAll().firstOrNull()
                
                runOnUiThread {
                    user?.let {
                        findViewById<TextView>(R.id.tvSlipName)?.text = it.name
                        findViewById<TextView>(R.id.tvSlipPosition)?.text = it.position
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun formatRupiah(number: Long): String {
        return try {
            val localeID = Locale("id", "ID")
            val numberFormat = NumberFormat.getCurrencyInstance(localeID)
            numberFormat.format(number).replace("Rp", "Rp ")
        } catch (e: Exception) {
            "Rp $number"
        }
    }
}
