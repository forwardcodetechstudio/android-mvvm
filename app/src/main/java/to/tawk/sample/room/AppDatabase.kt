package to.tawk.sample.room

import androidx.room.Database
import androidx.room.RoomDatabase
import to.tawk.sample.data.Note
import to.tawk.sample.data.User

@Database(entities = [User::class, Note::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usersDao(): UserDao
}