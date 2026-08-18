package com.educalab.quimicatomix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.educalab.quimicatomix.data.local.entity.MoleculeChallenge
import kotlinx.coroutines.flow.Flow

@Dao
interface MoleculeChallengeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(challenges: List<MoleculeChallenge>)

    @Query("SELECT * FROM molecule_challenge ORDER BY difficulty ASC, unlockLevel ASC")
    fun observeAll(): Flow<List<MoleculeChallenge>>

    @Query("SELECT * FROM molecule_challenge WHERE id = :id")
    suspend fun getById(id: String): MoleculeChallenge?

    @Query("SELECT COUNT(*) FROM molecule_challenge")
    suspend fun count(): Int
}
