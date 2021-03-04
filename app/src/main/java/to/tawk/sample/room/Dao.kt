package to.tawk.sample.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import to.tawk.sample.data.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE id>:since LIMIT :pageSize")
    suspend fun getUsers(since:Int, pageSize:Int): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllUsers(users: List<User>)


    @Query("UPDATE user SET notes=:notes WHERE id=:id")
    suspend fun updateNote(id: Int,notes:String)

}