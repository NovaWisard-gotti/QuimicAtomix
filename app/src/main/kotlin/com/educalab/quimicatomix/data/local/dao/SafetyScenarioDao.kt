package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.SafetyScenario
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyScenarioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(scenarios: List<SafetyScenario>)

    @Query("SELECT * FROM safety_scenario ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<SafetyScenario>>

    @Query("SELECT * FROM safety_scenario ORDER BY orderIndex ASC")
    suspend fun getAll(): List<SafetyScenario>

    @Query("SELECT * FROM safety_scenario WHERE id = :id")
    suspend fun getById(id: String): SafetyScenario?

    @Query("SELECT COUNT(*) FROM safety_scenario")
    suspend fun count(): Int
}
