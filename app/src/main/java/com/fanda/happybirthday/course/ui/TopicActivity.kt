package com.fanda.happybirthday.topic.ui

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fanda.happybirthday.R
import com.fanda.happybirthday.topic.model.Topic
import com.fanda.happybirthday.topic.model.TopicDataSource
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

class TopicActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TopicApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable fun TopicApp(modifier: Modifier = Modifier) {
    TopicList(modifier)
}

@Composable fun TopicList(modifier: Modifier = Modifier) {
    val topics = TopicDataSource.topics
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(8.dp)
    ) {
        items(topics) { topic ->
            TopicCard(topic)
        }
    }
}

@Composable fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = topic.image), contentDescription = stringResource(id = topic.title), modifier = Modifier
                    .size(68.dp)
                    .aspectRatio(1f), contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = stringResource(id = topic.title),
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_grain), contentDescription = null, modifier = Modifier.padding(start = 16.dp))
                    Text(text = topic.count.toString(), modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true) @Composable fun TipTimeAppPreview() {
    HappyBirthdayTheme {
        TopicApp()
    }
}