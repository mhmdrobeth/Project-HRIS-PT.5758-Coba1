package com.example.kelolauser

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_requests")
data class LeaveRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val userName: String,
    val leaveType: String, // Cuti, Izin, Sakit
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String = "Pending", // Pending, Disetujui, Ditolak
    val dateSubmitted: String
)
