package com.educalab.quimicatomix.ui.navigation

/** Rutas de navegación. Se mantienen como constantes simples y explícitas (sin sealed extra). */
object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val TOPIC_DETAIL = "topic/{topicId}"
    const val EXPERIMENT_PLAY = "experiment/{experimentId}"
    const val MOLECULE_HUB = "molecules"
    const val MOLECULE_PLAY = "molecule/{moleculeId}"
    const val SAFETY_HUB = "safety"
    const val SAFETY_PLAY = "safety/{scenarioId}"
    const val PROGRESS_EQUIPMENT = "progress_equipment"
    const val PROFILE = "profile"

    fun topicDetail(topicId: String) = "topic/$topicId"
    fun experimentPlay(experimentId: String) = "experiment/$experimentId"
    fun moleculePlay(moleculeId: String) = "molecule/$moleculeId"
    fun safetyPlay(scenarioId: String) = "safety/$scenarioId"
}
