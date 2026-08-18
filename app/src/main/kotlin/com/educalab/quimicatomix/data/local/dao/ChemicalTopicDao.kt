package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import kotlinx.coroutines.flow.Flow

@Dao
interface ChemicalTopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(topics: List<ChemicalTopic>)

    @Query("SELECT * FROM chemical_topic ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<ChemicalTopic>>

    @Query("SELECT * FROM chemical_topic ORDER BY orderIndex ASC")
    suspend fun getAll(): List<ChemicalTopic>

    @Query("SELECT * FROM chemical_topic WHERE id = :id")
    suspend fun getById(id: String): ChemicalTopic?

    @Query("SELECT COUNT(*) FROM chemical_topic")
    suspend fun count(): Int
}
