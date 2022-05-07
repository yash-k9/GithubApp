package com.yashk9.githubapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashk9.githubapp.repository.GithubRepository
import com.yashk9.githubapp.util.Result
import com.yashk9.githubapp.util.viewState.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {
    //Query State
    private val _uiState = MutableStateFlow<ViewState>(ViewState.Empty)
    val uiState = _uiState.asStateFlow()

    private var query: String = ""

    //Query the Database for repositories
    fun queryRepo(query: String){
        viewModelScope.launch {
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

    fun setQuery(query: String){
        this.query = query
    }

    fun getQuery(): String{
        return query
    }

}