package com.yashk9.githubapp.util

import kotlin.Exception

//ResultWrapper for Network Functions
sealed class Result<out V, out E>{
    data class Success<out V>(val data: V): Result<V, Nothing>()
    data class Error<out E>(val exception: E): Result<Nothing, E>()

    companion object Factory{
        inline fun<V> build(function: () -> V): Result<V, Exception> =
            try{
                Success(function.invoke())
            }catch (e: Exception){
                Error(e)
            }
    }
}