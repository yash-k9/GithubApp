package com.yashk9.githubapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owner")
data class Owner(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val login: String,
    val avatar_url: String,
    var url: String
)