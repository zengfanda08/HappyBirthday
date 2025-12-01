package com.fanda.happybirthday

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat

class TipUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 编译器知道 androidTest 目录中带有 @Test 注解的方法引用的是插桩测试，而 test 目录中带有 @Test 注解的方法引用的是本地测试
    @Test
    fun calculate_20_percent_tip() {
        // 插桩测试会测试应用的实际实例及其界面，因此必须设置界面内容
        composeTestRule.setContent {
            HappyBirthdayTheme {
                TipTimeApp()
            }
        }
        // 现在界面内容已设置完毕，接下来就可以编写指令来与应用界面组件交互
        // 找到包含特定文本的节点，并进行内容填充
        // 必须全匹配，大小写敏感
        composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        // 直接对界面组件调用断言，是否跟给定的文本相同，相同则通过
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
            "No node with this text was found.")
    }


}