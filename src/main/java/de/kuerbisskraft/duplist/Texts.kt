package de.kuerbisskraft.duplist

import java.lang.StringBuilder

class Texts {
    private val _reasons: Array<String> = arrayOf("duper", "scammer", "ph-falle")
    fun reasons() = _reasons

    private val _permissions: Array<String> = arrayOf("meldung", "list", "op")
    fun permissions() = _permissions

    private val _onReportSuccessful: String = "Danke, dass Du mit deiner Meldung dem Server geholfen hast!"
    fun onReportSuccessful() = _onReportSuccessful

    private val _onReportSyntaxError: String =
        "Du musst die Koordinaten (X, Y, Z) mit dem Grund $_reasons und einem Wert angeben"
    fun onReportSyntaxError() = _onReportSyntaxError

    fun onHelp(): String {
        val out = StringBuilder()



        return out.toString()
    }
}
