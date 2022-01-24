package com.yashk9.githubapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yashk9.githubapp.model.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubRepoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repoList: List<Repo>)

    @Query("SELECT * FROM repos")
    fun getAllRepo(): PagingSource<Int, Repo>

    @Query("SELECT * FROM repos WHERE id = :id")
    fun getRepoById(id: Int): Flow<Repo>

    @Query("DELETE FROM repos")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM repos")
    suspend fun getCount(): Int

    @Query("SELECT * FROM repos where name LIKE '%' || :query || '%'")
    suspend fun queryRepo(query: String): List<Repo>
}