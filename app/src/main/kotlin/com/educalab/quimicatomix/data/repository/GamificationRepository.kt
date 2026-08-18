package com.educalab.quimicatomix.data.repository

import com.educalab.quimicatomix.data.local.dao.AttemptDao
import com.educalab.quimicatomix.data.local.dao.BadgeDao
import com.educalab.quimicatomix.data.local.dao.LabEquipmentDao
import com.educalab.quimicatomix.data.local.dao.ProgressDao
import com.educalab.quimicatomix.data.local.dao.UserProfileDao
import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.LabEquipment
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.UnlockedEquipment
import com.educalab.quimicatomix.data.local.entity.UserBadge
import com.educalab.quimicatomix.domain.logic.CriteriaEvaluator
import com.educalab.quimicatomix.domain.model.PlayerStats
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de insignias y equipamiento de laboratorio coleccionable. Todo desbloqueo
 * se decide con [CriteriaEvaluator] sobre estadísticas reales, y se persiste de forma
 * permanente (nunca se "recalcula" en memoria en cada apertura de pantalla).
 */
class GamificationRepository(
    private val badgeDao: BadgeDao,
    private val equipmentDao: LabEquipmentDao,
    private val progressDao: ProgressDao,
    private val attemptDao: AttemptDao,
    private val userProfileDao: UserProfileDao
) {
    fun observeBadges(): Flow<List<Badge>> = badgeDao.observeAll()
    fun observeEarnedBadgeIds(userId: Long): Flow<List<String>> = badgeDao.observeEarnedIds(userId)

    fun observeEquipment(): Flow<List<LabEquipment>> = equipmentDao.observeAll()
    fun observeUnlockedEquipmentIds(userId: Long): Flow<List<String>> = equipmentDao.observeUnlockedIds(userId)

    suspend fun buildPlayerStats(userId: Long): PlayerStats {
        val profile = userProfileDao.getById(userId)
        val allProgress = progressDao.getAllForUser(userId)
        val mastered = allProgress.count { it.mastery == MasteryState.DOMINADO }
        val streak = run {
            val recent = attemptDao.getRecent(userId, 50)
            var s = 0
            for (a in recent) { if (a.success) s++ else break }
            s
        }
        return PlayerStats(
            experimentsCompleted = attemptDao.countSuccesses(userId),
            topicsMastered = mastered,
            currentSuccessStreak = streak,
            safetyScenariosCompleted = attemptDao.countSuccessfulSafetyScenarios(userId),
            moleculesBuilt = attemptDao.countSuccessfulMolecules(userId),
            level = profile?.level ?: 1,
            totalStars = attemptDao.sumStars(userId)
        )
    }

    /**
     * Evalúa y persiste todas las insignias y equipamiento recién desbloqueados.
     * Se llama tras cada finalización de experimento/molécula/escenario de seguridad.
     */
    suspend fun evaluateAndAwardUnlocks(
        userId: Long,
        stats: PlayerStats,
        allBadges: List<Badge>,
        allEquipment: List<LabEquipment>
    ): Pair<List<Badge>, List<LabEquipment>> {
        val earnedBadgeIds = badgeDao.getEarnedIds(userId).toSet()
        val unlockedEquipIds = equipmentDao.getUnlockedIds(userId).toSet()

        val newBadgeIds = CriteriaEvaluator.evaluateAll(
            allBadges.map { Triple(it.id, it.criteriaType, it.criteriaValue) },
            earnedBadgeIds,
            stats
        )
        val newEquipIds = CriteriaEvaluator.evaluateAll(
            allEquipment.map { Triple(it.id, it.unlockCriteriaType, it.unlockCriteriaValue) },
            unlockedEquipIds,
            stats
        )

        val now = System.currentTimeMillis()
        newBadgeIds.forEach { badgeDao.award(UserBadge(userId = userId, badgeId = it, earnedAt = now)) }
        newEquipIds.forEach { equipmentDao.unlock(UnlockedEquipment(userId = userId, equipmentId = it, unlockedAt = now)) }

        return allBadges.filter { it.id in newBadgeIds } to allEquipment.filter { it.id in newEquipIds }
    }
}
