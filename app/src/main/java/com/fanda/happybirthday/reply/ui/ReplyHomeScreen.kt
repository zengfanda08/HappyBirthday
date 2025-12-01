package com.fanda.happybirthday.reply.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fanda.happybirthday.R
import com.fanda.happybirthday.reply.data.Email
import com.fanda.happybirthday.reply.data.MailboxType
import com.fanda.happybirthday.reply.data.local.LocalAccountsDataProvider
import com.fanda.happybirthday.reply.ui.utils.ReplyContentType
import com.fanda.happybirthday.reply.ui.utils.ReplyNavigationType

@Composable fun ReplyHomeScreen(
    contentType: ReplyContentType,
    navigationType: ReplyNavigationType,
    replyUiState: ReplyUiState,
    onTabPressed: (MailboxType) -> Unit,
    onEmailCardPressed: (Email) -> Unit,
    onDetailScreenBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            mailboxType = MailboxType.Inbox, icon = Icons.Filled.Inbox, text = stringResource(id = R.string.tab_inbox)
        ), NavigationItemContent(
            mailboxType = MailboxType.Sent, icon = Icons.AutoMirrored.Filled.Send, text = stringResource(id = R.string.tab_sent)
        ), NavigationItemContent(
            mailboxType = MailboxType.Drafts, icon = Icons.Filled.Drafts, text = stringResource(id = R.string.tab_drafts)
        ), NavigationItemContent(
            mailboxType = MailboxType.Spam, icon = Icons.Filled.Report, text = stringResource(id = R.string.tab_spam)
        )
    )
    // 当用户在较大屏幕上，显示抽屉式导航栏
    if (navigationType == ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        val navigationDrawerContentDescription = stringResource(R.string.navigation_drawer)
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(Modifier.width(dimensionResource(R.dimen.drawer_width))) {
                NavigationDrawerContent(
                    selectedDestination = replyUiState.currentMailbox,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(dimensionResource(R.dimen.drawer_padding_content))
                )
            }
        }, modifier = Modifier.testTag(navigationDrawerContentDescription)) {
            ReplyAppContent(
                contentType = contentType,
                navigationType = navigationType,
                replyUiState = replyUiState,
                onTabPressed = onTabPressed,
                onEmailCardPressed = onEmailCardPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        }

    } else {
        // 如果在主页显示
        if (replyUiState.isShowingHomepage) {
            ReplyAppContent(
                contentType = contentType,
                navigationType = navigationType,
                replyUiState = replyUiState,
                onTabPressed = onTabPressed,
                onEmailCardPressed = onEmailCardPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = modifier
            )
        } else {
            // 在详情页面显示
            ReplyDetailsScreen(
                isFullScreen = true, replyUiState = replyUiState, onBackPressed = onDetailScreenBackPressed, modifier = modifier
            )
        }
    }

}

@Composable private fun ReplyAppContent(
    contentType: ReplyContentType,
    navigationType: ReplyNavigationType,
    replyUiState: ReplyUiState,
    onTabPressed: ((MailboxType) -> Unit),
    onEmailCardPressed: (Email) -> Unit,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {

        val navigationRailContentDescription = stringResource(R.string.navigation_rail)

        // 通过动画方式显示侧边导航栏
        AnimatedVisibility(visible = navigationType == ReplyNavigationType.NAVIGATION_RAIL) {
            ReplyNavigationRail(
                currentTab = replyUiState.currentMailbox,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                modifier = Modifier.testTag(navigationRailContentDescription)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {

            if (contentType == ReplyContentType.LIST_AND_DETAIL) {
                // 显示列表和详情
                ReplyListAndDetailContent(replyUiState = replyUiState, onEmailCardPressed = onEmailCardPressed, modifier = Modifier.weight(1f))
            } else {
                // 显示列表
                ReplyListOnlyContent(
                    replyUiState = replyUiState, onEmailCardPressed = onEmailCardPressed, modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = dimensionResource(R.dimen.email_list_only_horizontal_padding)
                        )
                )
            }

            AnimatedVisibility(visible = navigationType == ReplyNavigationType.BOTTOM_NAVIGATION) {
                ReplyBottomNavigationBar(
                    currentTab = replyUiState.currentMailbox, onTabPressed = onTabPressed, navigationItemContentList = navigationItemContentList, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable private fun ReplyNavigationRail(
    currentTab: MailboxType, onTabPressed: ((MailboxType) -> Unit), navigationItemContentList: List<NavigationItemContent>, modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(selected = currentTab == navItem.mailboxType, onClick = { onTabPressed(navItem.mailboxType) }, icon = {
                Icon(
                    imageVector = navItem.icon, contentDescription = navItem.text
                )
            })
        }
    }
}

@Composable private fun ReplyBottomNavigationBar(
    currentTab: MailboxType, onTabPressed: ((MailboxType) -> Unit), navigationItemContentList: List<NavigationItemContent>, modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(selected = currentTab == navItem.mailboxType, onClick = { onTabPressed(navItem.mailboxType) }, icon = {
                Icon(
                    imageVector = navItem.icon, contentDescription = navItem.text
                )
            })
        }
    }
}

@Composable private fun NavigationDrawerContent(
    selectedDestination: MailboxType, onTabPressed: ((MailboxType) -> Unit), navigationItemContentList: List<NavigationItemContent>, modifier: Modifier = Modifier
) {
    Column(modifier = modifier.wrapContentWidth(Alignment.Start)) {
        NavigationDrawerHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.profile_image_padding)),
        )
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(selected = selectedDestination == navItem.mailboxType, label = {
                Text(
                    text = navItem.text, modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.drawer_padding_header))
                )
            }, icon = {
                Icon(
                    imageVector = navItem.icon, contentDescription = navItem.text
                )
            }, colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
            ), onClick = { onTabPressed(navItem.mailboxType) })
        }
    }
}

@Composable private fun NavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        ReplyLogo(modifier = Modifier.size(dimensionResource(R.dimen.reply_logo_size)))
        ReplyProfileImage(
            drawableResource = LocalAccountsDataProvider.defaultAccount.avatar,
            description = stringResource(id = R.string.profile),
            modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
        )
    }
}

private data class NavigationItemContent(
    val mailboxType: MailboxType, val icon: ImageVector, val text: String
)