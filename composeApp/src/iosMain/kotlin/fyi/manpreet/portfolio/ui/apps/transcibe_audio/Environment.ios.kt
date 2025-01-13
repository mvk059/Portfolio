package fyi.manpreet.portfolio.ui.apps.transcibe_audio

import platform.Foundation.NSBundle
import platform.Foundation.NSDictionary
import platform.Foundation.dictionaryWithContentsOfFile

actual fun getApiKey(): String {
    val bundle = NSBundle.mainBundle
    val configPath = bundle.pathForResource("Config", "plist") ?: error("Config.plist not found")
    val config = NSDictionary.dictionaryWithContentsOfFile(configPath) ?: error("Could not read Config.plist")
    return config["GROQ"] as? String ?: error("GROQ not found in Config.plist")
}
