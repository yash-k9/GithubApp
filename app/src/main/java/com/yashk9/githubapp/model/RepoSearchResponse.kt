package com.yashk9.githubapp.model

data class RepoSearchResponse(
    val total_count: Int,
    val items: List<Repo>
)