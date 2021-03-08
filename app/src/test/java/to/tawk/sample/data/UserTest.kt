package to.tawk.sample.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserTest {

    private lateinit var user: User

    @Before
    fun setUp() {
        user = User(1, "test", "test.com/abc.png", "test.com/test", "notes", 10, 100)
    }

    @Test
    fun hasNotes() {
        assertEquals(true, user.notes.isNullOrEmpty().not())
    }

    @Test
    fun hasNoNotes() {
        assertEquals(true, user.notes.isNullOrEmpty())
    }
}