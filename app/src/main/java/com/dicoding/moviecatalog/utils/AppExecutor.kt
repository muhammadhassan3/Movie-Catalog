package com.dicoding.moviecatalog.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutor @VisibleForTesting constructor(
    private val diskIo: Executor,
    private val networkIO: Executor,
    private val mainThread: Executor
) {
    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(THREAD_COUNT),
        MainThreadExecutor()
    )

    fun diskIO(): Executor = diskIo

    fun networkIO(): Executor = networkIO

    fun mainThread(): Executor = mainThread

    companion object{
        private const val THREAD_COUNT = 3
    }

    private class MainThreadExecutor: Executor{
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }

    }
}