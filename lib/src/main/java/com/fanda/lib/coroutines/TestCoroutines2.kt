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
            // 当前协程会立即返回，即可任务还没完成
            launch {
                // 并发运行，在新协程中运行
                printForecast2()
            }
            // 当前协程会立即返回，即可任务还没完成
            launch {
                // 并发运行，在新协程中运行
                printTemperature2()
            }
            // 先执行的这里的代码，然后等协程执行完毕，再结束
            println("Have a good day!")
        }
    }
    println("Execution time: ${time / 1000.0} seconds")
}

suspend fun printForecast2() {
    delay(1000)
    println("Sunny")
}

suspend fun printTemperature2() {
    delay(1000)
    println("30\u00b0C")
}
