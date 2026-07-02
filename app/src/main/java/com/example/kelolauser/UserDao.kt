package com.example.kelolauser

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: Int): User?

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Int
    @Query("SELECT * FROM users WHERE name LIKE :query OR position LIKE :query")
    fun searchUsers(query: String): List<User>
}
