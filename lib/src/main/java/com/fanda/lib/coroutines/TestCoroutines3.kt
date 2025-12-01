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
            val forecast = async {
                // 并发运行，在新协程中运行
                getForecast()
            }
            val temperature = async {
                // 并发运行，在新协程中运行
                getTemperature()
            }
            println("${forecast.await()}, ${temperature.await()}")
            // 现在不会先执行的这里的代码，需要等协程执行完毕，再执行这里，然后结束
            println("Have a good day!")
        }
    }
    println("Execution time: ${time / 1000.0} seconds")
}

// 有返回值
suspend fun getForecast(): String {
    delay(1000)
    return "Sunny"
}

// 有返回值
suspend fun getTemperature(): String {
    delay(1000)
    return "30\u00b0C"
}
