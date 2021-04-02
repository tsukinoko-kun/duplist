package de.kuerbisskraft.duplist

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.ChatColor
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type

class Colors {
    private val gson = Gson()
    private val filePath: String
    private val settings = hashMapOf<String, String>()

    init {
        val dir = "plugins/duplist/"
        filePath = dir + "settings.json"
        val path = File(dir)
        if (!path.exists()) {
            File(dir).mkdirs()
        }

        // Load custom settings
        val file = File(filePath)
        if (file.exists()) {
            // Read File
            val fr = FileReader(filePath)
            val json = fr.readText()
            fr.close()

            // Parse Json
            val type: Type = object : TypeToken<Map<String, String>>() {}.type
            val impArr: Map<String, String> = gson.fromJson(json, type)
            for (el in impArr) {
                settings[el.key] = el.value
            }
        }

        // Add default values
        var needToWrite = false
        if (!settings.containsKey("Primary")) {
            settings["primary"] = "${ChatColor.RESET}"
            needToWrite = true
        }
        if (!settings.containsKey("accent")) {
            settings["accent"] = "${ChatColor.GREEN}"
            needToWrite = true
        }
        if (!settings.containsKey("accent1")) {
            settings["accent1"] = "${ChatColor.YELLOW}"
            needToWrite = true
        }
        if (!settings.containsKey("error")) {
            settings["error"] = "${ChatColor.RED}"
            needToWrite = true
        }
        if (!settings.containsKey("ok")) {
            settings["ok"] = "${ChatColor.GOLD}"
            needToWrite = true
        }

        // Store defaults
        if (needToWrite) {
            // store defaults
            val json = gson.toJson(settings)
            val fw = FileWriter(filePath)
            fw.write(json)
            fw.close()
        }
    }

    fun getPrimaryColor(): String {
        return getColor("primary")
    }

    fun getAccentColor(): String {
        return getColor("accent")
    }

    fun getSecondAccentColor(): String {
        return getColor("accent1")
    }

    fun getErrorColor(): String {
        return getColor("error")
    }

    fun getOkColor(): String {
        return getColor("ok")
    }

    private fun getColor(key: String): String {
        if (settings.containsKey(key)) {
            return settings[key]!!
        }

        throw notFound(key)
    }

    private fun notFound(key: String): Exception {
        return Exception("duplist.color.${key} not found, delete plugins/duplist/settings.json to restore defaults")
    }
}
