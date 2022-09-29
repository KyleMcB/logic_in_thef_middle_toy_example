package com.example.untestable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class UntestableViewModelLogicTest {
    val testCoroutineScope = CoroutineScope(newFixedThreadPoolContext(2, "test thread pool"))

    class TestViewModelLogicDI(scope: CoroutineScope, printfun: (Any?) -> Unit) : UntestableViewModelLogic({ scope }, printfun)

    @Test
    fun basic() {
        val subject = TestViewModelLogicDI(testCoroutineScope, ::println)
    }

    @Test
    fun wePrintToConsole() = runBlocking {
        val delayLength = (UntestableViewModelLogic.delayInterval * 1.5).toLong()
        var asserterWasCalled = false
        val printAsserter: (Any?) -> Unit = {
            assert(it != null)
            asserterWasCalled = true
        }
        val subject = TestViewModelLogicDI(testCoroutineScope, printAsserter)
        delay(delayLength)
        assert(asserterWasCalled)
    }
}