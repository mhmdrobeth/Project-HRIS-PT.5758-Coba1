package com.example.kelolauser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PayrollDao {
    @Query("SELECT * FROM payroll")
    fun getAll(): List<Payroll>

    @Query("SELECT * FROM payroll WHERE userId = :userId")
    fun getPayrollByUserId(userId: Int): List<Payroll>

    @Insert
    fun insert(payroll: Payroll)

    @Query("DELETE FROM payroll")
    fun deleteAll()

    @Query("SELECT SUM(totalSalary) FROM payroll")
    fun getTotalSalary(): Long

    @Query("SELECT AVG(totalSalary) FROM payroll")
    fun getAverageSalary(): Double

    @Query("SELECT COUNT(*) FROM payroll")
    fun getPayrollCount(): Int
}
