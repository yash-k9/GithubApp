package com.yashk9.githubapp.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yashk9.githubapp.data.db.AppDatabase
import com.yashk9.githubapp.data.remote.GithubApiService
import com.yashk9.githubapp.model.Repo
import com.yashk9.githubapp.repository.paging.RepoRemoteMediator
import com.yashk9.githubapp.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val githubApiService: GithubApiService,
    private val githubDatabase: AppDatabase
) {
    @ExperimentalPagingApi
    fun getOfflineCache(): Flow<PagingData<Repo>> {
        val pagingSource = { githubDatabase.getGithubRepoDao().getAllRepo() }
        return Pager(
            config = PagingConfig(pageSize = 50, maxSize = 150),
            remoteMediator =  RepoRemoteMediator(githubApiService, githubDatabase),
            pagingSourceFactory =  pagingSource
        ).flow
    }

    @ExperimentalCoroutinesApi
    suspend fun getQueryResponse(query: String): Result<List<Repo>, Exception>{
        return Result.build{ githubApiService.getQueryRepo("$query in:name,description").items }
    }
}