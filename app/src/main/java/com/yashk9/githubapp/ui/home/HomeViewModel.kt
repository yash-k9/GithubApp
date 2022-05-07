package com.yashk9.githubapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.yashk9.githubapp.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {
    // PagingData from the Room
    @ExperimentalPagingApi
    val offlineCache = githubRepository.getOfflineCache()
}