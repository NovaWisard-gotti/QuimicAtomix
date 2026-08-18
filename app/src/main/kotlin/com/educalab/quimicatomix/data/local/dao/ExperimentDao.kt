package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.Experiment
import com.educalab.quimicatomix.data.local.entity.ExperimentCombination
import com.educalab.quimicatomix.data.local.entity.ExperimentResult
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import kotlinx.coroutines.flow.Flow

@Dao
interface ExperimentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(experiments: List<Experiment>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSteps(steps: List<ExperimentStep>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCombinations(combinations: List<ExperimentCombination>)

    @Insert
    suspend fun insertResult(result: ExperimentResult): Long

    @Query("SELECT * FROM experiment WHERE topicId = :topicId ORDER BY orderIndex ASC")
    fun observeByTopic(topicId: String): Flow<List<Experiment>>

    @Query("SELECT * FROM experiment ORDER BY orderIndex ASC")
    suspend fun getAll(): List<Experiment>

    @Query("SELECT * FROM experiment WHERE id = :id")
    suspend fun getById(id: String): Experiment?

    @Query("SELECT * FROM experiment_step WHERE experimentId = :experimentId ORDER BY stepIndex ASC")
    suspend fun getSteps(experimentId: String): List<ExperimentStep>

    @Query("SELECT * FROM experiment_combination WHERE experimentId = :experimentId")
    suspend fun getCombinations(experimentId: String): List<ExperimentCombination>

    @Query(
        """SELECT * FROM experiment_combination
           WHERE experimentId = :experimentId
             AND ((substanceAId = :subA AND substanceBId = :subB)
               OR (substanceAId = :subB AND substanceBId = :subA))
           LIMIT 1"""
    )
    suspend fun findCombination(experimentId: String, subA: String, subB: String): ExperimentCombination?

    @Query("SELECT * FROM experiment_result WHERE experimentId = :experimentId ORDER BY timestamp DESC")
    fun observeResultsFor(experimentId: String): Flow<List<ExperimentResult>>

    @Query(
        """SELECT er.* FROM experiment_result er
           INNER JOIN attempt a ON a.id = er.attemptId
           WHERE a.userId = :userId ORDER BY er.timestamp DESC"""
    )
    fun observeResultsForUser(userId: Long): Flow<List<ExperimentResult>>

    @Query("SELECT COUNT(*) FROM experiment")
    suspend fun count(): Int

    @Query("SELECT COUNT(*) FROM experiment WHERE topicId = :topicId")
    suspend fun countByTopic(topicId: String): Int
}
