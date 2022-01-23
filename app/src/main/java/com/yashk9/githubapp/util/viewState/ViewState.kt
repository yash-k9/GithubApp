package com.yashk9.githubapp.util.viewState

import com.yashk9.githubapp.model.Repo

sealed class ViewState {
    object Loading : ViewState()
    object Empty : ViewState()
    data class Success(val data: List<Repo>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}