package com.yashk9.githubapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yashk9.githubapp.data.local.GithubRepoDao
import com.yashk9.githubapp.data.local.RemoteKeysDao
import com.yashk9.githubapp.model.RemoteKeys
import com.yashk9.githubapp.model.Repo


@Database(entities = [Repo::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getGithubRepoDao(): GithubRepoDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}