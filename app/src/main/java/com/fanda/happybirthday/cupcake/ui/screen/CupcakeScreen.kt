package com.fanda.happybirthday.cupcake.ui.screen

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fanda.happybirthday.R
import com.fanda.happybirthday.cupcake.data.DataSource
import com.fanda.happybirthday.cupcake.ui.OrderViewModel
import com.fanda.happybirthday.ui.theme.HappyBirthdayTheme

// 路径
enum class CupcakeScreen(@param:StringRes val title: Int) {
    Start(title = R.string.app_name), Flavor(title = R.string.choose_flavor), Pickup(title = R.string.choose_pickup_date), Summary(title = R.string.order_summary)
}


/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class) @Composable fun CupcakeAppBar(
    currentScreen: CupcakeScreen, canNavigateBack: Boolean, navigateUp: () -> Unit, modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(stringResource(id = currentScreen.title)) }, colors = TopAppBarDefaults.mediumTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), modifier = modifier, navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    })
}

@Composable fun CupcakeApp(
    viewModel: OrderViewModel = viewModel(), navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    // 堆栈中的路由是可观察的，根据当前中的目标路由，显示不同的标题
    val cupcakeScreen = CupcakeScreen.valueOf(backStackEntry?.destination?.route ?: CupcakeScreen.Start.name)
    Scaffold(topBar = {
        // 只要返回堆栈中的当前屏幕后面还有屏幕，系统就会显示向上按钮
        CupcakeAppBar(currentScreen = cupcakeScreen, canNavigateBack = navController.previousBackStackEntry != null, navigateUp = { navController.navigateUp() })
    }) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(navController = navController, startDestination = CupcakeScreen.Start.name, modifier = Modifier.padding(innerPadding)) {
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(
                    quantityOptions = DataSource.quantityOptions, onNextButtonClicked = {
                        // 先处理数据，再处理导航
                        viewModel.setQuantity(it)
                        navController.navigate(CupcakeScreen.Flavor.name)
                    }, modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current
                SelectOptionScreen(subtotal = uiState.price,
                    options = DataSource.flavors.map { id -> context.getString(id) },
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    onCancelButtonClicked = {
                        cancelOrderAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Summary.name) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = CupcakeScreen.Summary.name) {
                val context = LocalContext.current
                OrderSummaryScreen(onSendButtonClicked = { subject, summary -> shareOrder(context, subject, summary) },
                    onCancelButtonClicked = { cancelOrderAndNavigateToStart(viewModel, navController) },
                    orderUiState = uiState,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}

private fun shareOrder(context: Context, subject: String, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.new_cupcake_order)))
}

// 跳转回到首页面
private fun cancelOrderAndNavigateToStart(viewModel: OrderViewModel, navController: NavHostController) {
    viewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, inclusive = false)

}

@Preview(showBackground = true, showSystemUi = true) @Composable fun CupcakeAppPreview() {
    HappyBirthdayTheme {
        CupcakeApp()
    }
}
