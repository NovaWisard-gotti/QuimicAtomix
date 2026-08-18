package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.Atom
import kotlinx.coroutines.flow.Flow

@Dao
interface AtomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(atoms: List<Atom>)

    @Query("SELECT * FROM atom ORDER BY protons ASC")
    fun observeAll(): Flow<List<Atom>>

    @Query("SELECT * FROM atom ORDER BY protons ASC")
    suspend fun getAll(): List<Atom>

    @Query("SELECT * FROM atom WHERE id = :id")
    suspend fun getById(id: String): Atom?

    @Query("SELECT * FROM atom WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<String>): List<Atom>

    @Query("SELECT COUNT(*) FROM atom")
    suspend fun count(): Int
}
