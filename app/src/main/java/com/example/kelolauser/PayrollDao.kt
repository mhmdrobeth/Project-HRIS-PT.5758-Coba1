package com.example.kelolauser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PayrollDao {
    @Query("SELECT * FROM payroll")
    fun getAll(): List<Payroll>

    @Insert
    fun insert(payroll: Payroll)

    @Query("DELETE FROM payroll")
    fun deleteAll()
}
