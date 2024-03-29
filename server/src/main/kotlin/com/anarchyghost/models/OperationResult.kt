package com.anarchyghost.models

sealed interface OperationResult<T> {
    data object NotFound : OperationResult<Nothing>
    data object InternalError : OperationResult<Nothing>
    data class Success<T>(val data: T) : OperationResult<T>

}