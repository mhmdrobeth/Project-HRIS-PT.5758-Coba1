package com.example.kelolauser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payroll")
data class Payroll(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int = 0, // Menghubungkan gaji ke user tertentu
    val month: String,
    val year: String,
    val basicSalary: Long, // Menggunakan Long agar bisa dihitung
    val allowance: Long,
    val totalSalary: Long,
    val dateIssued: String
)
