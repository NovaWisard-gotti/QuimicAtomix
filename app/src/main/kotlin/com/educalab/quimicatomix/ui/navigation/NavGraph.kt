package com.educalab.quimicatomix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.educalab.quimicatomix.ui.screens.equipment.ProgressEquipmentScreen
import com.educalab.quimicatomix.ui.screens.experiment.ExperimentPlayScreen
import com.educalab.quimicatomix.ui.screens.home.HomeScreen
import com.educalab.quimicatomix.ui.screens.molecule.MoleculeBuilderScreen
import com.educalab.quimicatomix.ui.screens.molecule.MoleculeHubScreen
import com.educalab.quimicatomix.ui.screens.onboarding.OnboardingScreen
import com.educalab.quimicatomix.ui.screens.profile.CreateProfileScreen
import com.educalab.quimicatomix.ui.screens.profile.ProfileScreen
import com.educalab.quimicatomix.ui.screens.safety.SafetyHubScreen
import com.educalab.quimicatomix.ui.screens.safety.SafetyPlayScreen
import com.educalab.quimicatomix.ui.screens.topic.TopicDetailScreen

@Composable
fun QuimicAtomixNavGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
    onProfileChanged: () -> Unit = {}
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onOpenTopic = { topicId -> navController.navigate(Routes.topicDetail(topicId)) },
                onOpenMolecules = { navController.navigate(Routes.MOLECULE_HUB) },
                onOpenSafety = { navController.navigate(Routes.SAFETY_HUB) },
                onOpenProgress = { navController.navigate(Routes.PROGRESS_EQUIPMENT) },
                onOpenProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.TOPIC_DETAIL) {
            TopicDetailScreen(
                onBack = { navController.popBackStack() },
                onOpenExperiment = { experimentId -> navController.navigate(Routes.experimentPlay(experimentId)) }
            )
        }
        composable(Routes.EXPERIMENT_PLAY) {
            ExperimentPlayScreen(
                onBack = { navController.popBackStack() },
                onFinished = { navController.popBackStack() }
            )
        }
        composable(Routes.MOLECULE_HUB) {
            MoleculeHubScreen(
                onBack = { navController.popBackStack() },
                onOpenChallenge = { moleculeId -> navController.navigate(Routes.moleculePlay(moleculeId)) }
            )
        }
        composable(Routes.MOLECULE_PLAY) {
            MoleculeBuilderScreen(
                onBack = { navController.popBackStack() },
                onFinished = { navController.popBackStack() }
            )
        }
        composable(Routes.SAFETY_HUB) {
            SafetyHubScreen(
                onBack = { navController.popBackStack() },
                onOpenScenario = { scenarioId -> navController.navigate(Routes.safetyPlay(scenarioId)) }
            )
        }
        composable(Routes.SAFETY_PLAY) {
            SafetyPlayScreen(
                onBack = { navController.popBackStack() },
                onFinished = { navController.popBackStack() }
            )
        }
        composable(Routes.PROGRESS_EQUIPMENT) {
            ProgressEquipmentScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onCreateProfile = { navController.navigate(Routes.CREATE_PROFILE) },
                onProfileChanged = onProfileChanged
            )
        }
        composable(Routes.CREATE_PROFILE) {
            CreateProfileScreen(
                onBack = { navController.popBackStack() },
                onCreated = onProfileChanged
            )
        }
    }
}
