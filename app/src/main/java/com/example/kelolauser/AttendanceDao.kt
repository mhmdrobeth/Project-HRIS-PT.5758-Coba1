package com.example.kelolauser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance ORDER BY id DESC")
    fun getAllAttendance(): List<Attendance>

    @Insert
    fun insert(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE userId = :userId ORDER BY id DESC")
    fun getAttendanceByUserId(userId: Int): List<Attendance>

    @Query("SELECT COUNT(*) FROM attendance WHERE status = 'Terlambat'")
    fun getLateCount(): Int

    @Query("SELECT COUNT(*) FROM attendance")
    fun getTotalAttendanceCount(): Int
}
