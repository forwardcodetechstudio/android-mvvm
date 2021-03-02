package to.tawk.sample.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import to.tawk.sample.data.Note
import to.tawk.sample.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>

    @Insert
    suspend fun insertUser(user: User)

    @Insert
    suspend fun insertAllUsers(users: List<User>)

    @Insert
    suspend fun insertNote(note: Note)

    @Query("UPDATE note SET notes=:notes WHERE id=:id")
    suspend fun updateNote(id: Int,notes:String)

}