package to.tawk.sample.data

import android.text.TextUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "avatar_url") val avatar_url: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "notes") val notes: String?,
){

    fun hasNotes():Boolean{
        return !TextUtils.isEmpty(this.notes)
    }
}
