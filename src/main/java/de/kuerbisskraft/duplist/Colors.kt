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
        else {
            settings["primary"] = "${ChatColor.RESET}"
            settings["accent"] = "${ChatColor.GREEN}"
            settings["error"] = "${ChatColor.RED}"

            // store defaults
            val json = gson.toJson(settings)
            val fw = FileWriter(filePath)
            fw.write(json)
            fw.close()
        }
    }

    fun getPrimaryColor(): String {
        if (settings.containsKey("primary")) {
            return settings["primary"]!!
        }

        throw Exception("Primary Color not found")
    }

    fun getAccentColor(): String {
        if (settings.containsKey("accent")) {
            return settings["accent"]!!
        }

        throw Exception("Accent Color not found")
    }

    fun getErrorColor(): String {
        if (settings.containsKey("error")) {
            return settings["error"]!!
        }

        throw Exception("Error Color not found")
    }
}
