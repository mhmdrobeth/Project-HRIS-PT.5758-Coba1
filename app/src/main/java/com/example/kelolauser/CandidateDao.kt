package com.example.kelolauser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CandidateDao {
    @Query("SELECT * FROM candidates")
    fun getAll(): List<Candidate>

    @Insert
    fun insert(candidate: Candidate)

    @Query("UPDATE candidates SET status = :status WHERE id = :id")
    fun updateStatus(id: Int, status: String)
}
