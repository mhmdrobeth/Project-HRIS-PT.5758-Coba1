package com.example.kelolauser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LeaveRequestDao {
    @Query("SELECT * FROM leave_requests ORDER BY id DESC")
    fun getAll(): List<LeaveRequest>

    @Query("SELECT * FROM leave_requests WHERE userEmail = :email ORDER BY id DESC")
    fun getByUser(email: String): List<LeaveRequest>

    @Insert
    fun insert(leaveRequest: LeaveRequest)

    @Update
    fun update(leaveRequest: LeaveRequest)

    @Query("UPDATE leave_requests SET status = :status WHERE id = :id")
    fun updateStatus(id: Int, status: String)
}
