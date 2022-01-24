package com.dicoding.moviecatalog.utils

data class ApiResponse<out T>(val status: Status, val data: T?, val message: Event<String>? = null) {
    companion object{
        fun <T> success(data: T, message: String?): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, if(message != null) Event(message) else null)
        }
        fun <T> loading(): ApiResponse<T>{
            return ApiResponse(Status.LOADING, null, null)
        }
        fun <T> error(message: String): ApiResponse<T> {
            return ApiResponse(Status.ERROR, null,  Event(message))
        }
        fun <T> noData(): ApiResponse<T> {
            return ApiResponse(Status.NO_DATA, null, null)
        }
    }
}