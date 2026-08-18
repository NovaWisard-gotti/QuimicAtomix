package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.Attempt
import kotlinx.coroutines.flow.Flow

@Dao
interface AttemptDao {
    @Insert
    suspend fun insert(attempt: Attempt): Long

    @Query("SELECT * FROM attempt WHERE userId = :userId ORDER BY finishedAt DESC")
    fun observeForUser(userId: Long): Flow<List<Attempt>>

    @Query("SELECT * FROM attempt WHERE userId = :userId ORDER BY finishedAt DESC LIMIT :limit")
    suspend fun getRecent(userId: Long, limit: Int): List<Attempt>

    @Query("SELECT * FROM attempt WHERE userId = :userId AND experimentId = :experimentId ORDER BY finishedAt DESC")
    suspend fun getForExperiment(userId: Long, experimentId: String): List<Attempt>

    @Query("SELECT * FROM attempt WHERE userId = :userId AND success = 0 ORDER BY finishedAt DESC LIMIT :limit")
    suspend fun getRecentFailures(userId: Long, limit: Int): List<Attempt>

    @Query("SELECT COUNT(*) FROM attempt WHERE userId = :userId AND success = 1")
    suspend fun countSuccesses(userId: Long): Int

    @Query("SELECT COUNT(*) FROM attempt WHERE userId = :userId")
    suspend fun countAll(userId: Long): Int

    @Query("SELECT COALESCE(SUM(starsEarned),0) FROM attempt WHERE userId = :userId")
    suspend fun sumStars(userId: Long): Int

    @Query("SELECT COUNT(*) FROM attempt WHERE userId = :userId AND moleculeChallengeId IS NOT NULL AND success = 1")
    suspend fun countSuccessfulMolecules(userId: Long): Int

    @Query("SELECT COUNT(*) FROM attempt WHERE userId = :userId AND safetyScenarioId IS NOT NULL AND success = 1")
    suspend fun countSuccessfulSafetyScenarios(userId: Long): Int
}
