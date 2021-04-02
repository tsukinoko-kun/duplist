package de.kuerbisskraft.duplist

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CmdTabComplete(private val texts: Texts, private val dataManager: DataManager) {
    fun onTabComplete(
        sender: CommandSender,
        args: Array<out String>
    ): MutableList<String> {
        if (sender !is Player) {
            return mutableListOf()
        }

        when (args.size) {
            1 -> {
                return mutableListOf("permission", "del", "tp", "teleport", "list", "melden", "help")
            }

            2 -> {
                when (args[0]) {
                    "melden" -> {
                        return mutableListOf(sender.location.blockX.toString(), "~")
                    }

                    "list" -> {
                        return mutableListOf("8")
                    }

                    "permission" -> {
                        val ret = mutableListOf<String>()
                        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                            ret.add(onlinePlayer.name)
                        }
                        return ret
                    }

                    "tp", "teleport", "del" -> {
                        val ret = mutableListOf<String>()
                        for (i in 0 until dataManager.reportCount()) {
                            ret.add(i.toString())
                        }
                        return ret
                    }
                }
            }

            3 -> {
                when (args[0]) {
                    "melden" -> {
                        return mutableListOf(sender.location.blockY.toString(), "~")
                    }

                    "permission" -> {
                        val ret = mutableListOf<String>()
                        for (perm in texts.permissions()) {
                            ret.add(perm)
                        }
                        return ret
                    }
                }
            }

            4 -> {
                when (args[0]) {
                    "melden" -> {
                        return mutableListOf(sender.location.blockZ.toString(), "~")
                    }
                }
            }

            5 -> {
                when (args[0]) {
                    "melden" -> {
                        val ret = mutableListOf<String>()
                        for (permission in texts.reasons()) {
                            ret.add(permission)
                        }
                        return ret
                    }
                }
            }

            6 -> {
                when (args[0]) {
                    "melden" -> {
                        return mutableListOf("1")
                    }
                }
            }
        }

        return mutableListOf()
    }
}
