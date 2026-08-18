package com.educalab.quimicatomix.ui.navigation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.educalab.quimicatomix.QuimicAtomixApp
import com.educalab.quimicatomix.ui.screens.equipment.ProgressEquipmentViewModel
import com.educalab.quimicatomix.ui.screens.experiment.ExperimentPlayViewModel
import com.educalab.quimicatomix.ui.screens.home.HomeViewModel
import com.educalab.quimicatomix.ui.screens.molecule.MoleculeBuilderViewModel
import com.educalab.quimicatomix.ui.screens.onboarding.OnboardingViewModel
import com.educalab.quimicatomix.ui.screens.profile.ProfileViewModel
import com.educalab.quimicatomix.ui.screens.safety.SafetyViewModel
import com.educalab.quimicatomix.ui.screens.topic.TopicDetailViewModel

/**
 * Fábrica de ViewModels sin frameworks de inyección de dependencias externos: cada
 * ViewModel se construye a mano leyendo los repositorios desde [AppContainer], siguiendo
 * el patrón oficial recomendado por Android (CreationExtras + viewModelFactory).
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            OnboardingViewModel(quimicAtomixApplication().container)
        }
        initializer {
            HomeViewModel(quimicAtomixApplication().container)
        }
        initializer {
            TopicDetailViewModel(quimicAtomixApplication().container, createSavedStateHandle())
        }
        initializer {
            ExperimentPlayViewModel(quimicAtomixApplication().container, createSavedStateHandle())
        }
        initializer {
            MoleculeBuilderViewModel(quimicAtomixApplication().container, createSavedStateHandle())
        }
        initializer {
            SafetyViewModel(quimicAtomixApplication().container, createSavedStateHandle())
        }
        initializer {
            ProgressEquipmentViewModel(quimicAtomixApplication().container)
        }
        initializer {
            ProfileViewModel(quimicAtomixApplication().container)
        }
    }
}

fun CreationExtras.quimicAtomixApplication(): QuimicAtomixApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuimicAtomixApp)
