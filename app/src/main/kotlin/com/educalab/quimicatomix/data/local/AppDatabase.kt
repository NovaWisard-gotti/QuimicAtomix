package com.educalab.quimicatomix.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.educalab.quimicatomix.data.local.converters.Converters
import com.educalab.quimicatomix.data.local.dao.AtomDao
import com.educalab.quimicatomix.data.local.dao.AttemptDao
import com.educalab.quimicatomix.data.local.dao.BadgeDao
import com.educalab.quimicatomix.data.local.dao.ChemicalTopicDao
import com.educalab.quimicatomix.data.local.dao.ExperimentDao
import com.educalab.quimicatomix.data.local.dao.LabEquipmentDao
import com.educalab.quimicatomix.data.local.dao.MoleculeChallengeDao
import com.educalab.quimicatomix.data.local.dao.ProgressDao
import com.educalab.quimicatomix.data.local.dao.SafetyScenarioDao
import com.educalab.quimicatomix.data.local.dao.UserProfileDao
import com.educalab.quimicatomix.data.local.dao.VirtualSubstanceDao
import com.educalab.quimicatomix.data.local.entity.Atom
import com.educalab.quimicatomix.data.local.entity.Attempt
import com.educalab.quimicatomix.data.local.entity.Badge
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import com.educalab.quimicatomix.data.local.entity.Experiment
import com.educalab.quimicatomix.data.local.entity.ExperimentCombination
import com.educalab.quimicatomix.data.local.entity.ExperimentResult
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import com.educalab.quimicatomix.data.local.entity.LabEquipment
import com.educalab.quimicatomix.data.local.entity.MoleculeChallenge
import com.educalab.quimicatomix.data.local.entity.Progress
import com.educalab.quimicatomix.data.local.entity.SafetyScenario
import com.educalab.quimicatomix.data.local.entity.SubstanceProperty
import com.educalab.quimicatomix.data.local.entity.UnlockedEquipment
import com.educalab.quimicatomix.data.local.entity.UserBadge
import com.educalab.quimicatomix.data.local.entity.UserProfile
import com.educalab.quimicatomix.data.local.entity.VirtualSubstance

/**
 * Base de datos Room local de QuimicAtomix. 100% offline: no hay ningún componente de red,
 * autenticación en la nube ni sincronización. Todo el estado del jugador vive únicamente
 * en el dispositivo.
 */
@Database(
    entities = [
        UserProfile::class,
        ChemicalTopic::class,
        VirtualSubstance::class,
        SubstanceProperty::class,
        Experiment::class,
        ExperimentStep::class,
        ExperimentCombination::class,
        ExperimentResult::class,
        Atom::class,
        MoleculeChallenge::class,
        SafetyScenario::class,
        Attempt::class,
        LabEquipment::class,
        UnlockedEquipment::class,
        Progress::class,
        Badge::class,
        UserBadge::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao
    abstract fun chemicalTopicDao(): ChemicalTopicDao
    abstract fun virtualSubstanceDao(): VirtualSubstanceDao
    abstract fun experimentDao(): ExperimentDao
    abstract fun atomDao(): AtomDao
    abstract fun moleculeChallengeDao(): MoleculeChallengeDao
    abstract fun safetyScenarioDao(): SafetyScenarioDao
    abstract fun attemptDao(): AttemptDao
    abstract fun labEquipmentDao(): LabEquipmentDao
    abstract fun progressDao(): ProgressDao
    abstract fun badgeDao(): BadgeDao

    companion object {
        private const val DATABASE_NAME = "quimicatomix.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // No hay migraciones reales todavía (versión 1). Se documenta la política
                    // en docs/MANUAL_TECNICO.md para versiones futuras.
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                    .also { INSTANCE = it }
            }
        }

        /** Usado únicamente por tests para crear instancias en memoria aisladas. */
        fun buildInMemory(context: Context): AppDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }
}
