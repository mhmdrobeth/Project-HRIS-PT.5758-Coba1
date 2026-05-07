package com.example.kelolauser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payroll")
data class Payroll(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val month: String,
    val year: String,
    val basicSalary: String,
    val allowance: String,
    val totalSalary: String,
    val dateIssued: String
)
