package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.LabEquipment
import com.educalab.quimicatomix.data.local.entity.UnlockedEquipment
import kotlinx.coroutines.flow.Flow

@Dao
interface LabEquipmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(equipment: List<LabEquipment>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlock(unlocked: UnlockedEquipment): Long

    @Query("SELECT * FROM lab_equipment ORDER BY orderIndex ASC")
    fun observeAll(): Flow<List<LabEquipment>>

    @Query("SELECT * FROM lab_equipment ORDER BY orderIndex ASC")
    suspend fun getAll(): List<LabEquipment>

    @Query("SELECT equipmentId FROM unlocked_equipment WHERE userId = :userId")
    fun observeUnlockedIds(userId: Long): Flow<List<String>>

    @Query("SELECT equipmentId FROM unlocked_equipment WHERE userId = :userId")
    suspend fun getUnlockedIds(userId: Long): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM unlocked_equipment WHERE userId = :userId AND equipmentId = :equipmentId)")
    suspend fun isUnlocked(userId: Long, equipmentId: String): Boolean

    @Query("SELECT COUNT(*) FROM lab_equipment")
    suspend fun count(): Int
}
