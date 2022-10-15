import commands.BotCommand
import commands.CompensateCalc
import commands.Help
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object MyListenerAdapter : ListenerAdapter() {

    private val LOGGER: Logger = LoggerFactory.getLogger(MyListenerAdapter::class.java)

    private const val prefix: String = "!"

    private val commandList = listOf(
        Help(),
        CompensateCalc()
    )

    override fun onMessageReceived(event: MessageReceivedEvent) {
        // 判斷發起人是否為 bot
        if (event.author.isBot) return

        val message = event.message.contentRaw

        LOGGER.info("[${event.author.name}]說: $message")

        // 檢查是否包含前綴
        when (message.startsWith(prefix, true)) {
            true -> callCommand(commandList, event)
            false -> return
        }
    }

    private fun callCommand(source: List<BotCommand>, event: MessageReceivedEvent) {
        val message = event.message.contentRaw
        val input = message.substring(1)    // 去掉指令前綴
        val inputCommand = input.split(" ")[0]  // 取得指令

        // 搜尋指令陣列裡是否有對應的指令
        val command = source.firstOrNull { item ->
            item.commandName.firstOrNull { it == inputCommand } != null
        } ?: return

        // 找到指令，呼叫該指令
        val length = inputCommand.length + 2 // prefix + space
        val args = if (message.length > length) message.substring(length) else ""
        command.exec(event, args)
    }
}