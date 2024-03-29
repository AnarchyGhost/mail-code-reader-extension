package com.anarchyghost.utils

import jakarta.mail.BodyPart
import jakarta.mail.Message
import jakarta.mail.internet.MimeMultipart
import org.jsoup.Jsoup

val Message.text: String
    get(): String {
        if (this.isMimeType("text/plain")) {
            return this.content.toString()
        }
        if (this.isMimeType("multipart/*")) {
            return (this.content as MimeMultipart).text
        }
        return ""
    }

val MimeMultipart.text: String
    get() {
        var result = ""
        for (i in 0 until this.count) {
            val bodyPart = this.getBodyPart(i)
            if (bodyPart.isMimeType("text/plain")) {
                return """
                $result
                ${bodyPart.content}
                """.trimIndent()
            }
            result += bodyPart.text
        }
        return result
    }

val BodyPart.text: String
    get() {
        if (this.isMimeType("text/html")) {
            return """
                ${Jsoup.parse(this.content.toString()).text()}
            """.trimIndent()
        }
        if (this.content is MimeMultipart) {
            return (this.content as MimeMultipart).text
        }

        return ""
    }
