package pl.maciejnowak.commonobjects.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeCreation(
    @PrimaryKey @ColumnInfo(name = "table_name") val tableName: String,
    val timestamp: Long
)