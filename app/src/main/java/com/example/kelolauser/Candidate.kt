package com.example.kelolauser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class Candidate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String = "-",
    val position: String,
    val dateApplied: String,
    val cvUri: String? = null,
    val status: String = "Pending" // Pending, Diterima, Ditolak
)
