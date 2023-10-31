package com.example.burgers.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AssetsBurger.TABLE_NAME)
data class AssetsBurger(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "restaurant") val restaurant: String = "",
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "link") var link: String = "",
) {
    init {
        if(!link.startsWith("http")){
            link = "https://${this.link}"
        }
    }

    companion object {
        const val TABLE_NAME = "burgers"
    }
}