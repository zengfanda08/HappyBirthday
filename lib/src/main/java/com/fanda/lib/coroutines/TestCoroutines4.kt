package com.fanda.lib.coroutines

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/*
*
* 协程中的代码在默认情况下会依序调用。如果您想让代码并发运行，就必须明确指定并发运行.
* runBlocking 函数会等待所有代码块执行完毕，然后继续执行。
* */
fun main() {
    val time = measureTimeMillis {
        runBlocking {
            println("Weather forecast")
            println(getWeatherReport())
            // 现在不会先执行的这里的代码，需要等协程执行完毕，再执行这里，然后结束
            println("Have a good day!")
        }
    }
    println("Execution time: ${time / 1000.0} seconds")
}

// coroutineScope 创建局部协程作用域,在此作用域内启动的协程会归入此作用域内
suspend fun getWeatherReport() = coroutineScope {
    // coroutineScope() 仅在其所有工作（包括其启动的所有协程）完成后才会返回
    getForecast()
    getTemperature()
//    val forecast = async { getForecast4() }
//    val temperature = async { getTemperature4() }
//    "${forecast.await()} ${temperature.await()}"
}

// 有返回值
suspend fun getForecast4(): String {
    delay(1000)
    return "Sunny"
}

// 有返回值
suspend fun getTemperature4(): String {
    delay(1000)
    return "30\u00b0C"
}
