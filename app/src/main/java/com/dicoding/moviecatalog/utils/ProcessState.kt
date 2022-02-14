package com.dicoding.moviecatalog.utils

data class ProcessState(val status: Status, val message: Event<String>? = null) {
    companion object{
        fun success(): ProcessState{
            return ProcessState(Status.SUCCESS,null)
        }
        fun loading(): ProcessState{
            return ProcessState(Status.LOADING, null)
        }
        fun error(message: String): ProcessState{
            return ProcessState(Status.ERROR, Event(message))
        }
        fun noData(): ProcessState{
            return ProcessState(Status.NO_DATA, null)
        }
    }
}