package de.kuerbisskraft.duplist

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PermissionManager(private val texts: Texts, private val colors: Colors) {
    fun call(sender: CommandSender, player: Player, permission: String) {
        if (!sender.isOp && !sender.hasPermission("duplist.op")) {
            sender.sendMessage(texts.onNoPermission())
            return
        }

        sender.sendMessage(
            "${colors.getPrimaryColor()}${player.name}.${permission}: ${colors.getAccentColor()}${
                player.hasPermission(
                    permission
                )
            }"
        )
    }
}
