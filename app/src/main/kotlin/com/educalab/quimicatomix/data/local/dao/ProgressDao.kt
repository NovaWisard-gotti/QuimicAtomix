package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(progress: List<Progress>)

    @Update
    suspend fun update(progress: Progress)

    @Query("SELECT * FROM progress WHERE userId = :userId ORDER BY topicId ASC")
    fun observeForUser(userId: Long): Flow<List<Progress>>

    @Query("SELECT * FROM progress WHERE userId = :userId AND topicId = :topicId LIMIT 1")
    suspend fun get(userId: Long, topicId: String): Progress?

    @Query("SELECT * FROM progress WHERE userId = :userId")
    suspend fun getAllForUser(userId: Long): List<Progress>

    @Query("UPDATE progress SET mastery = :mastery WHERE userId = :userId AND topicId = :topicId")
    suspend fun setMastery(userId: Long, topicId: String, mastery: MasteryState)

    @Query(
        """UPDATE progress SET experimentsCompleted = experimentsCompleted + 1,
           starsTotal = starsTotal + :stars, lastPlayedAt = :timestamp
           WHERE userId = :userId AND topicId = :topicId"""
    )
    suspend fun registerCompletion(userId: Long, topicId: String, stars: Int, timestamp: Long)
}
