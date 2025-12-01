package com.fanda.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class TaskManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskManagerApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable fun TaskManagerApp(modifier: Modifier = Modifier) {
    TaskCompleted(painterResource(id = R.drawable.ic_task_completed), stringResource(id = R.string.task_title), stringResource(id = R.string.task_description), modifier)
}

@Composable fun TaskCompleted(painter: Painter, title: String, des: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painter, contentDescription = null)
        Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp, bottom = 8.dp))
        Text(text = des, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true) @Composable fun TaskManagerPreview() {
    HappyBirthdayTheme {
        TaskManagerApp()
    }
}