# PcReDiveBot-TW

## 超異域公主連結 Re: Dive 戰隊戰補償刀轉換 Discord 機器人

#### 本機器人參考 [終將成為妳](https://www.youtube.com/channel/UCvN59KwVSCv0KaAUuAYyUew/videos) 開發的 [補償刀文字軸秒數轉換機器人](https://github.com/YungPingXu/pcr-bot)。

#### 本 Discord 機器人採用 Kotlin+JDA 撰寫。

---



### 安裝方式

**注意：請具備一定能力再使用 請具備一定能力再使用 請具備一定能力再使用**

1. 下載並安裝OpenJDK 11，並確定JAVA_HOME有加到環境變數裡面
2. 透過 git clone 本專案到你的電腦中
3. 複製 config.example.json 並更名成 config.json
4. 用記事本或任何的文字編輯器打開 config.json，將 Discord bot 的 token 貼到雙引號內，如下：
   ```
   "token": ""
   ```
5. 利用 terminal 進到專案目錄
6. gradlew.bat clean
7. gradlew.bat shadowJar
8. 透過 java -jar ./build/libs/Bot-1.0-all.jar 指令執行本機器人
