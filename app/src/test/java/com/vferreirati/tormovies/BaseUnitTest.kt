package com.vferreirati.tormovies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@UseExperimental(ObsoleteCoroutinesApi::class)
@ExperimentalCoroutinesApi
open class BaseUnitTest {
    @get:Rule
    val executor = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @Before
    open fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    open fun dispose() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}