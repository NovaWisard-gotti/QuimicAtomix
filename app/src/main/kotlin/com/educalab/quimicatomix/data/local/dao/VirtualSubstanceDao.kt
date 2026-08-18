package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.SubstanceProperty
import com.educalab.quimicatomix.data.local.entity.VirtualSubstance
import kotlinx.coroutines.flow.Flow

@Dao
interface VirtualSubstanceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(substances: List<VirtualSubstance>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProperties(properties: List<SubstanceProperty>)

    @Query("SELECT * FROM virtual_substance WHERE topicId = :topicId")
    fun observeByTopic(topicId: String): Flow<List<VirtualSubstance>>

    @Query("SELECT * FROM virtual_substance")
    suspend fun getAll(): List<VirtualSubstance>

    @Query("SELECT * FROM virtual_substance WHERE id = :id")
    suspend fun getById(id: String): VirtualSubstance?

    @Query("SELECT * FROM virtual_substance WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<VirtualSubstance>

    @Query("SELECT * FROM substance_property WHERE substanceId = :substanceId")
    fun observeProperties(substanceId: String): Flow<List<SubstanceProperty>>

    @Query("SELECT COUNT(*) FROM virtual_substance")
    suspend fun count(): Int
}
