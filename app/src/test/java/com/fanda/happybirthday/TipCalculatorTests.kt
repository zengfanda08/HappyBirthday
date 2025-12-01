package com.fanda.happybirthday

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

// 点击类旁边的按钮，运行所有测试方法
class TipCalculatorTests {

    // 点击方法边的按钮，运行改测试方法
    @Test fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        val actualTip = calculateTip(amount = amount, tipPercentage = tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}