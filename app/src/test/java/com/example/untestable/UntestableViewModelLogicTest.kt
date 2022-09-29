package com.example.untestable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.newFixedThreadPoolContext
import org.junit.Test

internal class UntestableViewModelLogicTest {
    val testCoroutineScope = CoroutineScope(newFixedThreadPoolContext(2, "test thread pool"))

    class TestViewModelLogicDI(scope: CoroutineScope) : UntestableViewModelLogic({ scope })

    @Test
    fun basic() {
        val subject = TestViewModelLogicDI(testCoroutineScope)
    }
}