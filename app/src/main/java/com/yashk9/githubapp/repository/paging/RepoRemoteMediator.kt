package com.yashk9.githubapp.repository.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.yashk9.githubapp.data.db.AppDatabase
import com.yashk9.githubapp.data.local.GithubRepoDao
import com.yashk9.githubapp.data.local.RemoteKeysDao
import com.yashk9.githubapp.data.remote.GithubApiService
import com.yashk9.githubapp.model.RemoteKeys
import com.yashk9.githubapp.model.Repo
import kotlinx.coroutines.delay

@ExperimentalPagingApi
class RepoRemoteMediator(
    private val githubApiService: GithubApiService,
    private val database: AppDatabase
): RemoteMediator<Int, Repo>() {
    companion object{
        private const val TAG = "RepoRemoteMediator"
    }

    private val repoDao: GithubRepoDao = database.getGithubRepoDao()
    private val remoteKeysDao: RemoteKeysDao = database.getRemoteKeysDao()

    //If data is present in the database, allow user to refresh else launch refresh on open
    override suspend fun initialize(): InitializeAction {
        return if(repoDao.getCount() > 0){
            InitializeAction.SKIP_INITIAL_REFRESH
        }else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    //Fetches the current page and stores it in the database
    //RemoteKey holds the data of prev and next page for the item
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Repo>): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.prevKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            //Request GithubAPIService for the Repositories
            val response = githubApiService.getRepoByUser("square", state.config.pageSize, currentPage)
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            //Invalidate previous data on Refresh and add latest RepoItem and Respective key in Database
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    repoDao.deleteAll()
                    remoteKeysDao.deleteAll()
                }
                val keys = response.map { repo ->
                    RemoteKeys(
                        id = repo.id,
                        prevKey = prevPage,
                        nextKey = nextPage
                    )
                }

                remoteKeysDao.insertAll(keys)
                repoDao.insertAll(response)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            Log.d(TAG, "load: ${e.stackTrace} ${e.message}")
            return MediatorResult.Error(e)
        }
    }

    //These methods get the respective page for the current key based on load state
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Repo>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysById(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Repo>
    ): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                remoteKeysDao.getRemoteKeysById(id = repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Repo>
    ): RemoteKeys? {
        val s = state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                remoteKeysDao.getRemoteKeysById(id = repo.id)
            }
    }
}