package de.kuerbisskraft.duplist

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PermissionManager(private val texts: Texts) {
    val permissionStore = hashMapOf<String, Boolean>()

    fun call(sender: CommandSender, player: Player, permission: String, value: Boolean?) {
        val id = "${player.name}.${permission}"
        when (value) {
            null -> {
                sender.sendMessage("${id}: ${permissionStore[id]}")
            }

            else -> {
                permissionStore[id] = value

                val msg = "${id}: $value"

                sender.sendMessage(msg)
                player.sendMessage(msg)
            }
        }
    }

}
