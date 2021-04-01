package de.kuerbisskraft.duplist

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DataManager(private val texts: Texts) {
    private val reports = mutableListOf<Report>()

    fun report(
        reporter: CommandSender,
        locX: String,
        locY: String,
        locZ: String,
        reason: String,
        value: String
    ): Boolean {
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

        return report(x, y, z, reason, v)
    }

    private fun report(locX: Double, locY: Double, locZ: Double, reason: String, value: Double): Boolean {
        return reports.add(
            Report(
                locX, locY, locZ, reason, value
            )
        )
    }

    fun list(amount: Int = 8): String {
        var i = 0

        val out = StringBuilder()

        for (report in reports) {
            if (i == amount) {
                break
            }

            out.append(reportToString(report))

            i++
        }

        return out.toString()
    }

    private fun reportToString(r: Report): String {
        return "${r.locX}/${r.locY}/${r.locZ} : ${r.reason}"
    }
}
