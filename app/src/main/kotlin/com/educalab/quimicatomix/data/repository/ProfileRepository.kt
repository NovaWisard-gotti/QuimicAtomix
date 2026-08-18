package com.educalab.quimicatomix.data.repository

import com.educalab.quimicatomix.data.local.dao.UserProfileDao
import com.educalab.quimicatomix.data.local.entity.UserProfile
import com.educalab.quimicatomix.domain.logic.XpEngine
import com.educalab.quimicatomix.domain.model.XpAwardResult
import kotlinx.coroutines.flow.Flow

/** Repositorio del perfil local del jugador (alias + avatar, sin datos personales reales). */
class ProfileRepository(private val userProfileDao: UserProfileDao) {

    suspend fun hasProfile(): Boolean = userProfileDao.count() > 0

    suspend fun getOrCreateDefaultId(): Long {
        userProfileDao.getMostRecent()?.let { return it.id }
        val now = System.currentTimeMillis()
        return userProfileDao.insert(
            UserProfile(
                alias = "Explorador",
                avatarId = 0,
                createdAt = now,
                lastActiveAt = now
            )
        )
    }

    suspend fun createProfile(alias: String, avatarId: Int): Long {
        val now = System.currentTimeMillis()
        return userProfileDao.insert(
            UserProfile(alias = alias.take(18), avatarId = avatarId, createdAt = now, lastActiveAt = now)
        )
    }

    fun observeProfile(userId: Long): Flow<UserProfile?> = userProfileDao.observe(userId)

    fun observeAllProfiles(): Flow<List<UserProfile>> = userProfileDao.observeAll()

    suspend fun getProfile(userId: Long): UserProfile? = userProfileDao.getById(userId)

    suspend fun touch(userId: Long) = userProfileDao.touch(userId, System.currentTimeMillis())

    suspend fun setSoundEnabled(userId: Long, enabled: Boolean) = userProfileDao.setSoundEnabled(userId, enabled)

    suspend fun setHapticsEnabled(userId: Long, enabled: Boolean) = userProfileDao.setHapticsEnabled(userId, enabled)

    /** Otorga XP real y persiste el nuevo nivel si corresponde. Único punto de entrada de XP. */
    suspend fun awardXp(userId: Long, xpToAdd: Int): XpAwardResult {
        val profile = userProfileDao.getById(userId) ?: error("Perfil no encontrado")
        val result = XpEngine.award(profile.totalXp, xpToAdd)
        userProfileDao.addXp(userId, xpToAdd)
        if (result.didLevelUp) userProfileDao.setLevel(userId, result.newLevel)
        return result
    }
}
