package com.anarchyghost.service

import com.anarchyghost.models.Account
import com.anarchyghost.models.Code
import com.anarchyghost.models.OperationResult
import com.anarchyghost.utils.text
import jakarta.mail.Folder
import jakarta.mail.Message
import jakarta.mail.Session
import jakarta.mail.Store
import java.util.*

class ImapService {
    object SessionPropertiesConstants {
        const val IMAP_STORE_NAME = "imap"

        const val USE_SSL_PROPERTY_NAME = "mail.imap.ssl.enable"
    }

    private fun Account.toImapStore(): Store =
        Session.getInstance(Properties().also { props ->
            props.setProperty(SessionPropertiesConstants.USE_SSL_PROPERTY_NAME, this.useSsl.toString())
        }).getStore(SessionPropertiesConstants.IMAP_STORE_NAME).also { store ->
            store.connect(this.host, this.port, this.username, this.password)
        }

    private fun Store.getFolder(settings: Account): Folder {
        return this.getFolder(settings.folder).also {
            it.open(Folder.READ_ONLY)
        }
    }

    private fun Folder.getMessages(settings: Account): List<Message> {
        val messagesCount = this.messageCount
        val firstCount = if (messagesCount > settings.searchCount) messagesCount - settings.searchCount else 1
        return this.getMessages(firstCount, messagesCount).toList()
    }

    private fun List<Message>.findLast(settings: Account): Message? {
        return this.findLast { message -> message.subject.matches(Regex(settings.titleRegex)) }
    }

    private fun String.toCodeRegex() = Regex("\\b$this\\b")

    private fun Regex.findFirst(string: String) = this.find(string)?.groups?.first()?.value

    private fun Message.findCode(settings: Account): String? {
        val codePattern = settings.codeRegex.toCodeRegex()
        val headerCode = codePattern.findFirst(this.subject)
        if (headerCode != null || !settings.searchInBody) return headerCode
        return codePattern.findFirst(this.text)
    }

    private fun String.toCodeModel(): Code {
        return Code(code = this)
    }

    fun getEmailsBySettings(settings: Account): OperationResult<out Code> {
        try {
            val store = settings.toImapStore()
            val inboxFolder = store.getFolder(settings)
            val messages = inboxFolder.getMessages(settings)
            val message = messages.findLast(settings)
            val code = message?.findCode(settings)
            try {
                inboxFolder.close()
                store.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return if (code != null) {
                OperationResult.Success(data = code.toCodeModel())
            } else {
                OperationResult.NotFound
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return OperationResult.InternalError
        }
    }
}