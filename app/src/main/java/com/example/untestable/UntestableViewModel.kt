package com.example.untestable

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class UntestableViewModel : ViewModel() {
    private val _primes = MutableStateFlow(listOf(2, 3, 5))
    val primes = _primes.asStateFlow()
    val importantConstant = "!"

    init {
        viewModelScope.launch {
            while (isActive) {
                delay(2000)
                println("Hello$importantConstant")
            }
        }
        viewModelScope.launch {
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

    fun showDialog(context: Context) {
        val today = LocalDate.now().dayOfWeek!!
        val todayString = when (today) {
            DayOfWeek.MONDAY -> context.getString(R.string.monday)
            DayOfWeek.TUESDAY -> context.getString(R.string.tuesday)
            DayOfWeek.WEDNESDAY -> context.getString(R.string.wednesday)
            DayOfWeek.THURSDAY -> context.getString(R.string.thursday)
            DayOfWeek.FRIDAY -> context.getString(R.string.friday)
            DayOfWeek.SATURDAY -> context.getString(R.string.saturday)
            DayOfWeek.SUNDAY -> context.getString(R.string.sunday)
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(todayString)
        builder.show()
    }
}