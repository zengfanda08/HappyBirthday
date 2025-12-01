package com.fanda.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme
import java.text.NumberFormat
import kotlin.math.ceil

class TipTimeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                // 这里的 innerPadding 可以处理顶部状态栏和底部导航栏的间距问题
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipTimeApp(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable fun TipTimeApp(modifier: Modifier = Modifier) {
    TipTimeLayout(modifier.fillMaxSize())
}

@Composable fun TipTimeLayout(modifier: Modifier = Modifier) {
    var tipInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)

    // .verticalScroll(rememberScrollState()) 属性用于内容超过屏幕时，可滚动显示
    Column(
        modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // align 的优先级高于 Column 的 horizontalAlignment
        Text(
            text = stringResource(id = R.string.calculate_tip), modifier = Modifier
                .padding(top = 40.dp, bottom = 16.dp)
                .align(Alignment.Start)
        )
        EditNumberField(
            R.string.bill_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            value = tipInput,
            onValueChange = { tipInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = { roundUp = it }, modifier = modifier.padding(bottom = 32.dp))
        Text(
            text = stringResource(id = R.string.tip_amount, tip), style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable fun RoundTheTipRow(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.round_up_tip))
        Switch(checked = roundUp, onCheckedChange = onRoundUpChanged)
    }
}


// 下面这种方式会触发重组，但是 amountInput 值在重组时没被记住，一直被重置为初始值，是有问题的
//@Composable fun EditNumberField(modifier: Modifier = Modifier) {
//    var amountInput :MutableState<String> = mutableStateOf("0")
//    TextField(value = amountInput.value, onValueChange = {amountInput.value = it},modifier = modifier)
//}


// 注意：@StringRes 注解是一种类型安全的字符串资源使用方法，用于表明要传递的整数是 values/strings.xml 文件中的字符串资源。
// 需要调用 remember 函数来记住变量，将内容保存在内存中，从而避免被重新创建
@Composable fun EditNumberField(@StringRes label: Int, @DrawableRes leadingIcon: Int, keyboardOptions: KeyboardOptions, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = value, leadingIcon = { Icon(painterResource(leadingIcon), contentDescription = null) }, onValueChange = onValueChange, label = { Text(text = stringResource(label)) }, singleLine = true, keyboardOptions = keyboardOptions, modifier = modifier
    )
}


@VisibleForTesting
internal fun calculateTip(
    amount: Double, tipPercentage: Double = 15.0, roundUp: Boolean
): String {
    var tip = tipPercentage * amount / 100
    // 根据设备的区域设置（locale）来决定货币符号和格式。
    // 在中文环境下，如果 tip 是 15.0，那么该方法可能会返回 "¥15.00"；而在其他地区可能是 "$15.00" 或其他形式
    if (roundUp) {
        tip = ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}


@Preview(showBackground = true, showSystemUi = true) @Composable fun TipTimeAppPreview() {
    HappyBirthdayTheme {
        TipTimeApp()
    }
}