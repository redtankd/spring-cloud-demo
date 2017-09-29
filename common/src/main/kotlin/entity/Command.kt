package entity;

import java.io.Serializable
import java.util.*

interface Command : Serializable

enum class CommandAction {
    SUBMIT, MODIFY, CANCEL
}

data class QuoteRequestCommand(
        val action: CommandAction,
        val id: Int,
        val QuoteRequest: QuoteRequest
) : Command

data class Login(var userId: Int, var date: Date) : Serializable
