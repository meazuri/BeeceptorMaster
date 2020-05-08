package com.seint.beeceptor.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Article (
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id : Int,
    @ColumnInfo(name = "title")
    var title : String,
    @ColumnInfo(name = "last_update")
    var last_update : String ,
    @ColumnInfo(name = "short_description")
    var short_description : String,
    @ColumnInfo(name = "avatar")
    var avatar : String,
    @ColumnInfo(name = "text")
    var text : String?
) : Serializable