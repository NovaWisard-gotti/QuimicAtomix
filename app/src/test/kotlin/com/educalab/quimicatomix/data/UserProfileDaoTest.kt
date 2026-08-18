package com.educalab.quimicatomix.data

import androidx.test.core.app.ApplicationProvider
import com.educalab.quimicatomix.data.local.AppDatabase
import com.educalab.quimicatomix.data.local.entity.UserProfile
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserProfileDaoTest {

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
    fun `insert and retrieve profile by id`() = runTest {
        val id = db.userProfileDao().insert(
            UserProfile(alias = "Test", avatarId = 2, createdAt = 1000L, lastActiveAt = 1000L)
        )
        val loaded = db.userProfileDao().getById(id)
        assertEquals("Test", loaded?.alias)
        assertEquals(2, loaded?.avatarId)
    }

    @Test
    fun `getById on empty database returns null`() = runTest {
        assertNull(db.userProfileDao().getById(999L))
    }

    @Test
    fun `addXp increments totalXp without overwriting it`() = runTest {
        val id = db.userProfileDao().insert(
            UserProfile(alias = "Test", avatarId = 0, createdAt = 0L, lastActiveAt = 0L, totalXp = 10)
        )
        db.userProfileDao().addXp(id, 25)
        val loaded = db.userProfileDao().getById(id)
        assertEquals(35, loaded?.totalXp)
    }

    @Test
    fun `setSoundEnabled updates only the sound flag`() = runTest {
        val id = db.userProfileDao().insert(
            UserProfile(alias = "Test", avatarId = 0, createdAt = 0L, lastActiveAt = 0L, soundEnabled = true, hapticsEnabled = true)
        )
        db.userProfileDao().setSoundEnabled(id, false)
        val loaded = db.userProfileDao().getById(id)
        assertEquals(false, loaded?.soundEnabled)
        assertEquals(true, loaded?.hapticsEnabled)
    }

    @Test
    fun `count reflects number of inserted profiles`() = runTest {
        assertEquals(0, db.userProfileDao().count())
        db.userProfileDao().insert(UserProfile(alias = "A", avatarId = 0, createdAt = 0L, lastActiveAt = 0L))
        assertEquals(1, db.userProfileDao().count())
    }
}
