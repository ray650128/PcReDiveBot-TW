package commands

import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.Arrays

abstract class BotCommand {
    abstract val commandName: Array<String>
    abstract val commandDescription: String

    abstract fun exec(event: MessageReceivedEvent, args: String)
}