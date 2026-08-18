package com.educalab.quimicatomix.data.repository

import com.educalab.quimicatomix.data.local.dao.AtomDao
import com.educalab.quimicatomix.data.local.dao.ChemicalTopicDao
import com.educalab.quimicatomix.data.local.dao.ExperimentDao
import com.educalab.quimicatomix.data.local.dao.MoleculeChallengeDao
import com.educalab.quimicatomix.data.local.dao.SafetyScenarioDao
import com.educalab.quimicatomix.data.local.dao.VirtualSubstanceDao
import com.educalab.quimicatomix.data.local.entity.Atom
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import com.educalab.quimicatomix.data.local.entity.Experiment
import com.educalab.quimicatomix.data.local.entity.ExperimentCombination
import com.educalab.quimicatomix.data.local.entity.ExperimentStep
import com.educalab.quimicatomix.data.local.entity.MoleculeChallenge
import com.educalab.quimicatomix.data.local.entity.SafetyScenario
import com.educalab.quimicatomix.data.local.entity.VirtualSubstance
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de solo lectura sobre el contenido educativo semilla (temas, sustancias,
 * experimentos, átomos, moléculas y escenarios de seguridad). El contenido se inserta una
 * única vez por [com.educalab.quimicatomix.data.seed.DatabaseSeeder].
 */
class ContentRepository(
    private val topicDao: ChemicalTopicDao,
    private val substanceDao: VirtualSubstanceDao,
    private val experimentDao: ExperimentDao,
    private val atomDao: AtomDao,
    private val moleculeDao: MoleculeChallengeDao,
    private val safetyDao: SafetyScenarioDao
) {
    fun observeTopics(): Flow<List<ChemicalTopic>> = topicDao.observeAll()
    suspend fun getTopics(): List<ChemicalTopic> = topicDao.getAll()
    suspend fun getTopic(id: String): ChemicalTopic? = topicDao.getById(id)

    fun observeExperimentsByTopic(topicId: String): Flow<List<Experiment>> = experimentDao.observeByTopic(topicId)
    suspend fun getExperiment(id: String): Experiment? = experimentDao.getById(id)
    suspend fun getSteps(experimentId: String): List<ExperimentStep> = experimentDao.getSteps(experimentId)
    suspend fun getCombinations(experimentId: String): List<ExperimentCombination> = experimentDao.getCombinations(experimentId)
    suspend fun countExperiments(): Int = experimentDao.count()
    suspend fun countExperimentsByTopic(topicId: String): Int = experimentDao.countByTopic(topicId)

    fun observeSubstancesByTopic(topicId: String): Flow<List<VirtualSubstance>> = substanceDao.observeByTopic(topicId)
    suspend fun getSubstance(id: String): VirtualSubstance? = substanceDao.getById(id)
    suspend fun getSubstances(ids: List<String>): List<VirtualSubstance> = substanceDao.getByIds(ids)

    fun observeAtoms(): Flow<List<Atom>> = atomDao.observeAll()
    suspend fun getAtoms(): List<Atom> = atomDao.getAll()
    suspend fun getAtom(id: String): Atom? = atomDao.getById(id)

    fun observeMolecules(): Flow<List<MoleculeChallenge>> = moleculeDao.observeAll()
    suspend fun getMolecule(id: String): MoleculeChallenge? = moleculeDao.getById(id)

    fun observeSafetyScenarios(): Flow<List<SafetyScenario>> = safetyDao.observeAll()
    suspend fun getSafetyScenarios(): List<SafetyScenario> = safetyDao.getAll()
    suspend fun countSafetyScenarios(): Int = safetyDao.count()
}
