package to.tawk.sample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "avatar_url") val avatar: String,
    @ColumnInfo(name = "url") val url: String,
    var hasNotes:Boolean=false
)
