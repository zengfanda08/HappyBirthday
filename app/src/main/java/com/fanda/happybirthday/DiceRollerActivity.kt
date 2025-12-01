package com.fanda.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class DiceRollerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceRollerApp(
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * 创建一个可组合函数，用于显示骰子按钮和结果
 *
 * wrapContentSize() 方法会指定可用空间应至少与其内部组件一样大。但是，由于使用了 fillMaxSize() 方法，因此如果布局内的组件小于可用空间，则可以将 Alignment 对象传递到 wrapContentSize() 方法，以指定组件应如何在可用空间内对齐。
 */
@Composable fun DiceRollerApp(modifier: Modifier = Modifier) {
    DiceWidthButtonImage(
        modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

/*
* 既然有默认值，为什么还要传递 Modifier 实参。原因在于可组合函数可能会进行重组，这实质上意味着 @Composable 方法中的代码块会再次执行。如果在代码块中创建了 Modifier 对象，系统可能会重新创建该对象，并且这种方式效率不高。
* 通常，dp 尺寸以 4.dp 为增量进行更改
*  */
// 带默认的修饰符
@Composable fun DiceWidthButtonImage(modifier: Modifier = Modifier) {

    // 默认情况下，可组合函数是无状态的，这意味着它们不存储值，并且可随时被系统重组，从而导致值被重置。不过，Compose 提供了一种避免这种情况的便捷方式。可组合函数可以使用 remember 可组合函数将对象存储在内存中。
    // mutableIntStateOf 返回可观察对象，当值被更改时，Compose 会重新运行该函数进行重组。
    var result by remember { mutableIntStateOf(1) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(imageResource), contentDescription = result.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // 随机生成1到6的数值
            result = (1..6).random()
        }) {
            Text(text = stringResource(id = R.string.roll))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true) @Composable fun DiceRollerAppPreview() {
    HappyBirthdayTheme {
        DiceRollerApp()
    }
}