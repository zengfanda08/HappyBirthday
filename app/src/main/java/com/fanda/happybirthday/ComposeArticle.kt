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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class ComposeArticle : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComposeArticleApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable fun ComposeArticleApp(modifier: Modifier = Modifier){
    ComposeText(title = stringResource(id = R.string.title), des = stringResource(id = R.string.des), content = stringResource(id = R.string.content), painter = painterResource(id = R.drawable.bg_compose_background))
}

@Composable fun ComposeText(title:String ,des:String ,content:String ,painter: Painter,modifier: Modifier= Modifier){
    val bg = painterResource(id = R.drawable.bg_compose_background)
    Column(modifier = modifier.fillMaxSize()) {
        Image(painter = bg, contentDescription = null, contentScale = ContentScale.FillWidth)
        Text(text = title, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Text(
            text = des,
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Justify
        )
        Text(
            text = content,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Justify
        )
    }

}


@Preview(showBackground = true, showSystemUi = false) @Composable fun ComposeArticlePreview() {
    HappyBirthdayTheme {
        ComposeArticleApp()
    }
}