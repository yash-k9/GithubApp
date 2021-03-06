package com.yashk9.githubapp.data.remote

import com.yashk9.githubapp.model.Repo
import com.yashk9.githubapp.model.RepoSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    //Fetch the Repositories for a user
    @GET("/users/{name}/repos?q=sort:stars")
    suspend fun getRepoByUser(
        @Path("name") name: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ): List<Repo>

    //Query the API for the repository
    @GET("search/repositories")
    suspend fun getQueryRepo(
        @Query("q") query: String,
        @Query("per_page") per_page: Int = 100
    ): RepoSearchResponse
}
