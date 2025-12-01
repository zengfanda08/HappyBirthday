package com.fanda.happybirthday.reply.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fanda.happybirthday.reply.data.Email
import com.fanda.happybirthday.reply.data.MailboxType
import com.fanda.happybirthday.reply.ui.utils.ReplyContentType
import com.fanda.happybirthday.reply.ui.utils.ReplyNavigationType

@Composable fun ReplyApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
) {
    val viewModel: ReplyViewModel = viewModel()
    val replyUiState = viewModel.uiState.collectAsState().value
    val navigationType: ReplyNavigationType
    val contentType: ReplyContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
            contentType = ReplyContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = ReplyNavigationType.NAVIGATION_RAIL
            contentType = ReplyContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = ReplyContentType.LIST_AND_DETAIL
        }

        else -> {
            navigationType = ReplyNavigationType.BOTTOM_NAVIGATION
            contentType = ReplyContentType.LIST_ONLY
        }
    }

    ReplyHomeScreen(contentType = contentType, navigationType = navigationType, replyUiState = replyUiState, onTabPressed = { mailboxType: MailboxType ->
        viewModel.updateCurrentMailbox(mailboxType = mailboxType)
        viewModel.resetHomeScreenStates()
    }, onEmailCardPressed = { email: Email ->
        viewModel.updateDetailsScreenStates(
            email = email
        )
    }, onDetailScreenBackPressed = {
        viewModel.resetHomeScreenStates()
    }, modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true,device = Devices.PIXEL_TABLET) @Composable fun ReplyAppPreview() {
    ReplyApp(WindowWidthSizeClass.Medium)
}