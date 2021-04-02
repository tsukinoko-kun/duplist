package de.kuerbisskraft.duplist

import org.bukkit.command.CommandSender

class Texts(private val colors: Colors) {
    private val _reasons: Array<String> = arrayOf("duper", "scammer", "ph-falle")
    fun reasons() = _reasons

    private val _permissions: Array<String> = arrayOf("duplist.meldung", "duplist.list", "duplist.op", "duplist.tp", "duplist.del")
    fun permissions() = _permissions

    private val _onReportSuccessful: String =
        "${colors.getOkColor()}Danke, dass Du mit deiner Meldung dem Server geholfen hast!"

    fun onReportSuccessful() = _onReportSuccessful

    private val _onReportSyntaxError: String =
        "${colors.getErrorColor()}Du musst die Koordinaten (X, Y, Z) mit dem Grund [${_reasons[0]}, ${_reasons[1]}, ${_reasons[2]}] und einem Wert angeben"

    fun onReportSyntaxError() = _onReportSyntaxError

    fun onHelp(player: CommandSender): String {
        val out = StringBuilder()
        out.appendLine("${colors.getPrimaryColor()}duplist?")

        if (player.hasPermission("duplist.meldung")) {
            out.appendLine("${colors.getAccentColor()}melden${colors.getPrimaryColor()}: Erstelle eine neue Meldungen")
        }
        if (player.hasPermission("duplist.op")) {
            out.appendLine("${colors.getAccentColor()}permission${colors.getPrimaryColor()}: Prüfe die Berechtigung eines Spielers")
        }
        if (player.hasPermission("duplist.list")) {
            out.appendLine("${colors.getAccentColor()}list${colors.getPrimaryColor()}: Auflisten der Meldungen")
        }
        if (player.hasPermission("duplist.tp")) {
            out.appendLine("${colors.getAccentColor()}teleport${colors.getPrimaryColor()}: Teleportiere dich zu den Koordinaten")
        }
        if (player.hasPermission("duplist.del")) {
            out.appendLine("${colors.getAccentColor()}del${colors.getPrimaryColor()}: Lösche einen Eintrag")
        }

        return out.toString()
    }

    private val _onNoPermission: String = "${colors.getErrorColor()}Du hast nicht die nötige Berechtigung!"
    fun onNoPermission() = _onNoPermission

    private val _teleportFailed: String = "${colors.getErrorColor()}Teleport fehlgeschlagen!"
    fun onTeleportFailed() = _teleportFailed

    private val _onDeleteFailed: String = "${colors.getErrorColor()}Löschen fehlgeschlagen!"
    fun onDeleteFailed() = _onDeleteFailed

    fun onDelete(index: Int) = "${colors.getOkColor()}Eintrag an Index $index gelöscht"
}
