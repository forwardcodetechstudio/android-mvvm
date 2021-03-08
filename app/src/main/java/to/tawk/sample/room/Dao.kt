package to.tawk.sample.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import to.tawk.sample.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id>:since")
    suspend fun getUsers(since:Int): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<User>)


    @Query("UPDATE user SET notes=:notes WHERE id=:id")
    suspend fun updateNote(id: Int,notes:String?)

    @Query("SELECT notes FROM user WHERE id=:id")
    suspend fun getNote(id: Int): String?

}