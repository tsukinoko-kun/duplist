package de.kuerbisskraft.duplist

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CmdInterpreter(
    private val dataManager: DataManager,
    private val texts: Texts,
    private val permissionManager: PermissionManager
) {
    fun onCommand(sender: CommandSender, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(texts.onHelp())
            return true
        }

        val argsSize = args.size

        when (args[0]) {
            "melden" -> {
                if (argsSize == 6 && dataManager.report(sender, args[1], args[2], args[3], args[4], args[5])) {
                    sender.sendMessage(texts.onReportSuccessful())
                    return true
                }

                sender.sendMessage(texts.onReportSyntaxError())
            }

            "list" -> {
                when (argsSize) {
                    1 -> {
                        sender.sendMessage(dataManager.list())
                        return true
                    }

                    2 -> {
                        val amount = args[1].toIntOrNull()
                        if (amount == null) {
                            sender.sendMessage(dataManager.list())
                        } else {
                            sender.sendMessage(dataManager.list(amount))
                        }

                        return true
                    }
                }
            }

            "permission" -> {
                val player = Bukkit.getPlayer(args[1])
                if (player == null) {
                    return false
                }

                val permission = args[2]
                if (!texts.permissions().contains(permission)) {
                    return false
                }

                val value: Boolean? = if (argsSize == 4) {
                    when (args[3]) {
                        "true" -> true
                        "false" -> false
                        else -> null
                    }
                } else {
                    null
                }

                permissionManager.call(sender, player, permission, value)
            }

            "help", "?" -> {
                sender.sendMessage(texts.onHelp())
                return true
            }
        }

        return false
    }
}
