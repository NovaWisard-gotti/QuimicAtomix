package com.educalab.quimicatomix.data.repository

import com.educalab.quimicatomix.data.local.dao.AttemptDao
import com.educalab.quimicatomix.data.local.dao.ExperimentDao
import com.educalab.quimicatomix.data.local.dao.ProgressDao
import com.educalab.quimicatomix.data.local.entity.Attempt
import com.educalab.quimicatomix.data.local.entity.ExperimentResult
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.OutcomeType
import com.educalab.quimicatomix.data.local.entity.Progress
import com.educalab.quimicatomix.domain.logic.ProgressEngine
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de progreso e historial. Cada finalización de experimento se persiste como
 * [Attempt] + [ExperimentResult] reales (nunca se calcula progreso solo en memoria), y el
 * estado de dominio del tema se recalcula con [ProgressEngine] a partir de esos datos.
 */
class ProgressRepository(
    private val attemptDao: AttemptDao,
    private val progressDao: ProgressDao,
    private val experimentDao: ExperimentDao
) {
    fun observeProgress(userId: Long): Flow<List<Progress>> = progressDao.observeForUser(userId)

    suspend fun getProgress(userId: Long, topicId: String): Progress? = progressDao.get(userId, topicId)

    fun observeAttempts(userId: Long): Flow<List<Attempt>> = attemptDao.observeForUser(userId)

    suspend fun countSuccesses(userId: Long): Int = attemptDao.countSuccesses(userId)

    suspend fun currentSuccessStreak(userId: Long): Int {
        val recent = attemptDao.getRecent(userId, 50)
        var streak = 0
        for (attempt in recent) {
            if (attempt.success) streak++ else break
        }
        return streak
    }

    /**
     * Registra la finalización real de un experimento: guarda el intento, el resultado
     * consolidado, actualiza el progreso agregado del tema y recalcula su estado de dominio.
     */
    suspend fun registerExperimentCompletion(
        userId: Long,
        topicId: String,
        experimentId: String,
        outcome: OutcomeType,
        stars: Int,
        xpEarned: Int,
        mistakes: Int,
        startedAt: Long,
        finishedAt: Long,
        playerLevel: Int,
        topicMinLevel: Int
    ): Progress {
        val attemptId = attemptDao.insert(
            Attempt(
                userId = userId,
                experimentId = experimentId,
                startedAt = startedAt,
                finishedAt = finishedAt,
                success = outcome != OutcomeType.REINTENTAR,
                starsEarned = stars,
                xpEarned = xpEarned,
                mistakesCount = mistakes
            )
        )
        experimentDao.insertResult(
            ExperimentResult(
                attemptId = attemptId,
                experimentId = experimentId,
                outcome = outcome,
                starsEarned = stars,
                xpEarned = xpEarned,
                mistakesCount = mistakes,
                timestamp = finishedAt
            )
        )

        if (outcome != OutcomeType.REINTENTAR) {
            progressDao.registerCompletion(userId, topicId, stars, finishedAt)
        }

        val updated = progressDao.get(userId, topicId) ?: error("Progreso no inicializado para $topicId")
        val mastery = ProgressEngine.computeMastery(
            completed = updated.experimentsCompleted,
            total = updated.experimentsTotal,
            starsTotal = updated.starsTotal,
            isUnlockedByLevel = playerLevel >= topicMinLevel
        )
        if (mastery != updated.mastery) {
            progressDao.setMastery(userId, topicId, mastery)
        }
        return updated.copy(mastery = mastery)
    }

    /** Registra un intento sobre un reto de construcción molecular (no afecta Progress de tema). */
    suspend fun registerMoleculeAttempt(
        userId: Long,
        moleculeChallengeId: String,
        success: Boolean,
        stars: Int,
        xpEarned: Int,
        mistakes: Int,
        startedAt: Long,
        finishedAt: Long
    ) {
        attemptDao.insert(
            Attempt(
                userId = userId,
                moleculeChallengeId = moleculeChallengeId,
                startedAt = startedAt,
                finishedAt = finishedAt,
                success = success,
                starsEarned = stars,
                xpEarned = xpEarned,
                mistakesCount = mistakes
            )
        )
    }

    /** Registra un intento sobre un escenario de seguridad (no afecta Progress de tema). */
    suspend fun registerSafetyAttempt(
        userId: Long,
        safetyScenarioId: String,
        success: Boolean,
        xpEarned: Int,
        startedAt: Long,
        finishedAt: Long
    ) {
        attemptDao.insert(
            Attempt(
                userId = userId,
                safetyScenarioId = safetyScenarioId,
                startedAt = startedAt,
                finishedAt = finishedAt,
                success = success,
                starsEarned = if (success) 3 else 0,
                xpEarned = xpEarned,
                mistakesCount = if (success) 0 else 1
            )
        )
    }

    /** Recalcula (sin registrar un nuevo intento) el estado bloqueado/disponible tras subir de nivel. */
    suspend fun refreshUnlockState(userId: Long, topicId: String, playerLevel: Int, topicMinLevel: Int) {
        val current = progressDao.get(userId, topicId) ?: return
        val mastery = ProgressEngine.computeMastery(
            completed = current.experimentsCompleted,
            total = current.experimentsTotal,
            starsTotal = current.starsTotal,
            isUnlockedByLevel = playerLevel >= topicMinLevel
        )
        if (mastery != current.mastery) progressDao.setMastery(userId, topicId, mastery)
    }
}
