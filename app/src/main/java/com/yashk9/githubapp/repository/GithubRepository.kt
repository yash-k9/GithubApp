package com.yashk9.githubapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yashk9.githubapp.data.db.AppDatabase
import com.yashk9.githubapp.data.remote.GithubApiService
import com.yashk9.githubapp.model.Repo
import com.yashk9.githubapp.repository.paging.RepoRemoteMediator
import com.yashk9.githubapp.util.Constants.PAGE_SIZE
import com.yashk9.githubapp.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubApiService: GithubApiService,
    private val githubDatabase: AppDatabase
) {
    //Data handled by the RepoRemoteMediator and stored locally on Room
    //Database serves as Single Source of Truth
    @ExperimentalPagingApi
    fun getOfflineCache(): Flow<PagingData<Repo>> {
        val pagingSource = { githubDatabase.getGithubRepoDao().getAllRepo() }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, maxSize = PAGE_SIZE * 3),
            remoteMediator =  RepoRemoteMediator(githubApiService, githubDatabase),
            pagingSourceFactory =  pagingSource
        ).flow
    }

    //Query Fetched from the Database on Submit
    suspend fun getQueryFromDatabase(query: String): Result<List<Repo>, Exception>{
        return Result.build { githubDatabase.getGithubRepoDao().queryRepo(query) }
    }

    //Query Fetched from the GithubAPIService(RETROFIT)
    @ExperimentalCoroutinesApi
    suspend fun getQueryResponse(query: String): Result<List<Repo>, Exception>{
        return Result.build{ githubApiService.getQueryRepo("$query in:name,description").items }
    }
}