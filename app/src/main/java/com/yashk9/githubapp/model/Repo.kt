package com.yashk9.githubapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey(autoGenerate = false)
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