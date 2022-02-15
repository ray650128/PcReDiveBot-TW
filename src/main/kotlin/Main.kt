import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.cache.CacheFlag
import kotlin.system.exitProcess

private val botEventListener = object : ListenerAdapter() {}

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