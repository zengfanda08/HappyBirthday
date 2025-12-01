package com.fanda.happybirthday.reply.ui

import com.fanda.happybirthday.reply.data.Email
import com.fanda.happybirthday.reply.data.MailboxType
import com.fanda.happybirthday.reply.data.local.LocalEmailsDataProvider

data class ReplyUiState(
    val mailboxes: Map<MailboxType, List<Email>> = emptyMap(),
    val currentMailbox: MailboxType = MailboxType.Inbox,
    val currentSelectedEmail: Email = LocalEmailsDataProvider.defaultEmail,
    val isShowingHomepage: Boolean = true
) {
    // 使用延时初始化，只有访问过currentMailboxEmails时才会初始化
    val currentMailboxEmails: List<Email> by lazy { mailboxes[currentMailbox]!! }
}