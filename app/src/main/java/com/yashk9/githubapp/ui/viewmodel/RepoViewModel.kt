package com.yashk9.githubapp.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.yashk9.githubapp.data.remote.GithubApiService
import com.yashk9.githubapp.model.Repo
import com.yashk9.githubapp.repository.GithubRepository
import com.yashk9.githubapp.util.Result
import com.yashk9.githubapp.util.viewState.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    private val _uiState = MutableSharedFlow<ViewState>()
    val uiState = _uiState.asSharedFlow()

    @ExperimentalPagingApi
    val offlineCache = githubRepository.getOfflineCache()

    @ExperimentalCoroutinesApi
    suspend fun queryRepo(query: String){
        _uiState.emit(ViewState.Loading)
        val response = githubRepository.getQueryResponse(query)
        Log.d("MAIN", response.toString())
        when(response){
            is Result.Error -> _uiState.emit(ViewState.Error(response.exception))
            is Result.Success -> {
                if(response.data.isNotEmpty()){
                    _uiState.emit(ViewState.Success(response.data))
                }else{
                    _uiState.emit(ViewState.Empty)
                }
            }}
    }
}