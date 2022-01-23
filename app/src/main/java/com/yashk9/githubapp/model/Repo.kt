package com.yashk9.githubapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val id: Long,
    val name: String,
    val full_name: String,
    @Embedded(prefix = "owner_")
    val owner: Owner,
    val description: String?,
    val html_url: String,
    val stargazers_count: Int,
    val forks_count: Int,
    val language: String?
)