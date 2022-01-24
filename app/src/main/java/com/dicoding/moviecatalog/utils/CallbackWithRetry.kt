package com.dicoding.moviecatalog.utils

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CallbackWithRetry<T>: Callback<T> {
    private val retryCount = 3
    private var retry = 0
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(response.isSuccessful){
            onResponseSuccess(response.body())
        }else onResponseFailed(response.code(), response.message())
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if(retry <= retryCount){
            call.clone().enqueue(this)
            retry++
        }else{
            t.message?.let { onConnectionError(it) }
        }
    }

    abstract fun onResponseSuccess(data: T?)

    abstract fun onResponseFailed(errorCode: Int, errorMessage: String)

    abstract fun onConnectionError(message: String)
}