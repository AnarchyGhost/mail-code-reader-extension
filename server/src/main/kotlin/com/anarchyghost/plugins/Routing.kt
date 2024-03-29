package com.anarchyghost.plugins

import com.anarchyghost.models.Account
import com.anarchyghost.models.OperationResult
import com.anarchyghost.service.ImapService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*



fun Application.configureRouting() {
    val imapService = ImapService()
    routing {
        post("/getCode") {
            when(val code = imapService.getEmailsBySettings(call.receive<Account>())) {
                is OperationResult.NotFound -> call.respond(HttpStatusCode.NotFound)
                is OperationResult.InternalError -> call.respond(HttpStatusCode.InternalServerError)
                is OperationResult.Success -> call.respond(code.data)
            }
        }
    }
}
