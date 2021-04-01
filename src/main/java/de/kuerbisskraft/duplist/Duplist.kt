package de.kuerbisskraft.duplist

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KoinApiExtension
class Duplist : JavaPlugin(), Listener, CommandExecutor, KoinComponent {
    private val cmdInterpreter: CmdInterpreter
    private val cmdTabComplete: CmdTabComplete

    init {
        registerModules()
        cmdInterpreter = inject<CmdInterpreter>().value
        cmdTabComplete = inject<CmdTabComplete>().value
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name == "duplist") {
            return cmdInterpreter.onCommand(sender, args)
        }
        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (command.name != "duplist") {
            return mutableListOf()
        }

        return cmdTabComplete.onTabComplete(sender, args)
    }

    private fun registerModules() {
//        val plugin = this

        val koinModules = module {
//            single<Plugin> { plugin }
//            single { Bukkit.getLogger() }
            single { Colors() }
            single { Texts(get()) }
            single { PermissionManager(get(), get()) }
            single { DataManager(get(), get()) }
            single { CmdTabComplete(get(), get()) }
            single { CmdInterpreter(get(), get(), get()) }
        }

        startKoin {
            modules(koinModules)
        }
    }
}
