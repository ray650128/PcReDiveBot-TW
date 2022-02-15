import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.util.regex.Pattern
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

    when (inputCommand) {  // 取得指令
        "tr" -> {
            val length = inputCommand.length + 2 // prefix + space
            val params = if (message.length > length) {
                message.substring(length)
            } else ""
            event.message.reply(timeRipple(params)).queue()
        }
        else -> return
    }
}


private fun timeRipple(message: String): String {
    val tr = Pattern.compile("\\s*(\\d+)\\s*\n([\\s\\S]+)").matcher(message)
    if (tr.matches()) { // 判斷是否符合指令格式
        val time = tr.group(1).toIntOrNull() ?: return "輸入的秒數格式錯誤！正確的格式為\n!tr 補償秒數\n文字軸\n\n(補償秒數後面請直接換行，不要有其他字元)"  // 拆分時間與文字軸
        if (time in 1..90) {
            val lines = tr.group(2).split("\n")
            var resultLine = ""
            for (line in lines) {
                val filter = line
                    .replace(": ", "")
                    .replace(":", "")
                    .replace("\t", "")
                val match = Pattern.compile("(\\D*)(\\d{2,3})((\\s*[/~-]\\s*)(\\d{2,3}))?(.*)?").matcher(filter)
                if (match.matches()) {
                    val content1 = match.group(1)
                    val timeRange = match.group(3) // 056~057 這種有範圍的時間
                    val time1 = match.group(2).toInt()
                    var time2 = 0
                    if (!timeRange.isNullOrEmpty() && match.group(5).isNotEmpty()) {
                        time2 = match.group(5).toInt()
                    }
                    val rangeContent = match.group(4)
                    val content2 = match.group(6)
                    if (checkTime(time1) && ((timeRange.isNullOrEmpty() && match.group(5)
                            .isNullOrEmpty()) || (!timeRange.isNullOrEmpty() && !match.group(5)
                            .isNullOrEmpty() && checkTime(time2)))
                    ) {
                        val totalTime1 = time1 % 100 + (time1 / 100) * 60 // time1的秒數
                        val newTime1 = totalTime1 - (90 - time)
                        if (newTime1 < 0) { // 如果時間到了 後續的就不要轉換
                            break // 迴圈跳出
                        }
                        val result: String = if (match.group(5).isNullOrEmpty()) {
                            content1 + transformTime(newTime1) + content2
                        } else {
                            val totalTime2 = time2 % 100 + time2 / 100 * 60 // time2的秒數
                            val newTime2 = totalTime2 - (90 - time)
                            content1 + transformTime(newTime1) + rangeContent + transformTime(newTime2) + content2
                        }
                        resultLine += result
                    } else {
                        resultLine += line
                    }
                } else {
                    resultLine += line
                }
                resultLine += "\n"
            }
            return (resultLine)
        } else {
            return ("輸入的補償秒數錯誤，秒數必須要在 1～90 之間！")
        }
    } else {
        return ("輸入的秒數格式錯誤！正確的格式為\n!tr 補償秒數\n文字軸\n\n(補償秒數後面請直接換行，不要有其他字元)")
    }
}

private fun checkTime(number: Int): Boolean {
    return (number in 0..130) &&
            ((number / 100 == 0 && number % 100 in 0..59) || (number / 100 == 1 && number % 100 in 0..30))
}

private fun transformTime(originalTime: Int): String {
    return if (originalTime < 60) {
        String.format("%03d", originalTime)
    } else {
        String.format("%d%02d", originalTime / 60, originalTime % 60)
    }
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