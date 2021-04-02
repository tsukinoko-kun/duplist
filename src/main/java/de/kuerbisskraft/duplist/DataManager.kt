package de.kuerbisskraft.duplist

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.roundToLong


class DataManager(private val texts: Texts, private val colors: Colors) {
    private val gson = Gson()
    private val reports = mutableListOf<Report>()
    private val filePath: String
    private var lastHash: Int = -1

    init {
        val dir = "plugins/duplist/"
        filePath = dir + "reports.json"
        val path = File(dir)
        if (!path.exists()) {
            File(dir).mkdirs()
        }

        val file = File(filePath)
        if (file.exists()) {
            // Read File
            val fr = FileReader(filePath)
            val json = fr.readText()
            lastHash = json.hashCode()
            fr.close()

            // Parse Json
            val type: Type = object : TypeToken<List<Report>>() {}.type
            val impArr: List<Report> = gson.fromJson(json, type)
            for (el in impArr) {
                reports.add(el)
            }
        }

        val tick = Timer()
        tick.schedule(timerTask {
            store()
        }, 5000, 10000)
    }

    fun store() {
        val json = gson.toJson(reports)
        val newHash = json.hashCode()

        if (newHash == lastHash) {
            return
        }

        lastHash = newHash
        val fw = FileWriter(filePath)
        fw.write(json)
        fw.close()
    }

    fun report(
        reporter: CommandSender,
        locX: String,
        locY: String,
        locZ: String,
        reason: String,
        value: String
    ): Boolean {
        if (!reporter.hasPermission("duplist.meldung")) {
            reporter.sendMessage(texts.onNoPermission())
            return true
        }

        if (!texts.reasons().contains(reason)) {
            return false
        }

        val reporterLocation = (reporter as Player).location

        val x: Double? = if (locX == "~") {
            reporterLocation.x
        } else {
            locX.toDoubleOrNull()
        }
        val y: Double? = if (locY == "~") {
            reporterLocation.y
        } else {
            locY.toDoubleOrNull()
        }
        val z: Double? = if (locZ == "~") {
            reporterLocation.z
        } else {
            locZ.toDoubleOrNull()
        }

        val v: Double? = value.toDoubleOrNull()


        if (x == null || y == null || z == null || v == null) {
            return false
        }

        if (report(
                x, y, z, reason, v, if (reporterLocation.world != null) {
                    reporterLocation.world!!.name
                } else {
                    return false
                }
            )
        ) {
            reporter.sendMessage(texts.onReportSuccessful())
            return true
        }

        return false
    }

    private fun report(
        locX: Double,
        locY: Double,
        locZ: Double,
        reason: String,
        value: Double,
        world: String
    ): Boolean {
        if (reports.add(
                Report(
                    locX, locY, locZ, reason, value, world
                )
            )
        ) {
//            reports.sortedByDescending {
//                it.value
//            }
            return true
        }
        return false
    }

    fun list(player: CommandSender, amount: Int = 8): String {
        if (!player.hasPermission("duplist.list")) {
            return texts.onNoPermission()
        }


        val out = StringBuilder()
        out.appendLine("${colors.getPrimaryColor()}Duplist: ")

        if (reports.isEmpty()) {
            out.appendLine("${colors.getErrorColor()}leer")
        } else {
            var i = 0
            for (report in reports) {
                if (i == amount) {
                    break
                }

                out.appendLine("${colors.getSecondAccentColor()}${i}${colors.getAccentColor()}: ${reportToString(report)}")

                i++
            }
        }

        return out.toString()
    }

    private fun reportToString(r: Report): String {
        return "${r.world} : ${r.reason} @ ${r.locX.roundToLong()}/${r.locY.roundToLong()}/${r.locZ.roundToLong()} : ${r.value}"
    }

    fun teleport(player: Player, index: Int): Boolean {
        if (!player.hasPermission("duplist.tp")) {
            player.sendMessage(texts.onNoPermission())
            return true
        }

        if (reports.count() > index) {
            val reportData = reports[index]
            val world = Bukkit.getWorld(reportData.world) ?: return false
            val location = Location(world, reportData.locX, reportData.locY, reportData.locZ)
            return player.teleport(location)
        }

        return false
    }

    fun reportCount(): Int {
        return reports.size
    }

    fun delete(sender: CommandSender, index: Int): Boolean {
        if (!sender.hasPermission("duplist.del")) {
            sender.sendMessage(texts.onNoPermission())
            return true
        }

        if (reports.size > index) {
            reports.removeAt(index)
            sender.sendMessage(texts.onDelete(index))
            return true
        }

        return false
    }
}
