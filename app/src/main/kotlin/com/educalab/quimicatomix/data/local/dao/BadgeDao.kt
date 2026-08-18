package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(badges: List<Badge>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun award(userBadge: UserBadge): Long

    @Query("SELECT * FROM badge ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<Badge>>

    @Query("SELECT * FROM badge ORDER BY orderIndex ASC")
    suspend fun getAll(): List<Badge>

    @Query("SELECT badgeId FROM user_badge WHERE userId = :userId")
    fun observeEarnedIds(userId: Long): Flow<List<String>>

    @Query("SELECT badgeId FROM user_badge WHERE userId = :userId")
    suspend fun getEarnedIds(userId: Long): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM user_badge WHERE userId = :userId AND badgeId = :badgeId)")
    suspend fun hasBadge(userId: Long, badgeId: String): Boolean

    @Query("SELECT COUNT(*) FROM badge")
    suspend fun count(): Int
}
