package com.kapanen.cariadtesttask.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kapanen.cariadtesttask.model.Poi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val TIMER_DELAY = 30L //30 seconds

class DefaultPoiRepository(
    private val remoteDataSource: PoiRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : PoiRepository, CoroutineScope {

    private var isLiveUpdateEnabled: Boolean = false

    override val coroutineContext: CoroutineContext
        get() = ioDispatcher

    private val observablePois = MutableLiveData<List<Poi>>()
    private val observableError = MutableLiveData<Throwable>()

    override fun observePois(): LiveData<List<Poi>> = observablePois
    override fun observeError(): LiveData<Throwable> = observableError

    override fun enableLiveUpdate() {
        if (!isLiveUpdateEnabled) {
            isLiveUpdateEnabled = true
            tickerFlow(TIMER_DELAY.seconds)
                .map { LocalDateTime.now() }
                .distinctUntilChanged { old, new ->
                    old.minute == new.minute
                }
                .onEach {
                    loadData()
                }
                .launchIn(this)
        }
    }

    override fun disableLiveUpdate() {
        isLiveUpdateEnabled = false
    }

    private fun loadData() {
        launch(ioDispatcher) {
            val result = remoteDataSource.poiList()
            if (result.isSuccess) {
                observablePois.postValue(result.getOrDefault(emptyList()))
            } else {
                observableError.postValue(
                    result.exceptionOrNull() ?: IllegalStateException("Error")
                )
            }
        }
    }

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (isLiveUpdateEnabled) {
            emit(Unit)
            delay(period)
        }
    }

}
