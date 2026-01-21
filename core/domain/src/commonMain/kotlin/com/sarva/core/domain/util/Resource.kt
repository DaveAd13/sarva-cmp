package com.sarva.core.domain.util

sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Failure(val throwable: Throwable) : Resource<Nothing>
    data object Loading : Resource<Nothing>
}