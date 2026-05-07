package com.example.kelolauser

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Payroll::class, Candidate::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun payrollDao(): PayrollDao
    abstract fun candidateDao(): CandidateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kelola_user_db"
                ).fallbackToDestructiveMigration()
                .allowMainThreadQueries() // Hanya untuk demo, idealnya gunakan coroutines
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
