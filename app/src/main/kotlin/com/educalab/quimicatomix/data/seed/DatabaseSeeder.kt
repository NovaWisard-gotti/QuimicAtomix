package com.educalab.quimicatomix.data.seed

import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.Progress

/**
 * Inserta el contenido educativo semilla UNA sola vez (primera instalación). Se ejecuta
 * dentro de una transacción implícita por operación; cada DAO usa OnConflictStrategy.IGNORE
 * para que reinstalar/reabrir la app nunca duplique datos.
 */
object DatabaseSeeder {

    suspend fun seedIfNeeded(db: AppDatabase) {
        if (db.chemicalTopicDao().count() > 0) return // ya sembrado

        db.chemicalTopicDao().insertAll(SeedTopics.list)
        db.virtualSubstanceDao().insertAll(SeedSubstances.list)
        db.virtualSubstanceDao().insertProperties(SeedSubstanceProperties.list)
        db.experimentDao().insertAll(SeedExperiments.list)
        db.experimentDao().insertSteps(SeedExperiments.steps)
        db.experimentDao().insertCombinations(SeedExperiments.combinations)
        db.atomDao().insertAll(SeedAtoms.list)
        db.moleculeChallengeDao().insertAll(SeedMolecules.list)
        db.safetyScenarioDao().insertAll(SeedSafetyScenarios.list)
        db.badgeDao().insertAll(SeedBadgesAndEquipment.badges)
        db.labEquipmentDao().insertAll(SeedBadgesAndEquipment.equipment)
    }

    /** Crea las filas de Progress (una por tema) para un usuario recién creado. */
    suspend fun initializeProgressForUser(db: AppDatabase, userId: Long) {
        val topics = db.chemicalTopicDao().getAll()
        val rows = topics.map { topic ->
            val total = db.experimentDao().countByTopic(topic.id)
            Progress(
                userId = userId,
                topicId = topic.id,
                experimentsCompleted = 0,
                experimentsTotal = total,
                starsTotal = 0,
                mastery = if (topic.minLevelToUnlock <= 1) MasteryState.DISPONIBLE else MasteryState.BLOQUEADO,
                lastPlayedAt = null
            )
        }
        db.progressDao().insertAll(rows)
    }
}
