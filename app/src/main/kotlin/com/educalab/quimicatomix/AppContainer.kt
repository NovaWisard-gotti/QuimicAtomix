package com.educalab.quimicatomix

import android.content.Context
import com.educalab.quimicatomix.data.local.ActiveProfileStore
import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.repository.ContentRepository
import com.educalab.quimicatomix.data.repository.GamificationRepository
import com.educalab.quimicatomix.data.repository.ProfileRepository
import com.educalab.quimicatomix.data.repository.ProgressRepository
import com.educalab.quimicatomix.util.SoundHapticsManager

/**
 * Contenedor de dependencias manual y explícito (sin frameworks de inyección externos,
 * para mantener el proyecto simple, transparente y 100% offline). Se instancia una única
 * vez desde [QuimicAtomixApp] y se inyecta a los ViewModel mediante sus factories.
 */
class AppContainer(context: Context) {

    val database: AppDatabase = AppDatabase.getInstance(context)
    val activeProfileStore: ActiveProfileStore = ActiveProfileStore(context)

    /** Id del perfil activo en esta sesión. Se establece durante el arranque o el onboarding. */
    var currentUserId: Long = NO_USER
        private set

    fun setActiveUser(userId: Long) {
        currentUserId = userId
    }

    /** Cambia de perfil activo (usado al alternar entre perfiles o crear uno nuevo) y lo recuerda. */
    suspend fun switchProfile(userId: Long) {
        currentUserId = userId
        profileRepository.touch(userId)
        activeProfileStore.save(userId)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(database.userProfileDao())
    }

    val contentRepository: ContentRepository by lazy {
        ContentRepository(
            topicDao = database.chemicalTopicDao(),
            substanceDao = database.virtualSubstanceDao(),
            experimentDao = database.experimentDao(),
            atomDao = database.atomDao(),
            moleculeDao = database.moleculeChallengeDao(),
            safetyDao = database.safetyScenarioDao()
        )
    }

    val progressRepository: ProgressRepository by lazy {
        ProgressRepository(database.attemptDao(), database.progressDao(), database.experimentDao())
    }

    val gamificationRepository: GamificationRepository by lazy {
        GamificationRepository(
            badgeDao = database.badgeDao(),
            equipmentDao = database.labEquipmentDao(),
            progressDao = database.progressDao(),
            attemptDao = database.attemptDao(),
            userProfileDao = database.userProfileDao()
        )
    }

    val soundHapticsManager: SoundHapticsManager by lazy { SoundHapticsManager(context) }

    /**
     * Siembra el contenido si hace falta y determina si existe un perfil ya creado.
     * @return true si YA había un perfil (se puede saltar el onboarding), false si es la
     * primera instalación y se debe mostrar el onboarding para crear alias + avatar.
     */
    suspend fun bootstrap(): Boolean {
        com.educalab.quimicatomix.data.seed.DatabaseSeeder.seedIfNeeded(database)
        val hasProfile = profileRepository.hasProfile()
        if (hasProfile) {
            val storedId = activeProfileStore.read()
            val storedProfile = storedId?.let { profileRepository.getProfile(it) }
            currentUserId = storedProfile?.id ?: profileRepository.getOrCreateDefaultId()
            profileRepository.touch(currentUserId)
            activeProfileStore.save(currentUserId)
        }
        return hasProfile
    }

    companion object {
        const val NO_USER = -1L
    }
}
