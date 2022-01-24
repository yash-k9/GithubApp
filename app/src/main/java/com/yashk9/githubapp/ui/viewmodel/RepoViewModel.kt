package com.yashk9.githubapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.yashk9.githubapp.repository.GithubRepository
import com.yashk9.githubapp.util.Result
import com.yashk9.githubapp.util.viewState.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    //Query State
    private val _uiState = MutableSharedFlow<ViewState>()
    val uiState = _uiState.asSharedFlow()

    // PagingData from the Room
    @ExperimentalPagingApi
    val offlineCache = githubRepository.getOfflineCache()

    //Query the Database for repositories
    @ExperimentalCoroutinesApi
    suspend fun queryRepo(query: String){
        _uiState.emit(ViewState.Loading)
        val response = githubRepository.getQueryFromDatabase(query)
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