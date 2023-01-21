package com.adityamshidlyali.musicwiki.utility

import kotlinx.coroutines.CoroutineDispatcher

// Dispatcher provider interface for testing the mocked coroutines
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}