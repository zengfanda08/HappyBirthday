package com.fanda.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // 组件自身也要添加 fillMaxSize 才会填充整个屏幕
                    // modifier 传递过去组件中，尽量让组件解耦复用
                    GreetingImage(
                        message = stringResource(R.string.happy_birthday_text), from = stringResource(id = R.string.signature_text),
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Column(verticalArrangement = Arrangement.Center, modifier = modifier) {
        Text(text = message, fontSize = 65.sp, lineHeight = 80.sp, textAlign = TextAlign.Center)
        Text(
            text = from, fontSize = 36.sp, modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
    }
}

/*
*
* Android 为用户提供了许多工具。例如，TalkBack 是 Android 设备随附的 Google 屏幕阅读器。TalkBack 可为用户提供语音反馈，这样用户无需查看屏幕即可使用设备。
*
* 不过，在此应用中添加图片只是为了进行装饰。就本例而言，在图片中添加内容说明会使应用更难以通过 TalkBack 进行使用。您可以不设置面向用户的内容说明，而将图片的 contentDescription 实参设为 null，以便 TalkBack 跳过 Image 可组合函数。
* */

// 每个可组合函数都应接受一个可选的 Modifier 形参。修饰符用于控制界面元素在其父布局中的放置、显示或行为方式
@Composable fun GreetingImage(message: String, from: String, modifier: Modifier = Modifier) {
    val image = painterResource(id = R.drawable.androidparty)
    Box(modifier) {
        // 图片默认充满父布局
        Image(painter = image, contentDescription = null, contentScale = ContentScale.Crop, alpha = 0.5f)
        GreetingText(message = message, from = from,modifier = Modifier
            .fillMaxSize()
            .padding(8.dp))
    }
}

@Preview(showBackground = true, showSystemUi = false) @Composable fun BirthdayCardPreview() {
    HappyBirthdayTheme {
//        GreetingText(
//            message = "Happy Birthday Liu hang!", from = "From Fanda",
//            Modifier
//                .padding(8.dp)
//                .fillMaxSize()
//        )
        GreetingImage(message = "Happy Birthday Liu hang!", from = "From Fanda")
    }
}