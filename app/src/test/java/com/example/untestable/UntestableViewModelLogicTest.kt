package com.example.untestable

import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Test
import java.time.DayOfWeek

internal class UntestableViewModelLogicTest {
    val testCoroutineScope = CoroutineScope(newFixedThreadPoolContext(2, "test thread pool"))

    class TestViewModelLogicDI(scope: CoroutineScope, printfun: (Any?) -> Unit, var today: DayOfWeek = DayOfWeek.MONDAY) : UntestableViewModelLogic({ scope }, printfun) {
        fun showDialog(getString: (Int) -> String, dialogShower: MyDialogShower) = super.showDialogLogic(getString, dialogShower)
        override fun today(): DayOfWeek = today
    }

    @Test
    fun basic() {
        val subject = UntestableViewModelLogicTest.TestViewModelLogicDI(testCoroutineScope, ::println)
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

    @Test
    fun weShowCurrentDayInDialog() {
        val testStringValue = "aString"
        DayOfWeek.values().forEach { day ->
            val subject = TestViewModelLogicDI(testCoroutineScope, ::println, day)
            val getString: (Int) -> String = {
                val daysList = listOf(R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday)
                assert(daysList.contains(it))
                testStringValue
            }
            var titleSet = false
            var showCalled = false
            val testDialog = object : MyDialogShower {
                override fun setTitle(title: String) {
                    assert(title == testStringValue)
                    titleSet = true
                }

                override fun show() {
                    assert(titleSet)
                    showCalled = true
                }
            }
            subject.showDialog(getString, testDialog)
            assert(showCalled)
        }
    }
}