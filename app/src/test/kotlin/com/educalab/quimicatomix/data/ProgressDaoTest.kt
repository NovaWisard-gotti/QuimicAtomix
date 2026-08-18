package com.educalab.quimicatomix.data

import androidx.test.core.app.ApplicationProvider
import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.local.entity.ChemicalTopic
import com.educalab.quimicatomix.data.local.entity.MasteryState
import com.educalab.quimicatomix.data.local.entity.Progress
import com.educalab.quimicatomix.data.local.entity.UserProfile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProgressDaoTest {

    private lateinit var db: AppDatabase
    private var userId: Long = 0

    @Before
    fun setUp() = runTest {
        db = AppDatabase.buildInMemory(ApplicationProvider.getApplicationContext())
        userId = db.userProfileDao().insert(UserProfile(alias = "Test", avatarId = 0, createdAt = 0L, lastActiveAt = 0L))
        db.chemicalTopicDao().insertAll(
            listOf(
                ChemicalTopic(
                    id = "estados",
                    title = "Estados",
                    shortDescription = "",
                    narrativeIntro = "",
                    iconKey = "",
                    colorHex = "#000000",
                    orderIndex = 0,
                    minLevelToUnlock = 0
                )
            )
        )
        db.progressDao().insertAll(
            listOf(Progress(userId = userId, topicId = "estados", experimentsTotal = 10, mastery = MasteryState.DISPONIBLE))
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `registerCompletion increments completed count and stars`() = runTest {
        db.progressDao().registerCompletion(userId, "estados", stars = 3, timestamp = 12345L)
        val progress = db.progressDao().get(userId, "estados")
        assertEquals(1, progress?.experimentsCompleted)
        assertEquals(3, progress?.starsTotal)
        assertEquals(12345L, progress?.lastPlayedAt)
    }

    @Test
    fun `registerCompletion accumulates across multiple calls`() = runTest {
        db.progressDao().registerCompletion(userId, "estados", stars = 2, timestamp = 1L)
        db.progressDao().registerCompletion(userId, "estados", stars = 3, timestamp = 2L)
        val progress = db.progressDao().get(userId, "estados")
        assertEquals(2, progress?.experimentsCompleted)
        assertEquals(5, progress?.starsTotal)
    }

    @Test
    fun `setMastery updates mastery state independently`() = runTest {
        db.progressDao().setMastery(userId, "estados", MasteryState.DOMINADO)
        val progress = db.progressDao().get(userId, "estados")
        assertEquals(MasteryState.DOMINADO, progress?.mastery)
    }

    @Test
    fun `get on unknown topic returns null`() = runTest {
        val progress = db.progressDao().get(userId, "tema_inexistente")
        assertEquals(null, progress)
    }

    @Test
    fun `getAllForUser returns only rows for the requested user`() = runTest {
        val otherUser = db.userProfileDao().insert(UserProfile(alias = "Otro", avatarId = 1, createdAt = 0L, lastActiveAt = 0L))
        db.progressDao().insertAll(listOf(Progress(userId = otherUser, topicId = "estados", experimentsTotal = 5)))
        val rowsForFirstUser = db.progressDao().getAllForUser(userId)
        assertEquals(1, rowsForFirstUser.size)
    }
}
