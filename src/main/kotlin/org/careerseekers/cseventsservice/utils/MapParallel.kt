package org.careerseekers.cseventsservice.utils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

suspend fun <T, R> Iterable<T>.mapParallel(
    maxConcurrency: Int = 64,
    transform: suspend (T) -> R
): List<R> = coroutineScope {
    val deferred = mutableListOf<Deferred<R>>()
    val semaphore = Semaphore(maxConcurrency)

    map { item ->
        semaphore.withPermit {
            async { transform(item) }
        }
    }.forEach { deferred.add(it) }

    deferred.awaitAll()
}
