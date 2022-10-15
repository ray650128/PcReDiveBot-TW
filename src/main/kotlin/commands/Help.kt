package commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class Help: BotCommand() {
    override val commandName: Array<String> = arrayOf("help", "h")
    override val commandDescription: String = "機器人說明 help"

    override fun exec(event: MessageReceivedEvent, args: String) {
        val embed = EmbedBuilder().apply {
            setTitle("機器人使用說明書")
            setDescription("")
            setColor(1500903)
            setImage("https://i.imgur.com/9NaJ463.jpg")
            setAuthor("作者：楓小夜", "https://github.com/ray650128")
            addField(
                "tr",
                "補償刀文字軸轉換，用法：\n!tr 補償時間\n文字軸",
                false,
            )
        }.build()
        event.channel.sendMessageEmbeds(mutableListOf(embed)).queue()
    }
}