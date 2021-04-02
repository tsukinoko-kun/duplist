package de.kuerbisskraft.duplist

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CmdInterpreter(
    private val dataManager: DataManager,
    private val texts: Texts,
    private val permissionManager: PermissionManager
) {
    fun onCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(texts.onHelp(sender))
            return true
        }

        val argsSize = args.size

        when (args[0]) {
            "melden" -> {
                if (argsSize == 6 && dataManager.report(sender, args[1], args[2], args[3], args[4], args[5])) {
                    return true
                }

                sender.sendMessage(texts.onReportSyntaxError())
            }

            "list" -> {
                when (argsSize) {
                    1 -> {
                        sender.sendMessage(dataManager.list(sender))
                        return true
                    }

                    2 -> {
                        val amount = args[1].toIntOrNull()
                        if (amount == null) {
                            sender.sendMessage(dataManager.list(sender))
                        } else {
                            sender.sendMessage(dataManager.list(sender, amount))
                        }

                        return true
                    }
                }
            }

            "tp", "teleport" -> {
                when (argsSize) {
                    1 -> {
                        return if (dataManager.teleport(sender as Player, 0)) {
                            true
                        } else {
                            sender.sendMessage(texts.onTeleportFailed())
                            false
                        }
                    }

                    2 -> {
                        val index = args[1].toIntOrNull()
                        return if (dataManager.teleport(sender as Player, index ?: 1)) {
                            true
                        } else {
                            sender.sendMessage(texts.onTeleportFailed())
                            false
                        }
                    }
                }
            }

            "del" -> {
                if (argsSize < 2) {
                    sender.sendMessage(texts.onDeleteFailed())
                    return false
                }

                val index = args[1].toIntOrNull()
                if (index == null || !dataManager.delete(sender, index)) {
                    sender.sendMessage(texts.onDeleteFailed())
                }
            }

            "permission" -> {
                if (argsSize < 3) {
                    return false
                }

                val player = Bukkit.getPlayer(args[1]) ?: return false

                val permission = args[2]
                if (!texts.permissions().contains(permission)) {
                    return false
                }

//                val value = if (argsSize >= 4) {
//                    when (args[3]) {
//                        "true" -> true
//                        "false" -> false
//                        else -> null
//                    }
//                } else {
//                    null
//                }

                permissionManager.call(sender, player, permission)
            }

            "help", "?" -> {
                sender.sendMessage(texts.onHelp(sender))
                return true
            }
        }

        return false
    }
}
