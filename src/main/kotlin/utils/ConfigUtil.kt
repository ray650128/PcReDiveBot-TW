package utils

import com.google.gson.Gson
import model.Configure
import java.io.File

class ConfigUtil {
    private val configFile = File("config.json")

    lateinit var config: Configure
        private set

    fun init(): Boolean {
        if (!configFile.exists()) {
            println("找不到 config.json 檔案，\n" +
                    "請依照 config.example.json 檔案建立 config.json 後，再啟動本程式。")
            return false
        }

        val configRawText = configFile.readText(Charsets.UTF_8)
        if (configRawText.isEmpty()) {
            println("config.json 檔案空白，\n" +
                    "請依照 config.example.json 檔案建立 config.json 後，再啟動本程式。")
            return false
        }

        config = Gson().fromJson(configRawText, Configure::class.java)

        return true
    }
}