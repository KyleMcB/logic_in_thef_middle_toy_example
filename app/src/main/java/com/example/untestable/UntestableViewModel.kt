package com.example.untestable

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

abstract class UntestableViewModelLogic(
    scopeProvider: ViewModel.() -> CoroutineScope = { viewModelScope },
    val println: (Any?) -> Unit = ::println
) : ViewModel() {
    companion object {
        val delayInterval: Long = 2000
        val importantConstant = "!"
    }

    private val _primes = MutableStateFlow(listOf(2, 3, 5))
    val primes = _primes.asStateFlow()
    private val scope = scopeProvider()

    init {
        scope.launch {
            while (isActive) {
                delay(delayInterval)
                println("Hello$importantConstant")
            }
        }
        scope.launch {
            var numberToCheck = primes.value.last() + 1
            while (isActive) {
                if (isPrime(numberToCheck)) {
                    _primes.update { it + numberToCheck }
                    delay(1000) //Good job cpu! You found a prime. Take a break.
                }
                numberToCheck++
            }
        }
    }

    protected fun showDialogLogic(getString: (Int) -> String) {
        val today = LocalDate.now().dayOfWeek!!
        val todayString = when (today) {
            DayOfWeek.MONDAY -> getString(R.string.monday)
            DayOfWeek.TUESDAY -> getString(R.string.tuesday)
            DayOfWeek.WEDNESDAY -> getString(R.string.wednesday)
            DayOfWeek.THURSDAY -> getString(R.string.thursday)
            DayOfWeek.FRIDAY -> getString(R.string.friday)
            DayOfWeek.SATURDAY -> getString(R.string.saturday)
            DayOfWeek.SUNDAY -> getString(R.string.sunday)
        }
    }
}


class UntestableViewModel : UntestableViewModelLogic() {
    fun showDialog(context: Context) {
        showDialogLogic({ context.getString(it) })

        val builder = AlertDialog.Builder(context)
        builder.setTitle(todayString)
        builder.show()
    }
}