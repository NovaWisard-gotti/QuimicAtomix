package com.educalab.quimicatomix.data

import androidx.test.core.app.ApplicationProvider
import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.seed.DatabaseSeeder
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Verifica que el contenido semilla cumple los mínimos exigidos por la especificación
 * (55 experimentos, 35 escenarios de seguridad...) y que sembrar dos veces NO duplica datos.
 */
@RunWith(RobolectricTestRunner::class)
class DatabaseSeederTest {

    private lateinit var db: AppDatabase

    @Before
    fun setUp() {
        db = AppDatabase.buildInMemory(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `seeding populates at least the minimum required content`() = runTest {
        DatabaseSeeder.seedIfNeeded(db)

        assertEquals(6, db.chemicalTopicDao().count())
        assertTrue(db.experimentDao().count() >= 55)
        assertTrue(db.safetyScenarioDao().count() >= 35)
        assertTrue(db.atomDao().count() >= 10)
        assertTrue(db.moleculeChallengeDao().count() >= 10)
        assertTrue(db.badgeDao().count() >= 8)
        assertTrue(db.labEquipmentDao().count() >= 8)
        assertTrue(db.virtualSubstanceDao().count() > 0)
    }

    @Test
    fun `seeding twice does not duplicate rows (idempotent)`() = runTest {
        DatabaseSeeder.seedIfNeeded(db)
        val firstCount = db.experimentDao().count()
        DatabaseSeeder.seedIfNeeded(db)
        val secondCount = db.experimentDao().count()
        assertEquals(firstCount, secondCount)
    }

    @Test
    fun `every experiment has at least one step`() = runTest {
        DatabaseSeeder.seedIfNeeded(db)
        val experiments = db.experimentDao().getAll()
        for (experiment in experiments) {
            val steps = db.experimentDao().getSteps(experiment.id)
            assertTrue("Experiment ${experiment.code} has no steps", steps.isNotEmpty())
        }
    }

    @Test
    fun `mixture experiments reference a valid combination`() = runTest {
        DatabaseSeeder.seedIfNeeded(db)
        val mixtureExperiments = db.experimentDao().getAll()
            .filter { it.type == com.educalab.quimicatomix.data.local.entity.ExperimentType.MEZCLA }
        for (experiment in mixtureExperiments) {
            val combos = db.experimentDao().getCombinations(experiment.id)
            assertTrue("Mixture experiment ${experiment.code} has no combination", combos.isNotEmpty())
        }
    }
}
