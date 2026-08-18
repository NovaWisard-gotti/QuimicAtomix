package com.educalab.quimicatomix.data

import androidx.test.core.app.ApplicationProvider
import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.local.entity.Attempt
import com.educalab.quimicatomix.data.local.entity.UserProfile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AttemptDaoTest {

    private lateinit var db: AppDatabase
    private var userId: Long = 0

    @Before
    fun setUp() = runTest {
        db = AppDatabase.buildInMemory(ApplicationProvider.getApplicationContext())
        userId = db.userProfileDao().insert(UserProfile(alias = "Test", avatarId = 0, createdAt = 0L, lastActiveAt = 0L))
    }

    @After
    fun tearDown() {
        db.close()
    }

    private suspend fun insertAttempt(success: Boolean, stars: Int, finishedAt: Long) {
        db.attemptDao().insert(
            Attempt(
                userId = userId, experimentId = "exp_test", startedAt = finishedAt - 100,
                finishedAt = finishedAt, success = success, starsEarned = stars, xpEarned = 10, mistakesCount = 0
            )
        )
    }

    @Test
    fun `countSuccesses only counts successful attempts`() = runTest {
        insertAttempt(success = true, stars = 3, finishedAt = 1L)
        insertAttempt(success = false, stars = 0, finishedAt = 2L)
        insertAttempt(success = true, stars = 2, finishedAt = 3L)
        assertEquals(2, db.attemptDao().countSuccesses(userId))
    }

    @Test
    fun `sumStars adds stars across all attempts`() = runTest {
        insertAttempt(success = true, stars = 3, finishedAt = 1L)
        insertAttempt(success = true, stars = 2, finishedAt = 2L)
        assertEquals(5, db.attemptDao().sumStars(userId))
    }

    @Test
    fun `sumStars on empty history returns zero, not null`() = runTest {
        assertEquals(0, db.attemptDao().sumStars(userId))
    }

    @Test
    fun `getRecent orders attempts by most recent first`() = runTest {
        insertAttempt(success = true, stars = 1, finishedAt = 1L)
        insertAttempt(success = true, stars = 1, finishedAt = 5L)
        insertAttempt(success = true, stars = 1, finishedAt = 3L)
        val recent = db.attemptDao().getRecent(userId, 10)
        assertEquals(5L, recent.first().finishedAt)
    }

    @Test
    fun `countAll counts both successful and failed attempts`() = runTest {
        insertAttempt(success = true, stars = 3, finishedAt = 1L)
        insertAttempt(success = false, stars = 0, finishedAt = 2L)
        assertEquals(2, db.attemptDao().countAll(userId))
    }
}
