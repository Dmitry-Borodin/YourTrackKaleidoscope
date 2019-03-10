package com.pet.kaleidoscope.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * @author Dmitry Borodin on 3/10/19.
 */
abstract class ScopedPresenter : CoroutineScope {

    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    open fun onAttach() {
        job = Job()
    }

    open fun onDetouch() {
        job.cancel()
    }
}