import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.cache.CacheFlag
import kotlin.system.exitProcess

private val botEventListener = object : ListenerAdapter() {

    private val prefix = "!"

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)
        // 判斷發起人是否為 bot
        if (event.author.isBot) return
        if (!event.message.contentRaw.startsWith(prefix, true)) return

        println("[${event.author.name}]: ${event.message.contentRaw}")
        processMessage(event)
    }
}

private fun processMessage(event: MessageReceivedEvent) {
    val message = event.message.contentRaw
    val input = message.substring(1)    // 去掉指令前綴
    val inputCommand = input.split(" ")[0]  // 取得指令

    println("$input\n$inputCommand")
}

fun main(args: Array<String>) {
    val configUtil = ConfigUtil()
    if (!configUtil.init()) {
        exitProcess(0)
    }

    val jda = JDABuilder.createDefault(configUtil.config.token)
    jda.apply {
        disableCache(CacheFlag.ACTIVITY, CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
        setChunkingFilter(ChunkingFilter.NONE)
        enableIntents(
            arrayListOf(
                GatewayIntent.GUILD_MESSAGES
            )
        )
        addEventListeners(botEventListener)
    }.build()
}