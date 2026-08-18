package com.educalab.quimicatomix.data.local.dao

import androidx.room.*
import com.educalab.quimicatomix.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert
    suspend fun insert(profile: UserProfile): Long

    @Update
    suspend fun update(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :id")
    fun observe(id: Long): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile ORDER BY lastActiveAt DESC")
    fun observeAll(): Flow<List<UserProfile>>

    @Query("SELECT * FROM user_profile WHERE id = :id")
    suspend fun getById(id: Long): UserProfile?

    @Query("SELECT * FROM user_profile ORDER BY lastActiveAt DESC LIMIT 1")
    suspend fun getMostRecent(): UserProfile?

    @Query("SELECT COUNT(*) FROM user_profile")
    suspend fun count(): Int

    @Query("UPDATE user_profile SET totalXp = totalXp + :xpDelta WHERE id = :id")
    suspend fun addXp(id: Long, xpDelta: Int)

    @Query("UPDATE user_profile SET level = :level WHERE id = :id")
    suspend fun setLevel(id: Long, level: Int)

    @Query("UPDATE user_profile SET soundEnabled = :enabled WHERE id = :id")
    suspend fun setSoundEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE user_profile SET hapticsEnabled = :enabled WHERE id = :id")
    suspend fun setHapticsEnabled(id: Long, enabled: Boolean)

    @Query("UPDATE user_profile SET lastActiveAt = :timestamp WHERE id = :id")
    suspend fun touch(id: Long, timestamp: Long)
}
