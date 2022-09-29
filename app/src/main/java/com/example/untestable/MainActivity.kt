package com.example.untestable

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.untestable.ui.theme.UntestableTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update

abstract class MainActivityLogic : ComponentActivity()
class MainActivity :MainActivityLogic() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UntestableTheme {
                val model: UntestableViewModel by viewModels()
                val list by model.primes.collectAsState()
                val listSate = rememberLazyListState()
                LaunchedEffect(true) {
                    model.primes.collect() {
                        listSate.scrollToItem(it.lastIndex)
                    }
                }
                Surface(modifier = Modifier.fillMaxSize().clickable { model.showDialog(this@MainActivity) }, color = MaterialTheme.colors.background) {
                    LazyColumn(state = listSate) {
                        items(list) { item ->
                            Text(item.toString())
                        }
                    }
                }
            }
        }

    }
}

fun isPrime(number: Int): Boolean {
    if ((number == 1) || (number == 0) || (number == -1)) return false
    var index = 2
    fun square(num:Int) = num * num
    while (square(index) <= number) {
        if (number % index == 0) return false
        index++
    }
    return true
}

class UntestableViewModel : ViewModel() {
    private val _primes = MutableStateFlow(listOf(2,3,5))
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
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Hello!")
        builder.show()
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UntestableTheme {
        Greeting("Android")
    }
}