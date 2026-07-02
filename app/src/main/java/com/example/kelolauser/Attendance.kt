package com.example.kelolauser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val userName: String,
    val date: String,
    val time: String,
    val status: String // Hadir, Terlambat
)
