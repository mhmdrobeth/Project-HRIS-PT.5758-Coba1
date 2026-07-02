package com.example.kelolauser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(entities = [User::class, Payroll::class, Candidate::class, Attendance::class, LeaveRequest::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun payrollDao(): PayrollDao
    abstract fun candidateDao(): CandidateDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun leaveRequestDao(): LeaveRequestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kelola_user_db"
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                INSTANCE = instance
                
                // Ensure initial data exists
                Executors.newSingleThreadExecutor().execute {
                    instance.seedInitialData()
                }
                
                return instance
            }
        }
    }

    fun seedInitialData() {
        val adminEmail = "admin@hris.com"
        val karyawanEmail = "karyawan@hris.com"
        
        try {
            this.runInTransaction {
                val uDao = userDao()
                if (uDao.getUserByEmail(adminEmail) == null) {
                    uDao.insert(User(
                        name = "Admin HRD",
                        email = adminEmail,
                        password = "admin123",
                        role = "Admin HRD",
                        department = "HRD",
                        status = "Aktif",
                        initial = "A"
                    ))
                }
                
                if (uDao.getUserByEmail(karyawanEmail) == null) {
                    uDao.insert(User(
                        name = "Budi Karyawan",
                        email = karyawanEmail,
                        password = "user123",
                        role = "Karyawan",
                        department = "IT Support",
                        status = "Aktif",
                        initial = "B",
                        phone = "08123456789",
                        position = "IT Support Specialist"
                    ))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
